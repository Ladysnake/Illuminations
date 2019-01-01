package ladysnake.illuminations.common.init;

import net.fabricmc.loader.ModInfo;

public abstract class CommonProxy {

    public void preInit() {
    }

    public void init() {
    }

    public void postInit() {
    }

    public abstract ModInfo.Side getSide();

}
