package ladysnake.lightorbs.common;

import ladylib.LadyLib;
import ladysnake.lightorbs.common.init.CommonProxy;
import ladysnake.lightorbs.common.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = LightOrbs.MOD_ID, name = LightOrbs.MOD_NAME, version = LightOrbs.MOD_VERSION, acceptedMinecraftVersions = LightOrbs.MCVERSION)
public class LightOrbs {
    // References
    public static final String MOD_ID = "lightorbs";
    public static final String MOD_NAME = "LightOrbs";
    static final String MOD_VERSION = "0.3.1";
    static final String MCVERSION = "[1.12]";

    static final String CLIENT_PROXY_CLASS = "ladysnake.lightorbs.client.proxy.ClientProxy";
    static final String SERVER_PROXY_CLASS = "ladysnake.lightorbs.server.proxy.ServerProxy";

    public static final Logger LOGGER = LogManager.getLogger("LightOrbs");

    @SidedProxy(clientSide = LightOrbs.CLIENT_PROXY_CLASS, serverSide = LightOrbs.SERVER_PROXY_CLASS)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit();
        LadyLib lib = LadyLib.initLib(event);
        lib.makeCreativeTab(() -> new ItemStack(ModItems.FIREFLY_IN_A_JAR));
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        // some example code
    }

}
