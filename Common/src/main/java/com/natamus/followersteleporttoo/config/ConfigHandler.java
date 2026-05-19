package com.natamus.followersteleporttoo.config;

import com.natamus.followersteleporttoo.util.Reference;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	public static boolean disableFollowerDamageAfterTeleport = true;
	public static int durationInSecondsDamageShouldBeDisabled = 20;

	public static void initConfig() {
		configMetaData.put("disableFollowerDamageAfterTeleport", Arrays.asList(
			"When enabled, disables damage for followers shortly after a teleport. This can prevent fall damage or suffocation from an estimate target position."
		));
		configMetaData.put("durationInSecondsDamageShouldBeDisabled", Arrays.asList(
			"How long in seconds damage should be disabled for after a teleport when disableFollowerDamageAfterTeleport is enabled."
		));

		try {
			Class<?> duskConfigClass = Class.forName("com.natamus.collective.config.DuskConfig");
			Method initMethod = duskConfigClass.getMethod("init", String.class, String.class, Class.class);
			initMethod.invoke(null, Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
		}
		catch (ReflectiveOperationException | LinkageError ignored) {

		}
	}
}
