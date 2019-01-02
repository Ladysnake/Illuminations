package ladysnake.illuminations.client;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class IlluminationsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        IlluminationsEntities.registerRenders();
    }

}
