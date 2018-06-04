package ladysnake.lightorbs.common.config;

import ladysnake.lightorbs.common.LightOrbs;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = LightOrbs.MOD_ID)
@Mod.EventBusSubscriber
public class LightOrbsConfig {

    // Spawns
    @Config.Comment("Enable firefly spawn")
    public static boolean spawnFireflies = true;

    @Config.Comment("Enable psi firefly spawn")
    public static boolean spawnPsiFireflies = true;

    @Config.Comment("Enable lightning bug spawn")
    public static boolean spawnLightningBugs = true;

    @Config.Comment("Enable ember spawn")
    public static boolean spawnEmbers = true;

    @Config.Comment("Enable will o' wisp spawn")
    public static boolean spawnWillOWisps = true;

    @Config.Comment("Enable faerie spawn")
    public static boolean spawnFaeries = true;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(LightOrbs.MOD_ID)) {
            ConfigManager.sync(LightOrbs.MOD_ID, Config.Type.INSTANCE);
        }
    }
}