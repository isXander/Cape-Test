package co.uk.isxander.capetest.auth;

import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private final ResourceLocation cape;
    private final boolean hasCape;

    public PlayerData(UUID uuid, ResourceLocation cape) {
        this.uuid = uuid;
        this.hasCape = cape != null;
        this.cape = cape;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ResourceLocation getCapeLocation() {
        return cape;
    }

    public boolean hasCape() {
        return hasCape;
    }
}
