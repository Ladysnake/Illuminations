package net.minecraft.client.util;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SearchTreeManager implements IResourceManagerReloadListener
{
    /** The item search tree, used for the creative inventory's search tab */
    public static final SearchTreeManager.Key<ItemStack> ITEMS = new SearchTreeManager.Key<ItemStack>();
    /** The recipe search tree, used for the recipe book */
    public static final SearchTreeManager.Key<RecipeList> RECIPES = new SearchTreeManager.Key<RecipeList>();
    private final Map < SearchTreeManager.Key<?>, SearchTree<? >> trees = Maps. < SearchTreeManager.Key<?>, SearchTree<? >> newHashMap();

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        for (SearchTree<?> searchtree : this.trees.values())
        {
            searchtree.recalculate();
        }
    }

    public <T> void register(SearchTreeManager.Key<T> key, SearchTree<T> searchTreeIn)
    {
        this.trees.put(key, searchTreeIn);
    }

    /**
     * Gets the {@link ISearchTree} for the given search tree key, returning null if no such tree exists.
     */
    public <T> ISearchTree<T> get(SearchTreeManager.Key<T> key)
    {
        return (ISearchTree)this.trees.get(key);
    }

    @SideOnly(Side.CLIENT)
    public static class Key<T>
        {
        }
}