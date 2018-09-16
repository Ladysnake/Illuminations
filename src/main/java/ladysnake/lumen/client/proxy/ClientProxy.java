package ladysnake.lumen.client.proxy;

import ladylib.client.lighting.AttachedCheapLight;
import ladylib.client.lighting.CheapLightManager;
import ladylib.client.lighting.MutableCheapLight;
import ladylib.compat.EnhancedBusSubscriber;
import ladysnake.lumen.common.entities.AbstractLightOrb;
import ladysnake.lumen.common.init.CommonProxy;
import ladysnake.lumen.common.init.ModEntities;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EnhancedBusSubscriber(side = Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private int particleCount = 0;

    @Override
    public void preInit() {
        super.preInit();
        ModEntities.registerRenders();
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
        return Side.CLIENT;
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AbstractLightOrb) {
            MutableCheapLight light = ((AbstractLightOrb) event.getEntity()).createLight();
            if (light != null) {
                CheapLightManager.INSTANCE.addLight(new AttachedCheapLight(light, event.getEntity()));
            }
        }
    }
}
