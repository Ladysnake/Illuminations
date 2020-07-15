package ladysnake.illuminations.common.items;

import ladysnake.illuminations.client.IlluminationsClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GlowMealItem extends Item {
    public GlowMealItem(Item.Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        for (int i = 0; i < 500; i++) {
            world.addParticle(IlluminationsClient.FIREFLY, user.getX(), user.getY(), user.getZ(), 0, 0, 0);
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

}
