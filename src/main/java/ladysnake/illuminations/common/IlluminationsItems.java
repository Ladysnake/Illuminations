package ladysnake.illuminations.common;

import ladysnake.illuminations.common.entities.BugBallEntity;
import ladysnake.illuminations.common.items.BugBallItem;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Position;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class IlluminationsItems {
    public static Item BUGBALL;

    public static void registerItems() {
        BUGBALL = registerItem(new BugBallItem(new Item.Settings().group(ItemGroup.MISC)), "bugball");
    }

    public static Item registerItem(Item item, String name) {
        if (item instanceof BugBallItem) {
            registerItem(item, name, true);
        } else {
            registerItem(item, name, false);
        }

        return item;
    }

    public static Item registerItem(Item item, String name, boolean registerDispenserBehavior) {
        Registry.register(Registry.ITEM, Illuminations.MODID + ":" + name, item);
        if (registerDispenserBehavior) {
            DispenserBlock.registerBehavior(item, new ProjectileDispenserBehavior() {
                @Override
                protected ProjectileEntity createProjectile(World world, Position position, ItemStack itemStack) {
                    BugBallEntity bombEntity = Illuminations.BUGBALL.create(world);
                    bombEntity.setPos(position.getX(), position.getY(), position.getZ());
                    itemStack.decrement(1);
                    return bombEntity;
                }
            });
        }

        return item;
    }

}
