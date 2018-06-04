package ladysnake.lumen.server.proxy;

import ladysnake.lumen.common.init.CommonProxy;
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
