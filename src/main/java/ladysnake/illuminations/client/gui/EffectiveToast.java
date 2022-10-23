package ladysnake.illuminations.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.illuminations.client.Illuminations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class EffectiveToast implements Toast {
    private static final Identifier TEXTURE = new Identifier(Illuminations.MODID, "textures/gui/donate_toast.png");

    public static void add() {
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        EffectiveToast toast = toastManager.getToast(EffectiveToast.class, Toast.TYPE);
        if (toast == null) {
            toastManager.add(new EffectiveToast());
        }
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        DrawableHelper.drawTexture(matrices, 0, 0, getWidth(), getHeight(), 0, 0, getWidth(), getHeight(), 512, 256);
        manager.getClient().textRenderer.draw(matrices, Text.literal("Illuminations is merging with Effective!"), 35, 7, 0xFF005A);
        manager.getClient().textRenderer.draw(matrices, Text.literal("Illuminations will soon be discontinued, download Effective at:"), 35, 18, -1);
        manager.getClient().textRenderer.draw(matrices, Text.literal("https://www.curseforge.com/minecraft/mc-mods/effective").copy().fillStyle(Style.EMPTY.withColor(Formatting.BLUE)), 35, 29, -1);
        return MinecraftClient.getInstance().currentScreen instanceof TitleScreen ? Visibility.SHOW : Visibility.HIDE;
    }

    @Override
    public Object getType() {
        return TYPE;
    }

    @Override
    public int getWidth() {
        return 350;
    }

    @Override
    public int getHeight() {
        return 43;
    }
}
