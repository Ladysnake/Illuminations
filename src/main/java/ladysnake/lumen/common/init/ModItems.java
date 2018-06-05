package ladysnake.lumen.common.init;

import ladylib.registration.AutoRegister;
import ladysnake.lumen.common.Lumen;
import ladysnake.lumen.common.items.ItemCompanionSummoner;
import ladysnake.lumen.common.items.ItemGlassJar;
import net.minecraft.item.Item;

@AutoRegister(Lumen.MOD_ID)
public class ModItems {

    public static final ItemGlassJar GLASS_JAR = new ItemGlassJar();
    public static final ItemGlassJar FIREFLY_IN_A_JAR = new ItemGlassJar("firefly");
    public static final ItemGlassJar PSI_FIREFLY_IN_A_JAR = new ItemGlassJar("psi_firefly");
    public static final ItemGlassJar LIGHTNING_BUG_IN_A_JAR = new ItemGlassJar("lightning_bug");
    public static final ItemGlassJar EMBER_IN_A_JAR = new ItemGlassJar("ember");

    public static final Item AFFECTION_CORE = new Item();

    public static final ItemCompanionSummoner SCORCHING_FLINT = new ItemCompanionSummoner(ModEntities.Companion.SOLAR_ORB);
    public static final ItemCompanionSummoner SPARKLING_FLINT = new ItemCompanionSummoner(ModEntities.Companion.THUNDERBALL);
    public static final ItemCompanionSummoner RAT_HAMMOCK = new ItemCompanionSummoner(ModEntities.Companion.SPLINTER);

}
