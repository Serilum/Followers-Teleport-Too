package com.serilum.followersteleporttoo.forge.events;

import com.serilum.followersteleporttoo.events.TeleportEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.lang.invoke.MethodHandles;

public class ForgeTeleportEvent {
	public static void registerEventsInBus() {
		// BusGroup.DEFAULT.register(MethodHandles.lookup(), ForgeTeleportEvent.class);

		LivingHurtEvent.BUS.addListener(ForgeTeleportEvent::onFollowerDamage);
	}

	@SubscribeEvent
	public static boolean onFollowerDamage(LivingHurtEvent e) {
		LivingEntity livingEntity = e.getEntity();
		float damageAmount = e.getAmount();
		if (damageAmount <= 0F) {
			return false;
		}

		if (TeleportEvent.onFollowerDamage(livingEntity.level(), livingEntity, e.getSource(), damageAmount) == 0F) {
			e.setAmount(0F);
			return true;
		}
		return false;
	}
}
