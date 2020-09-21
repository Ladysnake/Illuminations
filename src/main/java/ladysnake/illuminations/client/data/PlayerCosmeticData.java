package ladysnake.illuminations.client.data;

public class PlayerCosmeticData {
    private final String aura;
    private final String color;
    private final String overhead;

    public PlayerCosmeticData(String aura, String color, String overhead) {
        this.aura = aura;
        this.color = color;
        this.overhead = overhead;
    }

    public String getAura() {
        return aura;
    }

    public String getColor() {
        return color;
    }

    public String getOverhead() {
        return overhead;
    }
}
