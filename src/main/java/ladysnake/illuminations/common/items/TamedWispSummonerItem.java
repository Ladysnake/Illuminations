package ladysnake.illuminations.common.items;

import ladysnake.illuminations.common.entities.TamedWispEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class TamedWispSummonerItem extends Item {
    public String wispType = "tamed_wisp";

    public TamedWispSummonerItem(Settings settings, String wispType) {
        super(settings);
        this.wispType = wispType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        TamedWispEntity wisp = new TamedWispEntity(world, playerEntity.x, playerEntity.y, playerEntity.z);
        wisp.setWispType(this.wispType);
        world.spawnEntity(wisp);

        return new TypedActionResult(ActionResult.PASS, playerEntity.getStackInHand(hand));
    }
}
