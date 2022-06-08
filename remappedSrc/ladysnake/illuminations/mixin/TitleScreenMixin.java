package ladysnake.illuminations.mixin;

import ladysnake.illuminations.client.Illuminations;
import ladysnake.illuminations.client.config.Config;
import ladysnake.illuminations.client.data.PlayerCosmeticData;
import ladysnake.illuminations.client.gui.DonateToast;
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
        PlayerCosmeticData playerCosmeticData = Illuminations.getCosmeticData(MinecraftClient.getInstance().getSession().getProfile().getId());
        if (Config.shouldDisplayDonationToast() && (playerCosmeticData == null || playerCosmeticData.getAura() == null || playerCosmeticData.getOverhead() == null || playerCosmeticData.getPet() == null)) {
            DonateToast.add();
        }
    }
}
