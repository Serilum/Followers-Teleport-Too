package com.serilum.followersteleporttoo.mixin;

import com.serilum.followersteleporttoo.events.TeleportEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Relative;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(value = ServerPlayer.class, priority = 1001)
public class ServerPlayerMixin {
	@Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z", at = @At(value = "HEAD"))
	public void teleportTo_head(ServerLevel serverLevel, double x, double y, double z, Set<Relative> relatives, float yRot, float xRot, boolean resetCamera, CallbackInfoReturnable<Boolean> cir) {
		TeleportEvent.onPlayerTeleportStart((ServerPlayer)(Object)this);
	}

	@Inject(method = "teleportTo(Lnet/minecraft/server/level/ServerLevel;DDDLjava/util/Set;FFZ)Z", at = @At(value = "RETURN"))
	public void teleportTo_return(ServerLevel serverLevel, double x, double y, double z, Set<Relative> relatives, float yRot, float xRot, boolean resetCamera, CallbackInfoReturnable<Boolean> cir) {
		TeleportEvent.onPlayerTeleportEnd((ServerPlayer)(Object)this, cir.getReturnValue());
	}
}
