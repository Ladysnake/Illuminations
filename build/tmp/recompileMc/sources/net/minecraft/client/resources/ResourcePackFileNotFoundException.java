package net.minecraft.client.resources;

import java.io.File;
import java.io.FileNotFoundException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourcePackFileNotFoundException extends FileNotFoundException
{
    public ResourcePackFileNotFoundException(File resourcePack, String fileName)
    {
        super(String.format("'%s' in ResourcePack '%s'", fileName, resourcePack));
    }
}