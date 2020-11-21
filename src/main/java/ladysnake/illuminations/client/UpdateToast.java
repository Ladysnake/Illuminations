package ladysnake.illuminations.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class UpdateToast implements Toast {
    @Override
    public Visibility draw(MatrixStack matrices, ToastManager manager, long startTime) {
        manager.getGame().getTextureManager().bindTexture(new Identifier("illuminations", "textures/gui/update_toast.png"));
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        manager.drawTexture(matrices, 0, 0, 0, 0, getWidth(), getHeight());
        manager.getGame().textRenderer.draw(matrices, new LiteralText("Illuminations update available!"), 32, 7, -256);
        manager.getGame().textRenderer.draw(matrices, new LiteralText("Update automatically downloaded"), 32, 18, -1);
        manager.getGame().textRenderer.draw(matrices, new LiteralText("Restart your game to install it"), 32, 29, -1);
        return MinecraftClient.getInstance().currentScreen instanceof TitleScreen ? Toast.Visibility.SHOW : Toast.Visibility.HIDE;
    }

    @Override
    public Object getType() {
        return TYPE;
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getHeight() {
        return 43;
    }

    public static void add() {
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        UpdateToast toast = toastManager.getToast(UpdateToast.class, Toast.TYPE);
        if (toast == null) {
            toastManager.add(new UpdateToast());
        }
    }
}
