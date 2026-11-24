plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
}

stonecutter {
	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("neoforge") {
			forgeLikeVersionRange.set("[${prop("deps.neoforge")},)")
		}
	}
}

neoForge {
	version = prop("deps.neoforge")
	accessTransformers.from(rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg"))
	validateAccessTransformers = true

	if (sc.properties.contains("deps.parchment")) parchment {
		val (mc, ver) = prop("deps.parchment").split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		val devJvmArgs = propsList("mod", "dev_jvm_args") +
			propsList("mod", "dev_jvm_args_mixin_debug")
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.current.version})"
			programArgument("--username=Dev")
			jvmArguments.addAll(devJvmArgs)
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${stonecutter.current.version})"
			jvmArguments.addAll(devJvmArgs)
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
	sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${sc.current.version}/src/main/generated")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }

	maven {
		name = "CaffeineMC"
		url = uri("https://maven.caffeinemc.net/releases") // or /snapshots
	}

	maven("https://api.modrinth.com/maven")
	maven("https://cursemaven.com")

	maven("https://maven.isxander.dev/releases") {
		name = "Xander Maven"
	}
}

dependencies {
	compileOnly("net.fabricmc:sponge-mixin:${property("mixin_version")}")
	compileOnly("io.github.llamalad7:mixinextras-common:${property("mixin_extras_version")}")
	annotationProcessor("io.github.llamalad7:mixinextras-common:${property("mixin_extras_version")}")
	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)

	compileOnly("net.caffeinemc:sodium-neoforge-mod:${property("sodium_version")}")
	compileOnly("net.caffeinemc:sodium-neoforge-api:${property("sodium_version")}")
	compileOnly("net.caffeinemc:sodium-neoforge:${property("sodium_version")}")

	compileOnly("dev.isxander:yet-another-config-lib:${property("yacl_version")}")
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
