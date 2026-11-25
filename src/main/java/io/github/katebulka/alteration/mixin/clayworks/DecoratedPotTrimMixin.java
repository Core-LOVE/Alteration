package io.github.katebulka.alteration.mixin.clayworks;

import com.teamabnormals.clayworks.common.DecoratedPotTrim;
import com.teamabnormals.clayworks.common.DecoratedPotTrimPattern;
import com.teamabnormals.clayworks.core.Clayworks;
import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.ModHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(
		value = DecoratedPotTrim.class,
		remap = false
)
public abstract class DecoratedPotTrimMixin implements IMixinConfigPlugin {
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return ModHelper.isModLoaded("clayworks");
	}

	@Inject(method = "addToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Ljava/util/function/Consumer;Lnet/minecraft/world/item/TooltipFlag;)V", at = @At("HEAD"), cancellable = true)
	private void alteration$addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag, CallbackInfo ci) {
		if (!ModConfig.INSTANCE.betterTrimTooltip) return;

		if (ModHelper.isShifting() && ModConfig.INSTANCE.tooltipCanExpand) {
			return;
		}

		DecoratedPotTrim self = (DecoratedPotTrim) (Object) this;

		if (!self.showInTooltip()) {
			return;
		}

		Component name = ((TrimMaterial) self.material().value()).description();
		Style style = name.getStyle();

		tooltipAdder.accept(
				Component.literal("")
						.append(((DecoratedPotTrimPattern)self.pattern().value()).description().copy().withStyle(ChatFormatting.GRAY))
						.append(CommonComponents.SPACE).append(Component.literal("(").withStyle(style))
						.append(name).append(Component.literal(")").withStyle(style))
		);

		ci.cancel();
	}
}
