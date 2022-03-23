package ladysnake.illuminations.client.gui;

import ladysnake.illuminations.client.config.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class AutoUpdateGreetingScreen extends Screen {
    private final Screen parent;

    public AutoUpdateGreetingScreen(Screen parent) {
        super(new TranslatableText("title.illuminations.autoUpdater"));
        this.parent = parent;
    }

    protected static int row(int index) {
        return 40 + index * 13;
    }

    protected void init() {
        this.addDrawableChild(new ButtonWidget(this.width / 2 - 125, this.height / 6 + 168, 100, 20, new TranslatableText("option.illuminations.disable").formatted(Formatting.RED), (button) -> {
            Config.setAutoUpdate(false);
            Config.save();
            this.client.setScreen(this.parent);
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 + 25, this.height / 6 + 168, 100, 20, new TranslatableText("option.illuminations.enable").formatted(Formatting.GREEN), (button) -> {
            Config.setAutoUpdate(true);
            Config.save();
            this.client.setScreen(this.parent);
        }));
    }

    public void removed() {
        Config.setDisplayGreetingScreen(false);
        Config.save();
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, row(1), 16777215);
        for (int i = 1; i <= 8; i++) {
            drawCenteredText(matrices, this.textRenderer, new TranslatableText("description.illuminations.autoUpdater" + i), this.width / 2, row(i + 2), 16777215);
        }
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        super.close();
        this.client.setScreen(this.parent);
    }
}
