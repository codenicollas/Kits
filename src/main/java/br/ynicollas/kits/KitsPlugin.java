package br.ynicollas.kits;

import br.ynicollas.kits.cache.CooldownsCache;
import br.ynicollas.kits.cache.KitsCache;
import br.ynicollas.kits.commands.*;
import br.ynicollas.kits.listeners.InventoryClickListener;
import br.ynicollas.kits.listeners.InventoryCloseListener;
import br.ynicollas.kits.listeners.PlayerQuitListener;
import br.ynicollas.kits.storage.Database;
import br.ynicollas.kits.storage.cooldowns.CooldownsStorage;
import br.ynicollas.kits.storage.kits.KitsStorage;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class KitsPlugin extends JavaPlugin {

    private Database database;

    private CooldownsStorage cooldowns;
    private KitsStorage kits;

    private CooldownsCache cooldownsCache;

    @Override
    public void onEnable() {
        getLogger().info("Iniciando Kits v" + getDescription().getVersion() + "...");

        saveDefaultConfig();

        setupDatabase();

        registerCommands();
        registerListeners();

        getLogger().info("Plugin Kits v" + getDescription().getVersion() + " habilitado com sucesso!");
    }

    @Override
    public void onDisable() {
        if (database != null) {
            database.closeConnection();
        }

        getLogger().info("Plugin Kits desabilitado.");
    }

    private void setupDatabase() {
        database = new Database(this);

        database.openConnection();

        this.cooldownsCache = new CooldownsCache();
        KitsCache kitsCache = new KitsCache();

        this.cooldowns = new CooldownsStorage(database, cooldownsCache);
        this.kits = new KitsStorage(database, kitsCache);
    }

    private void registerCommands() {
        getLogger().info("Registrando comandos...");

        registerCommand("createkit", new CreateKitCommand(kits));
        registerCommand("deletekit", new DeleteKitCommand(cooldowns, kits));
        registerCommand("editkit", new EditKitCommand(kits));
        registerCommand("givekit", new GiveKitCommand(kits));
        registerCommand("kit", new KitCommand(cooldowns, kits));
        registerCommand("viewkit", new ViewKitCommand(kits));
    }

    private void registerListeners() {
        getLogger().info("Registrando listeners...");

        getServer().getPluginManager().registerEvents(new InventoryCloseListener(kits), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(cooldownsCache), this);
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand command = getCommand(commandName);

        if (command != null) {
            command.setExecutor(executor);
        } else {
            getLogger().severe("Falha ao registrar o comando '" + commandName + "'.");
        }
    }
}