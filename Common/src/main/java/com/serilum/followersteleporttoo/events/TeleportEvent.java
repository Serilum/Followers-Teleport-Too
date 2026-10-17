package com.serilum.followersteleporttoo.events;

import com.natamus.collective.functions.ConfigFunctions;
import com.serilum.followersteleporttoo.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class TeleportEvent {
	private static final HashMap<UUID, Date> teleportedfollowers = new HashMap<UUID, Date>();
	private static final HashMap<UUID, List<Mob>> followersAwaitingOwner = new HashMap<>();
	private static final HashMap<UUID, Mob> mountsAwaitingRider = new HashMap<>();

	public static void onPlayerTeleportStart(ServerPlayer player) {
		if (player.isSpectator()) {
			return;
		}

		if (ConfigHandler.mountTeleportsToo && player.getVehicle() instanceof Mob) {
			mountsAwaitingRider.put(player.getUUID(), (Mob)player.getVehicle());
		}

		BlockPos ppos = player.blockPosition();
		int radius = ConfigHandler.followerSearchRadiusInBlocks;

		List<Mob> followers = new ArrayList<>();
		for (Entity ea : player.level().getEntities(null, new AABB(ppos.getX()-radius, ppos.getY()-radius, ppos.getZ()-radius, ppos.getX()+radius, ppos.getY()+radius, ppos.getZ()+radius))) {
			if (!(ea instanceof Mob)) {
				continue;
			}

			Mob mob = (Mob)ea;
			if (mob.isLeashed() || mob.equals(player.getVehicle())) {
				continue;
			}

			ResourceLocation entityTypeKey = EntityType.getKey(mob.getType());
			if (ConfigFunctions.isInIdList(ConfigHandler.followerTypesThatStayBehind, entityTypeKey.toString())) {
				continue;
			}

			if (mob instanceof TamableAnimal) {
				if (!isFollowingOwner((TamableAnimal)mob, player, entityTypeKey.getNamespace())) {
					continue;
				}
			}
			else if (mob instanceof Allay) {
				if (!ConfigHandler.allaysTeleportToo || !isLikedBy((Allay)mob, player)) {
					continue;
				}
			}
			else {
				continue;
			}

			followers.add(mob);
		}

		followersAwaitingOwner.put(player.getUUID(), followers);
	}

	private static boolean isFollowingOwner(TamableAnimal ta, ServerPlayer player, String namespace) {
		if (!ta.isTame() || !ta.isOwnedBy(player)) {
			return false;
		}

		if (ta.isInSittingPose() || ta.isOrderedToSit()) {
			return false;
		}

		if (namespace.equals("alexsmobs") || namespace.equals("alexscaves")) {
			return isSetToFollow(ta, namespace);
		}
		return true;
	}

	private static boolean isSetToFollow(TamableAnimal ta, String namespace) {
		int followCommand = 1;
		if (namespace.equals("alexscaves")) {
			followCommand = 2;
		}

		CompoundTag entityData = ta.saveWithoutId(new CompoundTag());

		for (String key : entityData.getAllKeys()) {
			if (key.endsWith("Sitting") && entityData.getBoolean(key)) {
				return false;
			}
			if (key.endsWith("Command") && entityData.getInt(key) != followCommand) {
				return false;
			}
		}
		return true;
	}

	private static boolean isLikedBy(Allay allay, ServerPlayer player) {
		Optional<UUID> likedPlayer = allay.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
		return likedPlayer.isPresent() && likedPlayer.get().equals(player.getUUID());
	}

	public static void onPlayerTeleportEnd(ServerPlayer player, boolean teleported) {
		List<Mob> followers = followersAwaitingOwner.remove(player.getUUID());
		Mob mount = mountsAwaitingRider.remove(player.getUUID());
		if (!teleported) {
			return;
		}

		ServerLevel targetLevel = player.serverLevel();
		Vec3 targetvec = player.position();

		if (mount != null && !mount.isRemoved()) {
			if (teleportFollower(mount, targetLevel, targetvec)) {
				Entity teleportedMount = targetLevel.getEntity(mount.getUUID());
				if (teleportedMount != null) {
					player.startRiding(teleportedMount);
				}
			}
		}

		if (followers == null) {
			return;
		}

		for (Mob follower : followers) {
			if (follower.isRemoved()) {
				continue;
			}

			if (!ConfigHandler.followersFollowIntoOtherDimensions && !follower.level().equals(targetLevel)) {
				continue;
			}

			teleportFollower(follower, targetLevel, targetvec);
		}
	}

	private static boolean teleportFollower(Mob follower, ServerLevel targetLevel, Vec3 targetvec) {
		if (!follower.teleportTo(targetLevel, targetvec.x, targetvec.y, targetvec.z, Set.of(), follower.getYRot(), follower.getXRot())) {
			return false;
		}

		if (ConfigHandler.disableFollowerDamageAfterTeleport) {
			teleportedfollowers.put(follower.getUUID(), new Date());
		}
		return true;
	}

	public static float onFollowerDamage(Level level, Entity entity, DamageSource damageSource, float damageAmount) {
		if (!ConfigHandler.disableFollowerDamageAfterTeleport) {
			return damageAmount;
		}

		if (teleportedfollowers.size() > 0) {
			if (!(entity instanceof Mob)) {
				return damageAmount;
			}

			UUID uuid = entity.getUUID();
			if (teleportedfollowers.containsKey(uuid)) {
				Date lastteleported = teleportedfollowers.get(uuid);

				long ms = ((new Date()).getTime()-lastteleported.getTime());
				if (ms > ConfigHandler.durationInSecondsDamageShouldBeDisabled * 1000L) {
					teleportedfollowers.remove(uuid);
					return damageAmount;
				}

				return 0F;
			}
		}

		return damageAmount;
	}
}
