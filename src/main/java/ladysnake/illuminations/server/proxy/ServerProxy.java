package ladysnake.illuminations.server.proxy;

import ladysnake.illuminations.common.init.CommonProxy;
import net.fabricmc.loader.ModInfo;

public class ServerProxy extends CommonProxy {

    @Override
    public void preInit() {
        super.preInit();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }

    @Override
    public ModInfo.Side getSide() {
        return ModInfo.Side.SERVER;
    }

}
