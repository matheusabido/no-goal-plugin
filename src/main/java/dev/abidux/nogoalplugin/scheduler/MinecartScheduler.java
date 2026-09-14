package dev.abidux.nogoalplugin.scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;

import dev.abidux.nogoalplugin.NoGoalPlugin;
import dev.abidux.nogoalplugin.utils.MineUtils;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class MinecartScheduler implements Runnable {
    public static final HashSet<Chunk> CHUNKS_TO_UNLOAD = new HashSet<>();

    @Override
    public void run() {
        List<Entity> minecarts = Bukkit.getWorlds()
            .stream()
            .map(w -> w.getEntitiesByClasses(Minecart.class))
            .flatMap(minecartList -> minecartList.stream().filter(MineUtils::isMLC))
            .toList();

        for (Entity minecart : minecarts) {
            Location predictionLocation = minecart.getLocation().add(minecart.getVelocity().multiply(1));

            Chunk predictedChunk = predictionLocation.getChunk();
            if (!predictedChunk.isLoaded()) {
                boolean success = predictedChunk.load(true);
                if (!success && NoGoalPlugin.getBot() != null) {
                    Long textChannelId = NoGoalPlugin.getInstance().getConfig().getLong("text_channel_id");
    
                    TextChannel textChannel = NoGoalPlugin.getBot().getInstance().getTextChannelById(textChannelId);
                    textChannel.sendMessage(":x: Minecart could not load chunk at " + predictionLocation.getBlockX() + ", " + predictionLocation.getBlockY() + ", " + predictionLocation.getBlockZ()).queue();
                }
            }
            predictedChunk.setForceLoaded(true);
            CHUNKS_TO_UNLOAD.add(predictedChunk);
        }
    }

    public static void unloadChunks() {
        Iterator<Chunk> iterator = CHUNKS_TO_UNLOAD.iterator();
        while (iterator.hasNext()) {
            Chunk chunk = iterator.next();
            if (!MineUtils.hasLoaderMinecart(chunk, null)) {
                chunk.setForceLoaded(false);
                iterator.remove();
            }
        }
    }

    public static void setChunksToUnload(FileConfiguration config) {
        List<String> serialized = CHUNKS_TO_UNLOAD.stream().map((chunk) -> chunk.getWorld().getName() + "," + chunk.getX() + "," + chunk.getZ()).toList();
        config.set("mlc", serialized);
    }

    public static void loadChunksToUnload(FileConfiguration config) {
        if (!config.isSet("mlc")) return;
        config.getStringList("mlc").forEach((chunkData) -> {
            try {
                String[] parts = chunkData.split(",");
                String worldName = parts[0];
                int x = Integer.parseInt(parts[1]);
                int z = Integer.parseInt(parts[2]);
                
                World world = Bukkit.getWorld(worldName);
                Chunk chunk = world.getChunkAt(x, z);
                System.out.println("CHUNK " + x + "," + z + " was read from config.");
                if (!chunk.isLoaded()) {
                    System.out.println("It had to be loaded.");
                    chunk.load(true);
                    chunk.setForceLoaded(true);
                }
                CHUNKS_TO_UNLOAD.add(chunk);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}