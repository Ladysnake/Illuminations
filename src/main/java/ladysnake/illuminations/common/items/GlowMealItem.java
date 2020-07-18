package ladysnake.illuminations.common.items;

import ladysnake.illuminations.client.IlluminationsClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class GlowMealItem extends Item {
    public GlowMealItem(Item.Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        Random rand = new Random();

        for (int i = 0; i < 1; i++) {
            world.addParticle(IlluminationsClient.FIREFLY, user.getX(), user.getY()+rand.nextFloat(), user.getZ(), 0, 0, 0);
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

}
