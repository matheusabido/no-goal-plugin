package dev.abidux.nogoalplugin.scheduler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.persistence.PersistentDataType;

import dev.abidux.nogoalplugin.NoGoalPlugin;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class MinecartScheduler implements Runnable {
    private static final HashSet<Chunk> CHUNKS_TO_UNLOAD = new HashSet<>();

    @Override
    public void run() {
        // long start = System.currentTimeMillis();

        List<Entity> minecarts = Bukkit.getWorlds()
            .stream()
            .map(w -> w.getEntitiesByClasses(Minecart.class))
            .flatMap(minecartList -> minecartList.stream().filter(m -> {
                Boolean bool = m.getPersistentDataContainer().get(NoGoalPlugin.KEY_MLC, PersistentDataType.BOOLEAN);
                return bool != null && bool;
            }))
            .toList();
        
        for (Entity minecart : minecarts) {
            Location predictionLocation = minecart.getLocation().add(minecart.getVelocity().multiply(2));
            Chunk chunk = predictionLocation.getChunk();
            if (!chunk.isLoaded()) {
                boolean success = chunk.load(true);
                if (!success && NoGoalPlugin.getBot() != null) {
                    Long textChannelId = NoGoalPlugin.getInstance().getConfig().getLong("text_channel_id");
    
                    TextChannel textChannel = NoGoalPlugin.getBot().getInstance().getTextChannelById(textChannelId);
                    textChannel.sendMessage(":x: Minecart could not load chunk at " + predictionLocation.getBlockX() + ", " + predictionLocation.getBlockY() + ", " + predictionLocation.getBlockZ()).queue();
                }
            }
            
            if (chunk.getX() != minecart.getChunk().getX() || chunk.getZ() != minecart.getChunk().getZ()) {
                // load and unload chunks
            }
        }

        // long total = System.currentTimeMillis() - start;
        // System.out.println(total + "ms");
    }

    public static void unloadChunks() {
        Iterator<Chunk> iterator = CHUNKS_TO_UNLOAD.iterator();
        while (iterator.hasNext()) {
            Chunk chunk = iterator.next();
            chunk.setForceLoaded(false);
            iterator.remove();
        }
    }
}