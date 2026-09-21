package de.daniel.bactromod.gametest;

import de.daniel.bactromod.config.Config;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
import net.minecraft.client.renderer.LightmapRenderStateExtractor;
import net.minecraft.client.renderer.state.LightmapRenderState;
import org.joml.Vector3f;

@SuppressWarnings("UnstableApiUsage")
public class FullbrightTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            world.getConnection().waitForChunksRender();
            context.setScreen(() -> new CreditsAndAttributionScreen(null));
            context.clickScreenButton("bactromod.options.title");
            context.waitFor(client -> client.isPaused());
            context.runOnClient(client -> {
                var config = Config.get();
                int savedMultiplier = config.gammaMultiplier;
                double vanillaGamma = client.options.gamma().get();
                // Prime the real, Mixin-transformed extractor once, then never tick it again:
                // while paused, changing the mod option must invalidate it on its own.
                var extractor = new LightmapRenderStateExtractor(client.gameRenderer, client);
                var state = new LightmapRenderState();
                try {
                    config.gammaMultiplier = 1;
                    extractor.tick();
                    extractor.extract(state, 1);
                    check(state.needsUpdate, "Initial extraction must update");
                    var vanillaAmbient = new Vector3f(state.ambientColor);
                    check(vanillaAmbient.distance(LightmapRenderStateExtractor.WHITE) > 0.01F,
                            "Test world must have ambient lighting below fullbright");
                    extractor.extract(state, 1);
                    check(!state.needsUpdate, "Unchanged brightness must not force another update");

                    config.gammaMultiplier = 8;
                    extractor.extract(state, 1);
                    check(state.needsUpdate, "Changing only mod brightness must refresh a paused lightmap");
                    float middleDistance = state.ambientColor.distance(LightmapRenderStateExtractor.WHITE);
                    check(middleDistance > 0.01F && middleDistance < vanillaAmbient.distance(LightmapRenderStateExtractor.WHITE),
                            "Intermediate brightness must brighten without saturating");

                    config.gammaMultiplier = 15;
                    extractor.extract(state, 1);
                    check(state.needsUpdate, "Fullbright must request an update");
                    check(state.ambientColor.equals(LightmapRenderStateExtractor.WHITE, 0.0001F),
                            "Maximum brightness must reach white");
                    extractor.extract(state, 1);
                    check(!state.needsUpdate, "Fullbright must not rebuild every frame");

                    config.gammaMultiplier = Integer.MAX_VALUE;
                    extractor.extract(state, 1);
                    check(!state.needsUpdate, "Clamping to the same brightness must not trigger a refresh");
                    config.gammaMultiplier = Integer.MIN_VALUE;
                    extractor.extract(state, 1);
                    check(state.needsUpdate, "Returning to vanilla must request an update");
                    check(state.ambientColor.equals(vanillaAmbient, 0.0001F),
                            "Returning to vanilla must restore the original ambient color");
                    check(client.options.gamma().get() == vanillaGamma, "Vanilla brightness must stay unchanged");
                } finally {
                    config.gammaMultiplier = savedMultiplier;
                }
            });
            context.setScreen(() -> null);
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
