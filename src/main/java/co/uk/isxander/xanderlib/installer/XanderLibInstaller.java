package co.uk.isxander.xanderlib.installer;

import club.sk1er.mods.core.ModCoreInstaller;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import club.sk1er.mods.core.ModCoreInstaller.JsonHolder;

public class XanderLibInstaller {

    // What version of XanderLib does this mod use
    public static final String DESIRED_VERSION = "0.3";
    private static final String VERSION_URL = "https://raw.githubusercontent.com/isXander/XanderLib/main/version.json";
    private static File dataDir = null;
    private static boolean isInstalled = false;

    private static boolean isInstalled() {
        return isInstalled;
    }

    public static int initialize(File gameDir) {
        if (isInstalled()) {
            return -1;
        }
        dataDir = new File(gameDir, "xanderlib");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        File data = new File(dataDir, "metadata.json");
        System.out.println("Metadata Json: " + data.getPath());


        File jar = new File(dataDir, "/" + DESIRED_VERSION + "/xanderlib.jar");
        new File(dataDir, "/" + DESIRED_VERSION).mkdirs();
        System.out.println("XanderLib Jar: " + jar.getPath());
        JsonHolder versionData = fetchJSON(VERSION_URL);

        boolean metaExists = data.exists();
        JsonHolder metadata = null;
        if (metaExists)
            metadata = readFile(data);
        if (!metaExists || !metadata.has("installed_versions") || !jsonArrayContains(metadata.optJSONArray("installed_versions"), new JsonPrimitive(DESIRED_VERSION))) {
            download("https://static.isxander.co.uk/mods/xanderlib/" + DESIRED_VERSION + ".jar", DESIRED_VERSION, jar, metadata);
        }

        ModCoreInstaller.addToClasspath(jar);

        isInstalled = true;
        System.out.println("XanderLib is now installed.");
        return 0;
    }

    public static boolean inClasspath() {
        try {
            Class.forName("co.uk.isxander.xanderlib.XanderLib", false, XanderLibInstaller.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static boolean download(String url, String version, File file, JsonHolder versionData) {
        System.out.println("Downloading XanderLib v" + version + " -> " + url);

        url = url.replace(" ", "%20");


        HttpURLConnection connection = null;
        InputStream is = null;
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (XanderLib Automatic Installer)");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            is = connection.getInputStream();
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) > 0) {
                outputStream.write(buffer, 0, read);
            }

            JsonArray arr = versionData.optJSONArray("installed_versions");
            JsonPrimitive ver = new JsonPrimitive(version);
            if (!jsonArrayContains(arr, ver))
                arr.add(ver);
            versionData.put("installed_versions", (JsonElement) arr);

            FileUtils.write(new File(dataDir, "metadata.json"), versionData.toString(), Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }

                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                System.out.println("Failed to clean up XanderLib Automatic Installer.");
                e.printStackTrace();
            }
        }
        return true;
    }

    private static boolean jsonArrayContains(JsonArray arr, JsonPrimitive primitive) {
        for (JsonElement element : arr) {
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().equals(primitive))
                return true;
        }
        return false;
    }

    private static JsonHolder readFile(File in) {
        try {
            return new JsonHolder(FileUtils.readFileToString(in, Charset.defaultCharset()));
        } catch (IOException ignored) {

        }
        return new JsonHolder();
    }

    public static JsonHolder fetchJSON(String url) {
        return new JsonHolder(fetchString(url));
    }

    public static String fetchString(String url) {
        url = url.replace(" ", "%20");
        System.out.println("Fetching " + url);

        HttpURLConnection connection = null;
        InputStream is = null;
        try {
            URL u = new URL(url);
            connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.addRequestProperty("User-Agent", "Mozilla/4.76 (XanderLib Automatic Installer)");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoOutput(true);
            is = connection.getInputStream();
            return IOUtils.toString(is, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.disconnect();
                }

                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                System.out.println("Failed to clean up XanderLib Automatic Installer.");
                e.printStackTrace();
            }
        }

        return "Failed to fetch";
    }

}