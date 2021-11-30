package ladysnake.illuminations.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.illuminations.client.Illuminations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class UpdateToast implements Toast {
    private static final Identifier TEXTURE = new Identifier(Illuminations.MODID, "textures/gui/update_toast.png");

    public static void add() {
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        UpdateToast toast = toastManager.getToast(UpdateToast.class, Toast.TYPE);
        if (toast == null) {
            toastManager.add(new UpdateToast());
        }
    }

    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 0, getWidth(), getHeight());
        manager.getClient().textRenderer.draw(matrices, new LiteralText("Illuminations update available!"), 34, 7, -256);
        manager.getClient().textRenderer.draw(matrices, new LiteralText("Illuminations automatically downloaded it"), 34, 18, -1);
        manager.getClient().textRenderer.draw(matrices, new LiteralText("Restart your game to finish installing"), 34, 29, -1);
        return MinecraftClient.getInstance().currentScreen instanceof TitleScreen ? Visibility.SHOW : Visibility.HIDE;
    }

    @Override
    public Object getType() {
        return TYPE;
    }

    @Override
    public int getWidth() {
        return 240;
    }

    @Override
    public int getHeight() {
        return 43;
    }
}
