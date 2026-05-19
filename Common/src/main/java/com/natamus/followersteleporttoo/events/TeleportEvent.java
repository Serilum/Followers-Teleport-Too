package com.natamus.followersteleporttoo.events;

import com.natamus.followersteleporttoo.config.ConfigHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TeleportEvent {
	private static final HashMap<UUID, Date> teleportedfollowers = new HashMap<UUID, Date>();

	public static void onPlayerTeleport(Level world, Entity entity, double targetX, double targetY, double targetZ) {
		if (world.isClientSide()) {
			return;
		}

		if (!(entity instanceof Player)) {
			return;
		}

		ServerPlayer player = (ServerPlayer)entity;
		List<TamableAnimal> followers = getFollowersToTeleport(world, player);
		teleportFollowers(followers, targetX, targetY, targetZ);
	}

	public static List<TamableAnimal> getFollowersToTeleport(Level world, ServerPlayer player) {
		List<TamableAnimal> followers = new ArrayList<TamableAnimal>();
		BlockPos ppos = player.blockPosition();
		for (Entity ea : world.getEntities(null, new AABB(ppos.getX()-50, ppos.getY()-50, ppos.getZ()-50, ppos.getX()+50, ppos.getY()+50, ppos.getZ()+50))) {
			if (ea instanceof TamableAnimal) {
				TamableAnimal ta = (TamableAnimal)ea;
				if (ta.isTame()) {
					if (!ta.isInSittingPose()) {
						if (ta.isOwnedBy(player)) {
							followers.add(ta);
						}
					}
				}
			}
		}

		return followers;
	}

	public static void teleportFollowers(List<TamableAnimal> followers, double targetX, double targetY, double targetZ) {
		if (followers == null) {
			return;
		}

		Vec3 targetvec = new Vec3(targetX, targetY, targetZ);
		for (TamableAnimal ta : followers) {
			ta.teleportTo(targetvec.x, targetvec.y, targetvec.z);

			if (ConfigHandler.disableFollowerDamageAfterTeleport) {
				teleportedfollowers.put(ta.getUUID(), new Date());
			}
		}
	}

	public static void teleportFollowers(List<TamableAnimal> followers, ServerLevel targetWorld, double targetX, double targetY, double targetZ) {
		if (followers == null) {
			return;
		}

		for (TamableAnimal ta : followers) {
			if (ta.teleportTo(targetWorld, targetX, targetY, targetZ, Set.<Relative>of(), ta.getYRot(), ta.getXRot(), false)) {
				if (ConfigHandler.disableFollowerDamageAfterTeleport) {
					teleportedfollowers.put(ta.getUUID(), new Date());
				}
			}
		}
	}

	public static float onFollowerDamage(Level level, Entity entity, DamageSource damageSource, float damageAmount) {
		if (!ConfigHandler.disableFollowerDamageAfterTeleport) {
			return damageAmount;
		}

		if (teleportedfollowers.size() > 0) {
			if (!(entity instanceof TamableAnimal)) {
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
