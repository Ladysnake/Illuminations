package ladysnake.illuminations.common.items;

import ladysnake.illuminations.common.entities.EntityWillOWispThrown;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemWillOWisp extends Item {

    public ItemWillOWisp(Settings settings) {
        super(settings);
    }

    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        ItemStack itemStack_1 = playerEntity_1.getStackInHand(hand_1);
        if (!playerEntity_1.abilities.creativeMode) {
            itemStack_1.subtractAmount(1);
        }

        world_1.playSound((PlayerEntity)null, playerEntity_1.x, playerEntity_1.y, playerEntity_1.z, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world_1.isClient) {
            EntityWillOWispThrown wispThrown = new EntityWillOWispThrown(world_1, playerEntity_1);
            wispThrown.calculateVelocity(playerEntity_1, playerEntity_1.pitch, playerEntity_1.yaw, 0.0F, 1.5F, 1.0F);
            world_1.spawnEntity(wispThrown);
        }

        playerEntity_1.incrementStat(Stats.USED.method_14956(this));
        return new TypedActionResult(ActionResult.SUCCESS, itemStack_1);
    }

}
