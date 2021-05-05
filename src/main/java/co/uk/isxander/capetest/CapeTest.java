package co.uk.isxander.capetest;

import club.sk1er.mods.core.ModCoreInstaller;
import co.uk.isxander.xanderlib.XanderLib;
import co.uk.isxander.xanderlib.utils.Constants;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = CapeTest.MOD_ID, name = CapeTest.MOD_NAME, version = CapeTest.MOD_VERSION, clientSideOnly = true)
public class CapeTest implements Constants {

    public static final String MOD_NAME = "isXander's Cape Test";
    public static final String MOD_ID = "capetest";
    public static final String MOD_VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(mc.mcDataDir);
        XanderLib.getInstance().initPhase();


    }

}
