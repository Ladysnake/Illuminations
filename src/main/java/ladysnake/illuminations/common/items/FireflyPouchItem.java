package ladysnake.illuminations.common.items;

import ladysnake.illuminations.common.entities.ThrownPouchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireflyPouchItem extends Item {

    public FireflyPouchItem(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack itemStack_1 = playerEntity.getStackInHand(hand);
        if (!playerEntity.abilities.creativeMode) {
            itemStack_1.subtractAmount(1);
        }

        world.playSound((PlayerEntity)null, playerEntity.x, playerEntity.y, playerEntity.z, SoundEvents.ENTITY_SPLASH_POTION_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClient) {
            ThrownPouchEntity thrownPouchEntity = new ThrownPouchEntity(playerEntity, world);
            thrownPouchEntity.setItem(itemStack_1);
            thrownPouchEntity.method_19207(playerEntity, playerEntity.pitch, playerEntity.yaw, 0.0F, 1.5F, 1.0F);
            world.spawnEntity(thrownPouchEntity);
        }

        return new TypedActionResult(ActionResult.SUCCESS, itemStack_1);
    }

}
