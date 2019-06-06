package ladysnake.illuminations.common.entities;

import ladysnake.illuminations.common.init.IlluminationsEntities;
import ladysnake.illuminations.common.init.IlluminationsItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class ThrownPouchEntity extends ThrownItemEntity {
    public ThrownPouchEntity(EntityType entityType, World world) {
        super(entityType, world);
    }

    public ThrownPouchEntity(LivingEntity livingEntity_1, World world_1) {
        super(IlluminationsEntities.THROWN_POUCH, livingEntity_1, world_1);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        if (!this.world.isClient) {
            for (int i = 0; i < 7; i++) {
                FireflyEntity firefly = new BloodbeetleEntity(this.world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z);
                this.world.spawnEntity(firefly);
            }
            this.world.spawnEntity(new ItemEntity(this.world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, new ItemStack(Items.LEATHER)));
            this.remove();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return IlluminationsItems.FIREFLY_POUCH;
    }
}
