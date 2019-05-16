package ladysnake.illuminations.common.items;

import ladysnake.illuminations.common.Illuminations;
import ladysnake.illuminations.common.entities.FireflyEntity;
import ladysnake.illuminations.common.init.IlluminationsItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

import static net.minecraft.sound.SoundEvents.BLOCK_CONDUIT_ACTIVATE;

public class BugNetItem extends Item {
    public BugNetItem(Settings settings) {
        super(settings);
    }

    @Override
    public boolean onEntityDamaged(ItemStack bugNet, LivingEntity targetEntity, LivingEntity player) {
        if (targetEntity instanceof FireflyEntity) {
            // stealth kill
            targetEntity.x = player.x;
            targetEntity.y = 500;
            targetEntity.z = player.z;
            targetEntity.kill();

            if (player instanceof PlayerEntity) {
                player.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);
                ((PlayerEntity) player).inventory.insertStack(new ItemStack(IlluminationsItems.FIREFLY));
            }
        }

        return false;
    }
}
