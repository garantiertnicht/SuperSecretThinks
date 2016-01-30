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

        URL website = new URL("http://127.0.0.1/blocky/version");
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

        String uPath = Minecraft.getMinecraft().mcDataDir + File.separator + "mods" + File.separator + "impblock-update.zip";

        Path updateP = fs.getPath(uPath);
        website = new URL("http://127.0.0.1/blocky/assets.zip");
        rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(uPath);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        ZipFile main = new ZipFile(modJar);
        ZipFile update = new ZipFile(uPath);
        ZipOutputStream newZip = new ZipOutputStream(new FileOutputStream(modJar + "_temp"));

        Enumeration<? extends ZipEntry> entries = main.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = entries.nextElement();

            if(e.getName().startsWith("assets"))
                continue;

            newZip.putNextEntry(e);
            if (!e.isDirectory()) {
                copy(main.getInputStream(e), newZip);
            }
            newZip.closeEntry();
        }

        entries = update.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = entries.nextElement();
            if(!e.getName().startsWith("assets")) {
                continue;
            }
            newZip.putNextEntry(e);
            if (!e.isDirectory()) {
                copy(update.getInputStream(e), newZip);
            }
            newZip.closeEntry();
        }

        ZipEntry e = new ZipEntry("assets/impblock/version");
        newZip.putNextEntry(e);

        newZip.write(v1.getBytes());
        newZip.closeEntry();

        // close
        main.close();
        newZip.close();
        update.close();

        Path old = fs.getPath(modJar);
        Path temp = fs.getPath(modJar + "_temp");

        Files.move(temp, old, StandardCopyOption.REPLACE_EXISTING);
        Files.deleteIfExists(updateP);

        ModImpBlocks.log.info("Textures Updated, please restart.");
        JOptionPane.showMessageDialog(null, "Textures were Updated. Please start Minecraft again!");
        new FMLCommonHandler().exitJava(0, false);
    }
}
