package ladysnake.illuminations.client.data;

import com.google.gson.JsonElement;

public class PlayerCosmeticData {
    private String overhead;
    private String aura;
    private boolean drip;
    private final int colorRed;
    private final int colorGreen;
    private final int colorBlue;

    public PlayerCosmeticData(JsonElement aura, JsonElement color, JsonElement overhead, JsonElement drip) {
        if (aura.isJsonNull()) {
            this.aura = null;
        } else {
            this.aura = aura.getAsString();
        }
        if (color.isJsonNull()) {
            this.colorRed = 0;
            this.colorGreen = 0;
            this.colorBlue = 0;
        } else {
            this.colorRed = Integer.valueOf(color.getAsString().substring(1, 3), 16);
            this.colorBlue = Integer.valueOf(color.getAsString().substring(3, 5), 16);
            this.colorGreen = Integer.valueOf(color.getAsString().substring(5), 16);
        }
        if (overhead.isJsonNull()) {
            this.overhead = null;
        } else {
            this.overhead = overhead.getAsString();
        }
        if (drip.isJsonNull()) {
            this.drip = false;
        } else {
            this.drip = drip.getAsBoolean();
        }
    }

    public String getAura() {
        return aura;
    }

    public int getColorRed() {
        return colorRed;
    }

    public int getColorBlue() {
        return colorBlue;
    }

    public int getColorGreen() {
        return colorGreen;
    }

    public boolean isDrip() {
        return drip;
    }

    public String getOverhead() {
        return overhead;
    }
}
