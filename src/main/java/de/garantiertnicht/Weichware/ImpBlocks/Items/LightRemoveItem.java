package de.garantiertnicht.Weichware.ImpBlocks.Items;

import de.garantiertnicht.Weichware.ImpBlocks.ModImpBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LightRemoveItem extends Item {
    public LightRemoveItem() {
        setCreativeTab(CreativeTabs.tabDecorations);
        setUnlocalizedName("light_off");
        setTextureName(String.format("%s:%s", ModImpBlocks.MODID, "light_off"));
    }

    //Right-Click will remove all Light Blocks in a 7x7x7 square.
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer ply, World world, int x, int y, int z, int side, float bx, float by, float bz) {

        double px = ply.posX;
        double py = ply.posY;
        double pz = ply.posZ;

        boolean removed = false;

        for(int i = (int) px - 7; i < px + 8; i++)
            for(int j = (int) py - 7; j < py + 8; j++)
                for(int k = (int) pz - 7; k < pz + 8; k++)
                    if(world.getBlock(i, j, k) == ModImpBlocks.light) {
                        world.setBlock(i, j, k, Blocks.air);
                        removed = true;
                    }

        //Remove block in Survival ONLY when at least one block was removed.
        if(!ply.capabilities.isCreativeMode && removed)
            stack.stackSize--;

        return true;
    }
}
