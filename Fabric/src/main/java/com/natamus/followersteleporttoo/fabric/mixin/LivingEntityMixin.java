package com.natamus.followersteleporttoo.fabric.mixin;

import com.natamus.followersteleporttoo.events.TeleportEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
	@Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
	private void followersteleporttoo$disableFollowerDamageAfterTeleport(ServerLevel serverLevel, DamageSource damageSource, float damageAmount, CallbackInfoReturnable<Boolean> cir) {
		Entity entity = (Entity)(Object)this;
		if (TeleportEvent.onFollowerDamage(serverLevel, entity, damageSource, damageAmount) == 0F) {
			cir.setReturnValue(false);
		}
	}
}
