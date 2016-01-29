package de.garantiertnicht.Weichware.ImpBlocks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import de.garantiertnicht.Weichware.ImpBlocks.BaseBlocks.Cube;
import net.minecraft.client.Minecraft;

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
        Cube a = new Cube("iluminati", test);
        GameRegistry.registerBlock(a, "iluminati");
    }
    public static void copy(InputStream input, OutputStream output) throws IOException {
        int bytesRead;
        final byte[] BUFFER = new byte[4096 * 1024];
        while ((bytesRead = input.read(BUFFER))!= -1) {
            output.write(BUFFER, 0, bytesRead);
        }
    }

    public void patchJar() throws IOException {
        FileSystem fs = FileSystems.getDefault();

        Path nf = fs.getPath("assets" + File.separator + "impblock" + File.separator + "textures" + File.separator + "blocks" + File.separator + "iluminati.png");
        URL website = new URL("http://127.0.0.1/Iluminati.png");
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(Minecraft.getMinecraft().mcDataDir + File.separator + "mods" + File.separator + "iluminati.png");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        ZipFile war = new ZipFile(modJar);
        ZipOutputStream append = new ZipOutputStream(new FileOutputStream(modJar + "_temp"));

        List avil = new ArrayList<String>();

        Enumeration<? extends ZipEntry> entries = war.entries();
        while (entries.hasMoreElements()) {
            ZipEntry e = entries.nextElement();

            if(e.getName().startsWith("assets" + File.separator + "impblock" + File.separator + "textures" + File.separator + "blocks" + File.separator));
                avil.add(e.getName());

            append.putNextEntry(e);
            if (!e.isDirectory()) {
                copy(war.getInputStream(e), append);
            }
            append.closeEntry();
        }

        boolean flag = true;

        for(int i = 0; i < avil.size(); i++) {
            if(((String)avil.get(i)).equals("assets" + File.separator + "impblock" + File.separator + "textures" + File.separator + "blocks" + File.separator + "iluminati.png")) {
                flag = false;
                break;
            }
        }

        Path png = fs.getPath(Minecraft.getMinecraft().mcDataDir + File.separator + "mods" + File.separator + "iluminati.png");

        if(flag) {
            ZipEntry e = new ZipEntry("assets" + File.separator + "impblock" + File.separator + "textures" + File.separator + "blocks" + File.separator + "iluminati.png");

            append.putNextEntry(e);

            append.write(Files.readAllBytes(png));
            append.closeEntry();
        }

        // close
        war.close();
        append.close();

        Path old = fs.getPath(modJar);
        Path temp = fs.getPath(modJar + "_temp");

        Files.move(temp, old, StandardCopyOption.REPLACE_EXISTING);
        Files.deleteIfExists(png);

        if(flag) {
            ModImpBlocks.log.info("Textures Updated, please restart.");
            JOptionPane.showMessageDialog(null, "Textures were Updated. Please start Minecraft again!");
            new FMLCommonHandler().exitJava(0, false);
        }
    }
}
