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

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION, acceptedMinecraftVersions = Reference.MCVERSION)
public class LightOrbs {

    public static final Logger LOGGER = LogManager.getLogger("LightOrbs");

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
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
