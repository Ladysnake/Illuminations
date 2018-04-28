package net.minecraft.client.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SearchTree<T> implements ISearchTree<T>
{
    /** A {@link SuffixArray} that contains values keyed by the ID (as produced by {@link #idFunc}). */
    protected SuffixArray<T> byId = new SuffixArray<T>();
    /** A {@link SuffixArray} that contains values keyed by the name (as produced by {@link #nameFunc}). */
    protected SuffixArray<T> byName = new SuffixArray<T>();
    /**
     * A function that takes a <code>T</code> and returns a list of Strings describing it normally.
     * <p>
     * For both items and recipes, this is implemented via {@link net.minecraft.item.ItemStack#getTooltip()
     * ItemStack.getTooltip} (with NORMAL tooltip flags), with formatting codes stripped, text trimmed, and empty lines
     * removed.
     *  
     * The result does not need to be corrected for
     */
    private final Function<T, Iterable<String>> nameFunc;
    /**
     * A function that takes a <code>T</code> and returns a list of {@link ResourceLocation}s describing it.
     * <p>
     * For both items and recipes, this is implemented via <code>Item.REGISTRY.getNameForObject</code>. (In the case of
     * registries, it is applied to all results)
     */
    private final Function<T, Iterable<ResourceLocation>> idFunc;
    /** All entries in the search tree. */
    private final List<T> contents = Lists.<T>newArrayList();
    /** Maps each entry in the search tree to a locally unique, increasing number (staring at 0). */
    private Object2IntMap<T> numericContents = new Object2IntOpenHashMap<T>();

    public SearchTree(Function<T, Iterable<String>> nameFuncIn, Function<T, Iterable<ResourceLocation>> idFuncIn)
    {
        this.nameFunc = nameFuncIn;
        this.idFunc = idFuncIn;
    }

    /**
     * Recalculates the contents of this search tree, reapplying {@link #nameFunc} and {@link #idFunc}. Should be called
     * whenever resources are reloaded (e.g. language changes).
     */
    public void recalculate()
    {
        this.byId = new SuffixArray<T>();
        this.byName = new SuffixArray<T>();

        for (T t : this.contents)
        {
            this.index(t);
        }

        this.byId.generate();
        this.byName.generate();
    }

    /**
     * Adds the given item to the search tree.
     *  
     * @param element The element to add
     */
    public void add(T element)
    {
        this.numericContents.put(element, this.contents.size());
        this.contents.add(element);
        this.index(element);
    }

    /**
     * Directly puts the given item into {@link #byId} and {@link #byName}, applying {@link #nameFunc} and {@link
     * idFunc}.
     *  
     * @param element The element to add
     */
    private void index(T element)
    {
        (this.idFunc.apply(element)).forEach((p_194039_2_) ->
        {
            this.byName.add(element, p_194039_2_.toString().toLowerCase(Locale.ROOT));
        });
        (this.nameFunc.apply(element)).forEach((p_194041_2_) ->
        {
            this.byId.add(element, p_194041_2_.toLowerCase(Locale.ROOT));
        });
    }

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
    public List<T> search(String searchText)
    {
        List<T> list = this.byId.search(searchText);

        if (searchText.indexOf(58) < 0)
        {
            return list;
        }
        else
        {
            List<T> list1 = this.byName.search(searchText);
            return (List<T>)(list1.isEmpty() ? list : Lists.newArrayList(new SearchTree.MergingIterator(list.iterator(), list1.iterator(), this.numericContents)));
        }
    }

    @SideOnly(Side.CLIENT)
    static class MergingIterator<T> extends AbstractIterator<T>
        {
            private final Iterator<T> leftItr;
            private final Iterator<T> rightItr;
            /**
             * A mapping of objects to unique numeric IDs, used to sort the list.
             * <p>
             * Since there's no good place to document how this class works, it basically just interleaves two iterators
             * together, choosing the entry that has the lower numeric ID in this map.
             */
            private final Object2IntMap<T> numbers;
            /** Current element from {@link #leftItr} */
            private T left;
            /** Current element from {@link #rightItr} */
            private T right;

            public MergingIterator(Iterator<T> leftIn, Iterator<T> rightIn, Object2IntMap<T> numbersIn)
            {
                this.leftItr = leftIn;
                this.rightItr = rightIn;
                this.numbers = numbersIn;
                this.left = (T)(leftIn.hasNext() ? leftIn.next() : null);
                this.right = (T)(rightIn.hasNext() ? rightIn.next() : null);
            }

            protected T computeNext()
            {
                if (this.left == null && this.right == null)
                {
                    return (T)this.endOfData();
                }
                else
                {
                    int i;

                    if (this.left == this.right)
                    {
                        i = 0;
                    }
                    else if (this.left == null)
                    {
                        i = 1;
                    }
                    else if (this.right == null)
                    {
                        i = -1;
                    }
                    else
                    {
                        i = Integer.compare(this.numbers.getInt(this.left), this.numbers.getInt(this.right));
                    }

                    T t = (T)(i <= 0 ? this.left : this.right);

                    if (i <= 0)
                    {
                        this.left = (T)(this.leftItr.hasNext() ? this.leftItr.next() : null);
                    }

                    if (i >= 0)
                    {
                        this.right = (T)(this.rightItr.hasNext() ? this.rightItr.next() : null);
                    }

                    return t;
                }
            }
        }
}