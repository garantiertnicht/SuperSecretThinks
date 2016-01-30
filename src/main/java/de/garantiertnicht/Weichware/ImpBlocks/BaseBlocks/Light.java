package de.garantiertnicht.Weichware.ImpBlocks.BaseBlocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.garantiertnicht.Weichware.ImpBlocks.ModImpBlocks;
import net.minecraft.block.BlockAir;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Random;

public class Light extends BlockAir {
    public Light() {
        super();
        setLightLevel(1);
        setBlockName("lightblock");
    }
}
