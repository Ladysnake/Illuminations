package ladysnake.illuminations.common.items;

import ladysnake.illuminations.common.entities.FireflyEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FireflyItem extends Item {
    public FireflyItem(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        world.spawnEntity(new FireflyEntity(world, playerEntity.getX(), playerEntity.getY()+1, playerEntity.getZ()));
        if (!playerEntity.isCreative()) {
            playerEntity.getStackInHand(hand).decrement(1);
        }
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }
}
