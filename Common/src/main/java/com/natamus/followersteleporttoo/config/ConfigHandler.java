package com.natamus.followersteleporttoo.config;

import com.natamus.collective.config.DuskConfig;
import com.natamus.followersteleporttoo.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean disableFollowerDamageAfterTeleport = true;
	@Entry(min = 0, max = 86400) public static int durationInSecondsDamageShouldBeDisabled = 20;

	public static void initConfig() {
		configMetaData.put("disableFollowerDamageAfterTeleport", Arrays.asList(
			"When enabled, disables damage for followers shortly after a teleport. This can prevent fall damage or suffocation from an estimate target position."
		));
		configMetaData.put("durationInSecondsDamageShouldBeDisabled", Arrays.asList(
			"How long in seconds damage should be disabled for after a teleport when disableFollowerDamageAfterTeleport is enabled."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}