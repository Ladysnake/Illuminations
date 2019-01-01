package ladysnake.illuminations.common;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.fabricmc.api.ModInitializer;

public class Illuminations implements ModInitializer {
    public static final String MOD_ID = "illuminations";

    @Override
    public void onInitialize() {
        IlluminationsEntities.init();

    }
}

