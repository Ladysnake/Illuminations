package net.minecraft.client.tutorial;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CompletedTutorialStep implements ITutorialStep
{
    private final Tutorial tutorial;

    public CompletedTutorialStep(Tutorial tutorial)
    {
        this.tutorial = tutorial;
    }
}