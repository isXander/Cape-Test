package co.uk.isxander.capetest;

import club.sk1er.mods.core.ModCoreInstaller;
import co.uk.isxander.capetest.auth.PlayerCache;
import co.uk.isxander.capetest.auth.PlayerData;
import co.uk.isxander.capetest.command.Command;
import co.uk.isxander.capetest.config.CapeTestConfig;
import co.uk.isxander.capetest.listener.PlayerListener;
import co.uk.isxander.xanderlib.XanderLib;
import co.uk.isxander.xanderlib.utils.Constants;
import co.uk.isxander.xanderlib.utils.HttpsUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;
import java.util.UUID;

@Mod(modid = CapeTest.MOD_ID, name = CapeTest.MOD_NAME, version = CapeTest.MOD_VERSION, clientSideOnly = true)
public class CapeTest implements Constants {

    public static final String MOD_NAME = "isXander's Cape Test";
    public static final String MOD_ID = "capetest";
    public static final String MOD_VERSION = "1.0";

    public static final File DATA_DIR = new File(mc.mcDataDir, "/config/isxandercape");
    public static final String REPO_URL = "https://raw.githubusercontent.com/isXander/Cape-Test/repo/";

    @Mod.Instance(MOD_ID)
    private static CapeTest instance;

    private CapeTestConfig config;
    private PlayerCache playerCache;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModCoreInstaller.initializeModCore(mc.mcDataDir);
        XanderLib.getInstance().initPhase();

        if (!DATA_DIR.exists())
            DATA_DIR.mkdirs();

        config = new CapeTestConfig(new File(DATA_DIR, "/config.toml"));
        config.preload();

        playerCache = new PlayerCache();
        playerCache.saveCache();

        ClientCommandHandler.instance.registerCommand(new Command());
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }

    public static CapeTest getInstance() {
        return instance;
    }

    public CapeTestConfig getConfig() {
        return config;
    }

    public PlayerCache getPlayerCache() {
        return playerCache;
    }
}
