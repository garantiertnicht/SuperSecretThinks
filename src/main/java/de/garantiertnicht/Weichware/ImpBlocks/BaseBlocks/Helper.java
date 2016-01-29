package de.garantiertnicht.Weichware.ImpBlocks.BaseBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

import java.lang.reflect.Field;

public class Helper {
    static Material getMaterial(String s) {
        Class<Material> materialClass = Material.class;
        for(Field field : materialClass.getDeclaredFields()) {
            if(field.getName().equals(s))
                try {
                    return (Material) field.get(new Material(MapColor.stoneColor));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }

        return Material.rock;
    }

    static Block.SoundType getSoundType(String s) {
        Class<Block> blockClass = Block.class;
        for(Field field : blockClass.getDeclaredFields()) {
            if(field.getName().equals(s))
                try {
                    return (Block.SoundType) field.get(new Block.SoundType("none", 0.0F, 0.0F));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
        }

        return Block.soundTypeStone;
    }
}
