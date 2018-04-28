package net.minecraft.tileentity;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityEndPortal extends TileEntity
{
    @SideOnly(Side.CLIENT)
    public boolean shouldRenderFace(EnumFacing p_184313_1_)
    {
        return p_184313_1_ == EnumFacing.UP;
    }
}