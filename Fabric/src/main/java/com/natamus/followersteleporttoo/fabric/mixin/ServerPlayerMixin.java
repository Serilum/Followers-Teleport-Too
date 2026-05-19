package com.natamus.followersteleporttoo.fabric.mixin;

import com.natamus.followersteleporttoo.events.TeleportEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
	@Unique
	private List<TamableAnimal> followersteleporttoo$followersToTeleport = null;

	@Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z", at = @At("HEAD"))
	private void followersteleporttoo$collectFollowersBeforeTeleport(ServerLevel targetWorld, double targetX, double targetY, double targetZ, Set<Relative> relatives, float yRot, float xRot, boolean shouldResetCamera, CallbackInfoReturnable<Boolean> cir) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		this.followersteleporttoo$followersToTeleport = TeleportEvent.getFollowersToTeleport(player.level(), player);
	}

	@Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z", at = @At("RETURN"))
	private void followersteleporttoo$teleportFollowersAfterTeleport(ServerLevel targetWorld, double targetX, double targetY, double targetZ, Set<Relative> relatives, float yRot, float xRot, boolean shouldResetCamera, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			TeleportEvent.teleportFollowers(this.followersteleporttoo$followersToTeleport, targetWorld, targetX, targetY, targetZ);
		}

		this.followersteleporttoo$followersToTeleport = null;
	}

	@Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At("HEAD"))
	private void followersteleporttoo$collectFollowersBeforeTransition(TeleportTransition teleportTransition, CallbackInfoReturnable<ServerPlayer> cir) {
		ServerPlayer player = (ServerPlayer)(Object)this;
		this.followersteleporttoo$followersToTeleport = TeleportEvent.getFollowersToTeleport(player.level(), player);
	}

	@Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At("RETURN"))
	private void followersteleporttoo$teleportFollowersAfterTransition(TeleportTransition teleportTransition, CallbackInfoReturnable<ServerPlayer> cir) {
		if (cir.getReturnValue() != null) {
			Vec3 targetPos = teleportTransition.position();
			TeleportEvent.teleportFollowers(this.followersteleporttoo$followersToTeleport, teleportTransition.newLevel(), targetPos.x, targetPos.y, targetPos.z);
		}

		this.followersteleporttoo$followersToTeleport = null;
	}
}
