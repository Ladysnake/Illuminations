package ladysnake.illuminations.common.items;

import ladysnake.illuminations.client.IlluminationsClient;
import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.init.IlluminationsBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.World;

import java.util.Random;

public class GlowMealItem extends Item {
    public GlowMealItem(Item.Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.addParticle(IlluminationsClient.FIREFLY, user.getX(), user.getY(), user.getZ(), 0, 0, 0);
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

}
