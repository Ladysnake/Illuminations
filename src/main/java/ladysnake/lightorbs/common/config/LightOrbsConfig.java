package ladysnake.lightorbs.common.config;

import ladysnake.lightorbs.common.Reference;
import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID)
public class LightOrbsConfig {

    // Spawns
    public static boolean spawnFireflies = true;
    public static int fireflySwarmMinSize = 10;
    public static int fireflySwarmMaxSize = 20;

    public static boolean spawnPsiFireflies = true;
    public static int psiFireflySwarmMinSize = 10;
    public static int psiFireflySwarmMaxSize = 20;

    public static boolean spawnLightningBugs = true;
    public static int lightningBugSwarmMinSize = 10;
    public static int lightningBugSwarmMaxSize = 20;

    public static boolean spawnEmbers = true;
    public static int emberSwarmMinSize = 10;
    public static int emberSwarmMaxSize = 20;

}