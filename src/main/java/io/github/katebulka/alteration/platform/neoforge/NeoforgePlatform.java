package io.github.katebulka.alteration.platform.neoforge;

//? neoforge {

import io.github.katebulka.alteration.AlterationMod;
import io.github.katebulka.alteration.platform.Platform;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLLoader;

//@EventBusSubscriber(modid = AlterationMod.MOD_ID)
public class NeoforgePlatform implements Platform {
//	@SubscribeEvent
//	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
//		InteractionResultHolder<ItemStack> result = ModChainmailHelper.tryAttachOrDetach(event.getEntity(), event.getItemStack());
//
//		if (result.getResult().consumesAction()) {
//			event.setCanceled(true);
//			event.setCancellationResult(result.getResult());
//		}
//	}
//
//	@SubscribeEvent
//	public static void onIncomingDamage(LivingIncomingDamageEvent event) {
//		Holder<Attribute> holder = ModAttributes.getImpactResistanceHolder();
//
//		if (holder == null) {
//			return;
//		}
//
//		LivingEntity victim = event.getEntity();
//		AttributeInstance attrInstance = victim.getAttribute(holder);
//
//		if (attrInstance == null) {
//			return;
//		}
//
//		double resistance = attrInstance.getValue();
//
//		if (resistance <= 0.0) {
//			return;
//		}
//
//		DamageSource source = event.getSource();
//
//		if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.GENERIC)) {
//			float val = (float) Math.max(event.getAmount() - resistance, 1.0F);
//			event.setAmount(val);
//
//			if (ModConfig.INSTANCE.chainmailedSounds) {
//				SoundEvent sound = SoundEvents.CHAIN_PLACE;
//				victim.playSound(sound);
//
//				if (victim instanceof Player) {
//					victim.level().playSound(null, victim, sound, SoundSource.PLAYERS, 1.5F, 1.0F);
//				}
//			}
//		}
//	}

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public ModLoader loader() {
		return ModLoader.NEOFORGE;
	}

	//~ if >1.21.7 'FMLLoader.' -> 'FMLLoader.getCurrent().' {
	@Override
	public String mcVersion() {
		//~ if >1.21.7 'versionInfo()' -> 'getVersionInfo()'
		return FMLLoader.versionInfo().mcVersion();
	}

	@Override
	public boolean isDevelopmentEnvironment() {
		return !FMLLoader.isProduction();
	}
	//~}
}
//?}
