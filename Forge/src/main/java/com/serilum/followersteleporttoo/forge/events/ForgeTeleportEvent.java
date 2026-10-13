package com.serilum.followersteleporttoo.forge.events;

import com.serilum.followersteleporttoo.events.TeleportEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeTeleportEvent {
	@SubscribeEvent
	public static void onFollowerDamage(LivingHurtEvent e) {
		LivingEntity livingEntity = e.getEntity();
		float damageAmount = e.getAmount();
		if (damageAmount <= 0F) {
			return;
		}

		if (TeleportEvent.onFollowerDamage(livingEntity.level(), livingEntity, e.getSource(), damageAmount) == 0F) {
			e.setAmount(0F);
			e.setCanceled(true);
		}
	}
}
