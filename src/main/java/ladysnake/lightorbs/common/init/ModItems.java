package ladysnake.lightorbs.common.init;

import ladylib.registration.AutoRegister;
import ladysnake.lightorbs.common.Reference;
import ladysnake.lightorbs.common.items.ItemGlassJar;
import ladysnake.lightorbs.common.items.ItemSolarOrb;

@AutoRegister(Reference.MOD_ID)
public class ModItems {

    public static final ItemGlassJar GLASS_JAR = new ItemGlassJar();
    public static final ItemGlassJar FIREFLY_IN_A_JAR = new ItemGlassJar("firefly");
    public static final ItemGlassJar PSI_FIREFLY_IN_A_JAR = new ItemGlassJar("psi_firefly");
    public static final ItemGlassJar LIGHTNING_BUG_IN_A_JAR = new ItemGlassJar("lightning_bug");

    // Test
    public static final ItemSolarOrb SOLAR_ORB = new ItemSolarOrb("solar_orb", true);
    public static final ItemSolarOrb EMPTY_SOLAR_ORB = new ItemSolarOrb("solar_orb", false);

}
