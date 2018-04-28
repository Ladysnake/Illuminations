package net.minecraft.client.gui.advancements;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public enum AdvancementState
{
    OBTAINED(0),
    UNOBTAINED(1);

    private final int id;

    private AdvancementState(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
}