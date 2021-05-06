package co.uk.isxander.capetest.auth;

import club.sk1er.mods.core.util.WebUtil;
import co.uk.isxander.capetest.CapeTest;
import co.uk.isxander.xanderlib.utils.Constants;
import co.uk.isxander.xanderlib.utils.HttpsUtils;
import co.uk.isxander.xanderlib.utils.json.BetterJsonObject;
import net.minecraft.client.renderer.texture.DynamicTexture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache implements Constants {

    public static final File CACHE_LOCATION = new File(CapeTest.DATA_DIR, "cache.json");
    public static final String CAPE_INFO_URL = CapeTest.REPO_URL + "capes.json";

    private BetterJsonObject jsonObj;
    private FileWriter fileWriter;

    private final Map<UUID, PlayerData> cache;

    public PlayerCache() {
        cache = new HashMap<>();
        jsonObj = null;
        fileWriter = null;
        try {
            fileWriter = new FileWriter(CACHE_LOCATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean saveCache() {
        String response = HttpsUtils.getString(CAPE_INFO_URL);
        if (response == null) return false;

        jsonObj = new BetterJsonObject(response);
        writeJson();



        return true;
    }

    public void clearCache() {
        cache.clear();
    }

    public PlayerData getPlayerData(UUID uuid) {
        return cache.computeIfAbsent(uuid, (k) -> {
            try {
                String uuidStr = uuid.toString();
                if (jsonObj.get("uuids").getAsJsonObject().has(uuidStr)) {
                    String cape = new BetterJsonObject(jsonObj.get("uuids").getAsJsonObject()).optString(uuidStr);
                    File file = new File(CapeTest.DATA_DIR, "/" + cape);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        HttpsUtils.downloadFile(CapeTest.REPO_URL + "assets/" + cape, file);
                    }
                    return new PlayerData(uuid, mc.getTextureManager().getDynamicTextureLocation(CapeTest.MOD_ID, new DynamicTexture(getImage(file))));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new PlayerData(uuid, null);
        });
    }

    private static BufferedImage getImage(File file) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return img;
    }

    private void writeJson() {
        if (!CACHE_LOCATION.exists() || !CACHE_LOCATION.isDirectory()) {
            try {
                if (!CACHE_LOCATION.exists()) {
                    File parent = CACHE_LOCATION.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }

                    CACHE_LOCATION.createNewFile();
                }

                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(jsonObj.toPrettyString());
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
