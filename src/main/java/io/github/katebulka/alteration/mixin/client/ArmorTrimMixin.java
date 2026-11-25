package io.github.katebulka.alteration.mixin.client;

import io.github.katebulka.alteration.ModConfig;
import io.github.katebulka.alteration.ModHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

// https://github.com/Andrew6rant/Better-Trim-Tooltips
@Mixin(
		value = ArmorTrim.class
)
public abstract class ArmorTrimMixin {
	@Shadow
	public abstract Holder<TrimPattern> pattern();

	@Shadow
	public abstract Holder<TrimMaterial> material();

	@Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
	private void alteration$addToTooltip(Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag flag, CallbackInfo ci) {
		if (!ModConfig.INSTANCE.betterTrimTooltip) return;

		if (ModHelper.isShifting() && ModConfig.INSTANCE.tooltipCanExpand) {
			return;
		}

		Component name = ((TrimMaterial) this.material().value()).description();
		Style style = name.getStyle();

		tooltipAdder.accept(
				Component.literal("")
						.append(((TrimPattern)this.pattern().value()).description().copy().withStyle(ChatFormatting.GRAY))
						.append(CommonComponents.SPACE).append(Component.literal("(").withStyle(style))
						.append(name).append(Component.literal(")").withStyle(style))
		);

		ci.cancel();
	}
}
