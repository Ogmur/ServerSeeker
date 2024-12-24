package de.damcraft.serverseeker.mixin;

import de.damcraft.serverseeker.gui.InstallMeteorScreen;
import meteordevelopment.meteorclient.utils.render.prompts.YesNoPrompt;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Duration;
import java.time.LocalDate;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Unique private static boolean firstLoad = true;

    @Inject(at = @At("HEAD"), method = "init()V", cancellable = true)
    private void init(CallbackInfo info) {
        // Check if meteor-client is installed
        if (!FabricLoader.getInstance().isModLoaded("meteor-client")) {
            info.cancel();
            MinecraftClient.getInstance().setScreen(new InstallMeteorScreen());
        }
    }
}
