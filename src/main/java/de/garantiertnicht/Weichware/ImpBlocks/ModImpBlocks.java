package de.garantiertnicht.Weichware.ImpBlocks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import de.garantiertnicht.Weichware.ImpBlocks.BaseBlocks.Cube;
import de.garantiertnicht.Weichware.ImpBlocks.BaseBlocks.Light;
import de.garantiertnicht.Weichware.ImpBlocks.Items.LightItem;
import de.garantiertnicht.Weichware.ImpBlocks.Items.LightRemoveItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Mod(modid = ModImpBlocks.MODID, version = ModImpBlocks.VERSION, name = ModImpBlocks.NAME)
public class ModImpBlocks
{
    public static final String MODID = "impblock";
    public static final String VERSION = "0.1";
    public static final String NAME = "Imperium 1871 Blocks";

    private String modJar;

    public static Block light;
    public static LightItem lightOn;
    public static LightRemoveItem lightOff;

    public static org.apache.logging.log4j.Logger log;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) throws IOException {
        log = e.getModLog();
        modJar = Minecraft.getMinecraft().mcDataDir + File.separator + "mods" + File.separator + "ImpBlocks.jar";

        patchJar();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        Properties test = new Properties();
        test.setProperty("light", "1.0F");
        test.setProperty("sound", "soundTypeAnvil");
        Cube a = new Cube("Iluminati", test);


        GameRegistry.registerBlock(a, "Iluminati");

        light = new Light();
        GameRegistry.registerBlock(light, "light");

        lightOn = new LightItem();
        lightOff = new LightRemoveItem();

        GameRegistry.registerItem(lightOn, "lightOn");
        GameRegistry.registerItem(lightOff, "lightOff");

        //Glowstone in the middle and the edges, Glass in the free slots
        GameRegistry.addShapedRecipe(new ItemStack(lightOn, 2), "OgO", "gOg", "OgO", 'O', Blocks.glowstone, 'g', Blocks.glass);

        //Glowstine in the middle, sorounded by Cleanstone
        GameRegistry.addShapedRecipe(new ItemStack(lightOff, 1), "SSS", "SOS", "SSS", 'S', Blocks.stone, 'O', Blocks.glowstone);
    }

    /*

            HILFSFUNKTIONEN

     */

    public void copy(InputStream input, OutputStream output) throws IOException {
        int bytesRead;
        final byte[] BUFFER = new byte[4096 * 1024];
        while ((bytesRead = input.read(BUFFER))!= -1) {
            output.write(BUFFER, 0, bytesRead);
        }
    }

    public void patchJar() throws IOException {
        FileSystem fs = FileSystems.getDefault();

        URL website = new URL("http://blocks.imperium1871.de/version");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());

        Scanner scan = new Scanner(rbc);
        String v1 = scan.nextLine();


        InputStream is = this.getClass().getResourceAsStream("/assets/impblock/version");
        scan = new Scanner(is);
        String v2 = scan.nextLine();

        if(v1.equals(v2) || v1.equals("0")) {
            ModImpBlocks.log.info(String.format("Client is up-to-secound; Version is %s and could %s", v2, v1));
            return;
        }

        ModImpBlocks.log.info(String.format("Running Client update; Version is %s and could %s", v2, v1));
        String jvm_location;
        if (System.getProperty("os.name").startsWith("Win")) {
            jvm_location = System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe";
        } else {
            jvm_location = System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        }

        log.info(Minecraft.getMinecraft().mcDataDir.getPath());
        Runtime.getRuntime().exec(new String[] {jvm_location, "-jar", Minecraft.getMinecraft().mcDataDir + File.separator + "AssetsLoader.jar", Minecraft.getMinecraft().mcDataDir.getPath(), "http://blocks.imperium1871.de/assets.zip", "ImpBlocks", "impblock", v1, String.valueOf(true)});
        new FMLCommonHandler().exitJava(0, false);
    }
}
