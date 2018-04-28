package net.minecraft.client.renderer.block.statemap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class StateMap extends StateMapperBase
{
    private final IProperty<?> name;
    private final String suffix;
    private final List < IProperty<? >> ignored;

    private StateMap(@Nullable IProperty<?> name, @Nullable String suffix, List < IProperty<? >> ignored)
    {
        this.name = name;
        this.suffix = suffix;
        this.ignored = ignored;
    }

    protected ModelResourceLocation getModelResourceLocation(IBlockState state)
    {
        Map < IProperty<?>, Comparable<? >> map = Maps. < IProperty<?>, Comparable<? >> newLinkedHashMap(state.getProperties());
        String s;

        if (this.name == null)
        {
            s = ((ResourceLocation)Block.REGISTRY.getNameForObject(state.getBlock())).toString();
        }
        else
        {
            s = String.format("%s:%s", Block.REGISTRY.getNameForObject(state.getBlock()).getResourceDomain(), this.removeName(this.name, map));
        }

        if (this.suffix != null)
        {
            s = s + this.suffix;
        }

        for (IProperty<?> iproperty : this.ignored)
        {
            map.remove(iproperty);
        }

        return new ModelResourceLocation(s, this.getPropertyString(map));
    }

    private <T extends Comparable<T>> String removeName(IProperty<T> property, Map < IProperty<?>, Comparable<? >> values)
    {
        return property.getName((T)values.remove(this.name));
    }

    @SideOnly(Side.CLIENT)
    public static class Builder
        {
            private IProperty<?> name;
            private String suffix;
            private final List < IProperty<? >> ignored = Lists. < IProperty<? >> newArrayList();

            public StateMap.Builder withName(IProperty<?> builderPropertyIn)
            {
                this.name = builderPropertyIn;
                return this;
            }

            public StateMap.Builder withSuffix(String builderSuffixIn)
            {
                this.suffix = builderSuffixIn;
                return this;
            }

            /**
             * Ignore the listed {@code IProperty}s when building the variant string for the final {@code
             * ModelResourceLocation}. It is valid to pass a {@code Block} that does not have one of these {@code
             * IProperty}s to the built {@code StateMap}.
             * @return {@code this}, for convenience in chaining
             *  
             * @param ignores the {@code IProperty}s to ignore when building a variant string
             */
            public StateMap.Builder ignore(IProperty<?>... ignores)
            {
                Collections.addAll(this.ignored, ignores);
                return this;
            }

            /**
             * Build a new {@code StateMap} with the settings contained in this {@code StateMap.Builder}. The {@code
             * StateMap} will work with any {@code Block} that has the required {@code IProperty}s.
             * @return a new {@code StateMap} with the settings contained in this {@code StateMap.Builder}
             * @see #ignore(IProperty...)
             * @see #withName(IProperty)
             * @see #withSuffix(String)
             */
            public StateMap build()
            {
                return new StateMap(this.name, this.suffix, this.ignored);
            }
        }
}