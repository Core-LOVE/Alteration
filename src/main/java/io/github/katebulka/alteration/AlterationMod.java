package io.github.katebulka.alteration;

import io.github.katebulka.alteration.platform.Platform;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? fabric {
//import io.github.katebulka.alteration.platform.fabric.FabricPlatform;
//?} neoforge {
import io.github.katebulka.alteration.platform.neoforge.NeoforgePlatform;
 //?} forge {
//import io.github.katebulka.alteration.platform.forge.ForgePlatform;
 //?}

@SuppressWarnings("LoggingSimilarMessage")
public class AlterationMod {

	public static final String MOD_ID = /*$ mod_id*/"alteration";
	public static final String MOD_VERSION = /*$ mod_version*/"0.1.0";
	public static final String MOD_FRIENDLY_NAME = /*$ mod_name*/"Alteration";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Platform PLATFORM = createPlatformInstance();

	public static void onInitialize() {
		ModConfig.load();

		LOGGER.info("Initializing {} on {}", MOD_ID, AlterationMod.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	public static void onInitializeClient() {
		LOGGER.info("Initializing {} Client on {}", MOD_ID, AlterationMod.xplat().loader());
		LOGGER.debug("{}: { version: {}; friendly_name: {} }", MOD_ID, MOD_VERSION, MOD_FRIENDLY_NAME);
	}

	public static Platform xplat() {
		return PLATFORM;
	}

	private static Platform createPlatformInstance() {
		//? fabric {
		//return new FabricPlatform();
		//?} neoforge {
		return new NeoforgePlatform();
		 //?} forge {
		//return new ForgePlatform();
		 //?}
	}

	public static ResourceLocation id(String path) {
		//? > 1.19.2 {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
		 //?} <= 1.19.2 {
		//return new ResourceLocation(MOD_ID, path);
		//?}
	}

	public static ResourceLocation id(String namespace, String path) {
		//? > 1.19.2 {
		return ResourceLocation.fromNamespaceAndPath(namespace, path);
		 //?} <= 1.19.2 {
		//return new ResourceLocation(namespace, path);
		//?}
	}
}
