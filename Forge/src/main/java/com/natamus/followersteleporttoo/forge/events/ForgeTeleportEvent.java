package com.natamus.followersteleporttoo.forge.events;

import com.natamus.followersteleporttoo.events.TeleportEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ForgeTeleportEvent {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerTeleport(EntityTeleportEvent.TeleportCommand e) {
		if (e.isCanceled()) {
			return;
		}

		Entity entity = e.getEntity();
		TeleportEvent.onPlayerTeleport(entity.level, entity, e.getTargetX(), e.getTargetY(), e.getTargetZ());
	}

	@SubscribeEvent
	public void onFollowerDamage(LivingHurtEvent e) {
		LivingEntity livingEntity = e.getEntity();

		if (TeleportEvent.onFollowerDamage(livingEntity.getLevel(), livingEntity, e.getSource(), e.getAmount()) == 0F) {
			e.setAmount(0F);
			e.setCanceled(true);
		}
	}
}
