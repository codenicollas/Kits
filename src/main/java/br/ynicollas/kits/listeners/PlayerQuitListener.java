package br.ynicollas.kits.listeners;

import br.ynicollas.kits.cache.CooldownsCache;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final CooldownsCache cooldownsCache;

    public PlayerQuitListener(CooldownsCache cooldownsCache) {
        this.cooldownsCache = cooldownsCache;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        cooldownsCache.removePlayer(player);
    }
}