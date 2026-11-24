package io.github.katebulka.alteration.platform.neoforge;

//? if neoforge {
import io.github.katebulka.alteration.AlterationMod;
import io.github.katebulka.alteration.ModConfigScreen;
import io.github.katebulka.alteration.ModHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = AlterationMod.MOD_ID, dist = Dist.CLIENT)
public class NeoforgeClientEntrypoint {
	public NeoforgeClientEntrypoint(ModContainer container) {
		if (ModHelper.isModLoaded("yet_another_config_lib_v3")) {
			container.registerExtensionPoint(IConfigScreenFactory.class,
					(c, parent) -> ModConfigScreen.createScreen(parent));
		}
	}
}
//?}
