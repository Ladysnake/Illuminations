package ladysnake.illuminations.common.items;

import ladysnake.illuminations.common.entities.FireflyEntity;
import ladysnake.illuminations.common.init.IlluminationsItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class BugNetItem extends Item {
    public BugNetItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean postHit(ItemStack bugNet, LivingEntity targetEntity, LivingEntity player) {
        if (targetEntity instanceof FireflyEntity) {
            targetEntity.remove();
            if (player instanceof PlayerEntity) {
//                player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
                bugNet.damage(1, player.getRandom(), (ServerPlayerEntity) player);
                ((PlayerEntity) player).inventory.insertStack(new ItemStack(IlluminationsItems.FIREFLY));
            }
        }

        return true;
    }
}
