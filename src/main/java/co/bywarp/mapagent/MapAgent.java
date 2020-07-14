/*
 * Copyright (c) 2019-2020 Warp <legal@warp.pw>
 * All Rights Reserved.
 *
 * This software is proprietary and is designed and intended for internal use only.
 * Unauthorized use, replication, distribution, or modification of this software
 * in any capacity is unlawful and punishable by the full extent of the law.
 */

package co.bywarp.mapagent;

import co.bywarp.mapagent.command.CommandHandler;
import co.bywarp.mapagent.command.commands.AuthorCommand;
import co.bywarp.mapagent.command.commands.BlocksCommand;
import co.bywarp.mapagent.command.commands.CenterCommand;
import co.bywarp.mapagent.command.commands.CreateCommand;
import co.bywarp.mapagent.command.commands.GametypeCommand;
import co.bywarp.mapagent.command.commands.MapDataCommand;
import co.bywarp.mapagent.command.commands.MapInfoCommand;
import co.bywarp.mapagent.command.commands.ParseCommand;
import co.bywarp.mapagent.data.game.GameDataManager;
import co.bywarp.mapagent.data.game.types.cannons.CannonsData;
import co.bywarp.mapagent.data.game.types.deathmatch.DeathmatchData;
import co.bywarp.mapagent.data.game.types.deathrun.DeathRunData;
import co.bywarp.mapagent.data.game.types.dragonrun.DragonRunData;
import co.bywarp.mapagent.data.game.types.egghunt.EggHuntData;
import co.bywarp.mapagent.data.game.types.iceball.IceBallData;
import co.bywarp.mapagent.data.game.types.infected.InfectedData;
import co.bywarp.mapagent.data.game.types.melonwars.MelonWarsData;
import co.bywarp.mapagent.data.game.types.snowfight.SnowFightData;
import co.bywarp.mapagent.data.game.types.spleef.SpleefData;
import co.bywarp.mapagent.data.game.types.sumo.SumoData;
import co.bywarp.mapagent.data.game.types.teamdeathmatch.TeamDeathmatchData;
import co.bywarp.mapagent.data.repository.MapDataRepository;
import co.bywarp.mapagent.parcel.JsonParcel;
import co.bywarp.mapagent.parser.ChunkParser;
import co.bywarp.mapagent.update.UpdateEvent;
import co.bywarp.mapagent.utils.PlayerUtils;
import co.bywarp.mapagent.utils.text.Lang;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

import lombok.Getter;
import lombok.Setter;

@Getter
public class MapAgent extends JavaPlugin implements Listener {

    private JsonParcel parcel;
    private CommandHandler commandHandler;
    private MapDataRepository repository;
    private GameDataManager manager;

    @Setter private ChunkParser currentParse;

    @Override
    public void onEnable() {
        this.parcel = new JsonParcel(this, new File("agent.json"));
        this.commandHandler = new CommandHandler(this);
        this.repository = new MapDataRepository(this, new File("repository.json"));
        this.manager = new GameDataManager(this,
                null,
                new CannonsData(),
                new DeathmatchData(),
                new DeathRunData(),
                new DragonRunData(),
                new EggHuntData(),
                new IceBallData(),
                new InfectedData(),
                new MelonWarsData(),
                new SnowFightData(),
                new SpleefData(),
                new SumoData(),
                new TeamDeathmatchData());

        this.registerCommands();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        commandHandler.registerCommand("author", new String[] { "setauthor" }, new AuthorCommand(repository));
        commandHandler.registerCommand("blocks", new BlocksCommand(manager, repository));
        commandHandler.registerCommand("center", new String[] { "setcenter" }, new CenterCommand(repository));
        commandHandler.registerCommand("create", new CreateCommand(repository));
        commandHandler.registerCommand("data", new String[] { "mapdata" }, new MapDataCommand(repository));
        commandHandler.registerCommand("gametype", new String[] { "setgametype" }, new GametypeCommand(repository));
        commandHandler.registerCommand("mapinfo", new MapInfoCommand(repository));
        commandHandler.registerCommand("parse", new ParseCommand(this, parcel.getExtruderPreferences(), repository, manager));
    }

    @EventHandler
    public void onUpdate(UpdateEvent event) {
        if (getCurrentParse() != null) {
            return;
        }

        PlayerUtils.sendServerMessage(Lang.colorMessage("&c&lUpdater is active, but there is no ongoing parse, this will cause problems. &e&l(/rl)"));
    }

}
