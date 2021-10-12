package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.gui.AutoUpdateGreetingScreen;
import ladysnake.illuminations.client.gui.UpdateToast;
import ladysnake.illuminations.updater.IlluminationsUpdater;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(at = @At(value = "RETURN"), method = "render")
    protected void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Config.isDisplayGreetingScreen()) {
            MinecraftClient.getInstance().setScreen(new AutoUpdateGreetingScreen((TitleScreen) (Object) this));
        } else {
            if (IlluminationsUpdater.NEW_UPDATE) {
                UpdateToast.add();
            }
        }
    }
}
