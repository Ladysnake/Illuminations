package ladysnake.illuminations.client.data;

public class PlayerAuraData {
    private final String aura;
    private final String color;

    public PlayerAuraData(String aura, String color) {
        this.aura = aura;
        this.color = color;
    }

    public String getAura() {
        return aura;
    }

    public String getColor() {
        return color;
    }
}
