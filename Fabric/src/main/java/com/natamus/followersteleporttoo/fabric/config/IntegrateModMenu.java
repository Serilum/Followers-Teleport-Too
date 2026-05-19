package com.natamus.followersteleporttoo.fabric.config;

import com.natamus.followersteleporttoo.util.Reference;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

import java.lang.reflect.Method;

public class IntegrateModMenu implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> {
			try {
				Class<?> duskConfigScreenClass = Class.forName("com.natamus.collective.config.DuskConfig$DuskConfigScreen");
				Method getScreenMethod = duskConfigScreenClass.getMethod("getScreen", Screen.class, String.class);
				return (Screen)getScreenMethod.invoke(null, parent, Reference.MOD_ID);
			}
			catch (ReflectiveOperationException | LinkageError ignored) {
				return parent;
			}
		};
	}
}
