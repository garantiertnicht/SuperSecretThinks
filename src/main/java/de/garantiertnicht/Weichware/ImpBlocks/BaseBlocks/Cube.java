package de.garantiertnicht.Weichware.ImpBlocks.BaseBlocks;

import de.garantiertnicht.Weichware.ImpBlocks.ModImpBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;

import java.util.Properties;

public class Cube extends Block {
    public Cube(String name) {
        super(Material.rock);
        setStepSound(Block.soundTypeStone);
        setBlockName(name);
        setCreativeTab(CreativeTabs.tabBlock);
        setBlockTextureName(String.format("%s:%s", ModImpBlocks.MODID, name));
    }

    public Cube(String name, Properties props) {
        super(Helper.getMaterial(props.getProperty("material", "rock")));
        setStepSound(Helper.getSoundType(props.getProperty("sound", "soundTypeStone")));
        setBlockName(name);
        setBlockTextureName(String.format("%s:%s", ModImpBlocks.MODID, name));
        setLightLevel(Float.valueOf(props.getProperty("light", "0.0F")));
        ModImpBlocks.log.info(String.format("Light level of %s set to %f", name, Float.valueOf(props.getProperty("light", "0.0F"))));
        setCreativeTab(CreativeTabs.tabBlock);
    }

}
