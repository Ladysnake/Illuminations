package ladysnake.illuminations.client.data;

public class PlayerCosmeticData {
    private final String aura;
    private final int colorRed;
    private final int colorBlue;
    private final int colorGreen;
    private final String overhead;
    private final boolean drip;

    public PlayerCosmeticData(String aura, String color, String overhead, boolean drip) {
        this.aura = aura;
        this.colorRed = Integer.valueOf(color.substring(1, 3), 16);
        this.colorBlue = Integer.valueOf(color.substring(3, 5), 16);
        this.colorGreen = Integer.valueOf(color.substring(5), 16);
        this.overhead = overhead;
        this.drip = drip;
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
