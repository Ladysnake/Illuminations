package ladysnake.lightorbs.server.proxy;

import ladysnake.lightorbs.common.init.CommonProxy;
import net.minecraftforge.fml.relauncher.Side;

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
    public Side getSide() {
        return Side.SERVER;
    }

}
