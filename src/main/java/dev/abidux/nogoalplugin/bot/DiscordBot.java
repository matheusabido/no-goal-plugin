package dev.abidux.nogoalplugin.bot;

import java.util.EnumSet;

import dev.abidux.nogoalplugin.NoGoalPlugin;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DiscordBot extends ListenerAdapter {
    
    private JDA instance;

    public DiscordBot(String token) {
        EnumSet<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
        this.instance = JDABuilder.createLight(token, intents).addEventListeners(this).build();
    }

    public void start() {
        try {
            this.instance.awaitReady();
            this.instance.updateCommands().addCommands(
                Commands.slash("ip", "Envia o ip do servidor")
            ).queue();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "ip":
                String serverIp = NoGoalPlugin.getInstance().getConfig().getString("server_ip");
                event.reply("IP do servidor: " + serverIp).queue();
                break;
        }
    }

    public JDA getInstance() {
        return instance;
    }
}
