package io.github.katebulka.alteration;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BooleanSupplier;

//? fabric {

//?} neoforge {
import net.neoforged.fml.loading.FMLLoader;
//?} forge {

//?}

public class AlterationMixinPlugin implements IMixinConfigPlugin {
	private static boolean isModLoaded(String modId) {
		//? if fabric {
		/*return FabricLoader.getInstance().isModLoaded(modId);
		 *///?} else {
		return FMLLoader.getLoadingModList().getModFileById(modId) != null;
		//?}
	}

	private static final Map<String, BooleanSupplier> APPLY_MAP = Map.of(
		"io.github.katebulka.alteration.mixin.sodium.SmoothLightPipelineMixin",     () -> isModLoaded("sodium"),
		"io.github.katebulka.alteration.mixin.supplementaries.CandleHolderMixin", 	() -> isModLoaded("supplementaries"),
		"io.github.katebulka.alteration.mixin.clayworks.DecoratedPotTrimMixin", 	() -> isModLoaded("clayworks")
	);

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		BooleanSupplier gate = APPLY_MAP.get(mixinClassName);
		return gate == null || gate.getAsBoolean();
	}

	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}
