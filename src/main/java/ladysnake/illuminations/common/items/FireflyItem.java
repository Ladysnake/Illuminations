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
        world.spawnEntity(new FireflyEntity(world, playerEntity.x, playerEntity.y+1, playerEntity.z));
        if (!playerEntity.isCreative()) {
            playerEntity.getStackInHand(hand).subtractAmount(1);
        }
        return new TypedActionResult(ActionResult.SUCCESS, playerEntity.getStackInHand(hand));
    }
}
