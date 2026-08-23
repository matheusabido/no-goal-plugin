package dev.abidux.nogoalplugin.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import dev.abidux.nogoalplugin.model.PlayerData;

public class PlayerDeath implements Listener {
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        PlayerData data = PlayerData.get(event.getPlayer());
        
        if (data.isKeepInventoryActive()) {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
    }

}
