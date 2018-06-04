package ladysnake.lumen.common;

import ladylib.LadyLib;
import ladysnake.lumen.common.init.CommonProxy;
import ladysnake.lumen.common.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Lumen.MOD_ID, name = Lumen.MOD_NAME, version = Lumen.MOD_VERSION, acceptedMinecraftVersions = Lumen.MCVERSION)
public class Lumen {
    // References
    public static final String MOD_ID = "lumen";
    public static final String MOD_NAME = "Lumen";
    static final String MOD_VERSION = "0.4.0";
    static final String MCVERSION = "[1.12]";

    static final String CLIENT_PROXY_CLASS = "ladysnake.lumen.client.proxy.ClientProxy";
    static final String SERVER_PROXY_CLASS = "ladysnake.lumen.server.proxy.ServerProxy";

    public static final Logger LOGGER = LogManager.getLogger("Lumen");

    @SidedProxy(clientSide = Lumen.CLIENT_PROXY_CLASS, serverSide = Lumen.SERVER_PROXY_CLASS)
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
