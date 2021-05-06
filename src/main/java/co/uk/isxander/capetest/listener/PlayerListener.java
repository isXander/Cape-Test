package co.uk.isxander.capetest.listener;

import club.sk1er.mods.core.util.Multithreading;
import co.uk.isxander.capetest.CapeTest;
import co.uk.isxander.capetest.auth.PlayerData;
import co.uk.isxander.capetest.render.CapeLayer;
import co.uk.isxander.xanderlib.utils.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerListener implements Constants {

    private final List<UUID> toAdd = new ArrayList<>();

    @SubscribeEvent
    public void onPlayerRender(RenderPlayerEvent.Pre event) {
        UUID uuid = event.entityPlayer.getUniqueID();
        if (toAdd.contains(uuid)) {
            toAdd.remove(uuid);
            PlayerData data = CapeTest.getInstance().getPlayerCache().getPlayerData(uuid);
            if (data.hasCape())
                event.renderer.addLayer(new CapeLayer(event.renderer));
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinWorldEvent event) {
        if (!CapeTest.getInstance().getConfig().enabled) return;
        if (!(event.entity instanceof EntityPlayer)) return;
        toAdd.add(event.entity.getUniqueID());
    }

}
