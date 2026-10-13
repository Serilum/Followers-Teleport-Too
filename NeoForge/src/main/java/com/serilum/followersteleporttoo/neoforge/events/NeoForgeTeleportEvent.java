package com.serilum.followersteleporttoo.neoforge.events;

import com.serilum.followersteleporttoo.events.TeleportEvent;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

public class NeoForgeTeleportEvent {
	@SubscribeEvent
	public static void onFollowerDamage(LivingIncomingDamageEvent e) {
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
