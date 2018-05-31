package ladysnake.lightorbs.common.init;

import ladysnake.lightorbs.client.renders.entities.*;
import ladysnake.lightorbs.common.LightOrbs;
import ladysnake.lightorbs.common.entities.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = LightOrbs.MOD_ID)
public class ModEntities {

    public enum Companion {
        SOLAR_ORB ("solar_orb", 243, 126, 74, 8),
        THUNDERBALL ("thunderball", 204, 234, 255, 8);

        // properties
        private String name;
        private int lightingR;
        private int lightingG;
        private int lightingB;
        private int lightingRadius;

        // constructor
        Companion(String name, int lightingR, int lightingG, int lightingB, int lightingRadius){
            this.name = name;
            this.lightingR = lightingR;
            this.lightingG = lightingG;
            this.lightingB = lightingB;
            this.lightingRadius = lightingRadius;
        }

        // getters
        public String getName() {
            return name;
        }

        public int getLightingR() {
            return lightingR;
        }

        public int getLightingG() {
            return lightingG;
        }

        public int getLightingB() {
            return lightingB;
        }

        public int getLightingRadius() {
            return lightingRadius;
        }
    }

    private static int id = 0;

    @SubscribeEvent
    public static void register(RegistryEvent.Register<EntityEntry> event) {
        IForgeRegistry<EntityEntry> reg = event.getRegistry();

        // FIREFLIES
        reg.register(createEntry(EntityFirefly::new, "firefly", 64, true)
                .spawn(EnumCreatureType.AMBIENT, 50, 1, 5, BiomeDictionary.getBiomes(BiomeDictionary.Type.WET))
                .spawn(EnumCreatureType.AMBIENT, 50, 1, 5, BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST))
                .build());

        reg.register(createEntry(EntityPsiFirefly::new, "psi_firefly", 64, true)
                .spawn(EnumCreatureType.CREATURE, 50, 1, 1, BiomeDictionary.getBiomes(BiomeDictionary.Type.MAGICAL))
                .build());

        reg.register(createEntry(EntityLightningBug::new, "lightning_bug", 64, true)
                .spawn(EnumCreatureType.AMBIENT, 50, 1, 5, BiomeDictionary.getBiomes(BiomeDictionary.Type.WET))
                .spawn(EnumCreatureType.AMBIENT, 50, 1, 5, BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST))
                .build());

        reg.register(createEntry(EntityEmber::new, "ember", 64, true)
                .spawn(EnumCreatureType.AMBIENT, 50, 1, 5, BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER))
                .build());

        // COMPANION ORBS
        reg.register(createEntry(EntityCompanionOrb::new, "companion_orb", 64, true).build());

        // EXPERIMENTAL
        reg.register(createEntry(EntityWillOWisp::new, "will_o_wisp", 64, true).build());
    }

    private static EntityEntryBuilder<Entity> createEntry(Function<World, Entity> entityFactory,
                                                          String name, int trackingRange, boolean sendsVelocityUpdates) {
        return EntityEntryBuilder.create()
                .entity(entityFactory.apply(null).getClass())
                .factory(entityFactory)
                .id(new ResourceLocation(LightOrbs.MOD_ID, name), id++)
                .name(name)
                .tracker(trackingRange, 1, sendsVelocityUpdates);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRenders() {
        // FIREFLIES
        RenderingRegistry.registerEntityRenderingHandler(EntityFirefly.class, RenderFirefly::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityPsiFirefly.class, RenderPsiFirefly::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityLightningBug.class, RenderLightningBug::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityEmber.class, RenderEmber::new);

        // COMPANION ORBS
        RenderingRegistry.registerEntityRenderingHandler(EntityCompanionOrb.class, RenderCompanionOrb::new);

        // EXPERIMENTAL
        RenderingRegistry.registerEntityRenderingHandler(EntityWillOWisp.class, RenderWillOWisp::new);
    }

}
