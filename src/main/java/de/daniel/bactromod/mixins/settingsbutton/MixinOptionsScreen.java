package de.daniel.bactromod.mixins.settingsbutton;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.daniel.bactromod.config.ConfigScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.GuiPipeline;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(OptionsScreen.class)
public abstract class MixinOptionsScreen extends Screen {
    @Unique
    private static final Identifier BACTROMOD_ICON = Identifier.fromNamespaceAndPath("bactromod", "icon.png");

    @Unique
    private LayoutElement bactromod$doneButton;

    @Unique
    private Button bactromod$settingsButton;

    protected MixinOptionsScreen(Component title) {
        super(title);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/HeaderAndFooterLayout;addToFooter(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;"))
    private LayoutElement captureDoneButton(HeaderAndFooterLayout instance, LayoutElement child, Operation<LayoutElement> original) {
        this.bactromod$doneButton = original.call(instance, child);
        return this.bactromod$doneButton;
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addSettingsButton(CallbackInfo ci) {
        Component title = Component.translatable("bactromod.options.title");
        this.bactromod$settingsButton = new Button(0, 0, 20, 20, title, _ -> this.minecraft.gui.setScreen(new ConfigScreen(this)), Supplier::get) {
            @Override
            protected void extractContents(GuiGraphicsExtractor graphics) {
                this.extractDefaultSprite(graphics);
                graphics.blit(GuiPipeline.TEXTURED, BACTROMOD_ICON, this.getX() + 2, this.getY() + 2, 0, 0, 16, 16, 16, 16);
            }
        };
        this.bactromod$settingsButton.setTooltip(Tooltip.create(title));
        this.addRenderableWidget(this.bactromod$settingsButton);
        this.bactromod$positionSettingsButton();
    }

    @Inject(method = "repositionElements", at = @At("TAIL"))
    private void positionSettingsButton(CallbackInfo ci) {
        this.bactromod$positionSettingsButton();
    }

    @Unique
    private void bactromod$positionSettingsButton() {
        if (this.bactromod$doneButton != null && this.bactromod$settingsButton != null) {
            this.bactromod$settingsButton.setPosition(
                    this.bactromod$doneButton.getX() + this.bactromod$doneButton.getWidth() + 4,
                    this.bactromod$doneButton.getY()
            );
        }
    }
}
