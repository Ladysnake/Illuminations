package net.minecraft.client.util;

import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface ISearchTree<T>
{
    /**
     * Searches this search tree for the given text.
     * <p>
     * If the query does not contain a <code>:</code>, then only {@link #byName} is searched; if it does contain a
     * colon, both {@link #byName} and {@link #byId} are searched and the results are merged using a {@link
     * MergingIterator}.
     * @return A list of all matching items in this search tree.
     *  
     * @param searchText The text to search for. Must be normalized with <code>toLowerCase(Locale.ROOT)</code> before
     * calling this method.
     */
    List<T> search(String searchText);
}