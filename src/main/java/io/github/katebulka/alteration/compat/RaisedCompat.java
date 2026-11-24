package io.github.katebulka.alteration.compat;

import dev.yurisuika.raised.api.RaisedApi;
import io.github.katebulka.alteration.AlterationMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class RaisedCompat {
	public static void initialize() {
		RaisedApi.register(AlterationMod.id("oxygen_bar"));
	}

	public static void translate(GuiGraphics guiGraphics, String path) {
		ResourceLocation id = AlterationMod.id(path);

		int x = RaisedApi.getX(id);
		int y = RaisedApi.getY(id);

		guiGraphics.pose().translate(
				(float) x, (float) y, 0.0F
		);
	}
}
