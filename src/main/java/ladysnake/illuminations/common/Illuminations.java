package ladysnake.illuminations.common;

import ladysnake.illuminations.common.init.IlluminationsBlocks;
import ladysnake.illuminations.common.init.IlluminationsEntities;
import ladysnake.illuminations.common.init.IlluminationsItems;
import ladysnake.illuminations.common.init.IlluminationsWorldFeatures;
import net.fabricmc.api.ModInitializer;

public class Illuminations implements ModInitializer {
    public static final String MOD_ID = "illuminations";

    @Override
    public void onInitialize() {
        IlluminationsItems.init();
//        IlluminationsBlocks.init();
//        IlluminationsEntities.init();
//        IlluminationsWorldFeatures.init();
    }
}

