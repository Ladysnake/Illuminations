package net.minecraft.item.crafting;

import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import javax.annotation.Nullable;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ingredient implements Predicate<ItemStack>
{
    //Because Mojang caches things... we need to invalidate them.. so... here we go..
    private static final java.util.Set<Ingredient> INSTANCES = java.util.Collections.newSetFromMap(new java.util.WeakHashMap<Ingredient, Boolean>());
    public static final Ingredient EMPTY = new Ingredient(new ItemStack[0])
    {
        public boolean apply(@Nullable ItemStack p_apply_1_)
        {
            return p_apply_1_.isEmpty();
        }
    };
    private final ItemStack[] matchingStacks;
    private final ItemStack[] matchingStacksExploded;
    private IntList matchingStacksPacked;
    private final boolean isSimple;

    protected Ingredient(int size)
    {
        this(new ItemStack[size]);
    }

    protected Ingredient(ItemStack... p_i47503_1_)
    {
        boolean simple = true;
        this.matchingStacks = p_i47503_1_;
        net.minecraft.util.NonNullList<ItemStack> lst = net.minecraft.util.NonNullList.create();
        for (ItemStack s : p_i47503_1_)
        {
            if (s.isEmpty())
                continue;
            if (s.getItem().isDamageable())
                simple = false;
            if (s.getMetadata() == net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE)
                s.getItem().getSubItems(net.minecraft.creativetab.CreativeTabs.SEARCH, lst);
            else
                lst.add(s);
        }
        this.matchingStacksExploded = lst.toArray(new ItemStack[lst.size()]);
        this.isSimple = simple && this.matchingStacksExploded.length > 0;
        Ingredient.INSTANCES.add(this);
    }

    public ItemStack[] getMatchingStacks()
    {
        return this.matchingStacksExploded;
    }

    public boolean apply(@Nullable ItemStack p_apply_1_)
    {
        if (p_apply_1_ == null)
        {
            return false;
        }
        else
        {
            for (ItemStack itemstack : this.matchingStacks)
            {
                if (itemstack.getItem() == p_apply_1_.getItem())
                {
                    int i = itemstack.getMetadata();

                    if (i == 32767 || i == p_apply_1_.getMetadata())
                    {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public IntList getValidItemStacksPacked()
    {
        if (this.matchingStacksPacked == null)
        {
            this.matchingStacksPacked = new IntArrayList(this.matchingStacksExploded.length);

            for (ItemStack itemstack : this.matchingStacksExploded)
            {
                this.matchingStacksPacked.add(RecipeItemHelper.pack(itemstack));
            }

            this.matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.matchingStacksPacked;
    }

    public static void invalidateAll()
    {
        for (Ingredient ing : INSTANCES)
            if (ing != null)
                ing.invalidate();
    }

    protected void invalidate()
    {
        this.matchingStacksPacked = null;
    }

    public static Ingredient fromItem(Item p_193367_0_)
    {
        return fromStacks(new ItemStack(p_193367_0_, 1, 32767));
    }

    public static Ingredient fromItems(Item... items)
    {
        ItemStack[] aitemstack = new ItemStack[items.length];

        for (int i = 0; i < items.length; ++i)
        {
            aitemstack[i] = new ItemStack(items[i]);
        }

        return fromStacks(aitemstack);
    }

    public static Ingredient fromStacks(ItemStack... stacks)
    {
        if (stacks.length > 0)
        {
            for (ItemStack itemstack : stacks)
            {
                if (!itemstack.isEmpty())
                {
                    return new Ingredient(stacks);
                }
            }
        }

        return EMPTY;
    }

    // Merges several vanilla Ingredients together. As a qwerk of how the json is structured, we can't tell if its a single Ingredient type or multiple so we split per item and remerge here.
    //Only public for internal use, so we can access a private field in here.
    public static Ingredient merge(java.util.Collection<Ingredient> parts)
    {
        net.minecraft.util.NonNullList<ItemStack> lst = net.minecraft.util.NonNullList.create();
        for (Ingredient part : parts)
        {
            for (ItemStack stack : part.matchingStacks)
                lst.add(stack);
        }
        return new Ingredient(lst.toArray(new ItemStack[lst.size()]));
    }

    public boolean isSimple()
    {
        return isSimple || this == EMPTY;
    }
}