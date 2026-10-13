package com.serilum.followersteleporttoo.config;

import com.natamus.collective.config.DuskConfig;
import com.serilum.followersteleporttoo.util.Reference;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConfigHandler extends DuskConfig {
	public static HashMap<String, List<String>> configMetaData = new HashMap<String, List<String>>();

	@Entry public static boolean disableFollowerDamageAfterTeleport = true;
	@Entry(min = 0, max = 86400) public static int durationInSecondsDamageShouldBeDisabled = 20;
	@Entry(min = 1, max = 256) public static int followerSearchRadiusInBlocks = 50;
	@Entry public static String followerTypesThatStayBehind = "minecraft:nautilus";
	@Entry public static boolean followersFollowIntoOtherDimensions = true;
	@Entry public static boolean allaysTeleportToo = true;
	@Entry public static boolean mountTeleportsToo = true;

	public static void initConfig() {
		configMetaData.put("disableFollowerDamageAfterTeleport", Arrays.asList(
			"When enabled, disables damage for followers shortly after a teleport. This can prevent fall damage or suffocation from an estimate target position."
		));
		configMetaData.put("durationInSecondsDamageShouldBeDisabled", Arrays.asList(
			"How long in seconds damage should be disabled for after a teleport when disableFollowerDamageAfterTeleport is enabled."
		));
		configMetaData.put("followerSearchRadiusInBlocks", Arrays.asList(
			"How far around the player in blocks followers are taken along on a teleport."
		));
		configMetaData.put("followerTypesThatStayBehind", Arrays.asList(
			"A comma separated list of entity ids that never teleport with their owner, for example 'minecraft:nautilus,cobblemon:pokemon'."
		));
		configMetaData.put("followersFollowIntoOtherDimensions", Arrays.asList(
			"Whether followers also come along when a teleport takes the player into another dimension."
		));
		configMetaData.put("allaysTeleportToo", Arrays.asList(
			"Whether allays that like the player (after being given an item) teleport along as well."
		));
		configMetaData.put("mountTeleportsToo", Arrays.asList(
			"Whether the mount the player is riding teleports along, with the player put back on it."
		));

		DuskConfig.init(Reference.NAME, Reference.MOD_ID, ConfigHandler.class);
	}
}