package com.natamus.followersteleporttoo;

import com.natamus.followersteleporttoo.util.Reference;
import net.fabricmc.api.ModInitializer;

import java.lang.reflect.Method;

public class ModFabric implements ModInitializer {
	
	@Override
	public void onInitialize() {
		if (!shouldLoad()) {
			return;
		}

		setGlobalConstants();
		ModCommon.init();

		loadEvents();

		registerMod();
	}

	private void loadEvents() {

	}

	private static void setGlobalConstants() {

	}

	private static boolean shouldLoad() {
		try {
			Class<?> shouldLoadCheckClass = Class.forName("com.natamus.collective.check.ShouldLoadCheck");
			Method shouldLoadMethod = shouldLoadCheckClass.getMethod("shouldLoad", String.class);
			return (Boolean)shouldLoadMethod.invoke(null, Reference.MOD_ID);
		}
		catch (ReflectiveOperationException ignored) {
			return true;
		}
	}

	private static void registerMod() {
		try {
			Class<?> registerModClass = Class.forName("com.natamus.collective.check.RegisterMod");
			Method registerMethod = registerModClass.getMethod("register", String.class, String.class, String.class, String.class);
			registerMethod.invoke(null, Reference.NAME, Reference.MOD_ID, Reference.VERSION, Reference.ACCEPTED_VERSIONS);
		}
		catch (ReflectiveOperationException ignored) {

		}
	}
}
