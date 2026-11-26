package io.github.katebulka.alteration.platform.fabric;

//? fabric {

/*import io.github.katebulka.alteration.AlterationMod;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;

@Entrypoint("client")
public class FabricClientEntrypoint implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(
			new IdentifiableResourceReloadListener() {
				@Override
				public ResourceLocation getFabricId() {

				}

				@Override
				public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller prepProfiler, ProfilerFiller reloadProfiler, Executor bgExecutor, Executor gameExecutor) {

				}
			}
		);

		AlterationMod.onInitializeClient();
	}

}
*///?}
