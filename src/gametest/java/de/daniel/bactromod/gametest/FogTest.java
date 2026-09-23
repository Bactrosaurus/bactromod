package de.daniel.bactromod.gametest;

import de.daniel.bactromod.config.Config;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.world.level.material.FogType;

@SuppressWarnings("UnstableApiUsage")
public class FogTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        try (var world = context.worldBuilder().create()) {
            world.getConnection().waitForChunksRender();
            boolean saved = context.computeOnClient(client -> Config.get().atmosphericFog);
            try {
                context.runOnClient(client -> Config.get().atmosphericFog = true);
                context.waitFor(client -> client.gameRenderer.gameRenderState().levelRenderState
                        .cameraRenderState.fogData.renderDistanceEnd < Float.MAX_VALUE);

                context.runOnClient(client -> Config.get().atmosphericFog = false);
                context.waitFor(client -> client.gameRenderer.gameRenderState().levelRenderState
                        .cameraRenderState.fogData.renderDistanceEnd == Float.MAX_VALUE);
                context.runOnClient(client -> {
                    var camera = client.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
                    if (camera.fogType != FogType.NONE) throw new AssertionError("Expected atmospheric camera fog");
                    if (camera.fogData.environmentalEnd != Float.MAX_VALUE
                            || camera.fogData.renderDistanceStart != Float.MAX_VALUE) {
                        throw new AssertionError("Disabling atmospheric fog must clear fog distances");
                    }
                    if (camera.fogData.skyEnd == Float.MAX_VALUE || camera.fogData.cloudEnd == Float.MAX_VALUE) {
                        throw new AssertionError("Sky and clouds must retain their distance limits");
                    }
                });

                context.runOnClient(client -> Config.get().atmosphericFog = true);
                context.waitFor(client -> client.gameRenderer.gameRenderState().levelRenderState
                        .cameraRenderState.fogData.renderDistanceEnd < Float.MAX_VALUE);
            } finally {
                context.runOnClient(client -> Config.get().atmosphericFog = saved);
            }
        }
    }
}
