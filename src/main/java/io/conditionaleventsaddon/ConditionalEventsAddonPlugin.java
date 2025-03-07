package io.conditionaleventsaddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import ce.ajneb97.api.ConditionalEventsAPI;
import ce.ajneb97.api.ConditionalEventsEvent;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class ConditionalEventsAddonPlugin extends JavaPlugin implements Listener {

	Plugin worldGuard = null;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.worldGuard = getWorldGuardPlugin();
        ConditionalEventsAPI.registerApiActions(this, 
        	new CoreProtectLogAction(this.getLogger()),
        	new BreakNaturallyAction(this.getLogger(), this.worldGuard)
        );
    }

    // from https://ajneb97.gitbook.io/conditionalevents/api
    //Event called when conditions for an event are accomplished and a group
	//of actions is executed.
	@EventHandler
	public void actionsExecuted(ConditionalEventsEvent event){
	   Player player = event.getPlayer(); //Returns null if not a player event
	   String eventName = event.getEvent();
	   // String actionGroup = event.getAction();  //Changed to actionGroup?	   
	   String actionGroup = event.getActionGroup();
	}

	//https://bukkit.org/threads/worldguard-methods-listener.61430/
    private WorldGuardPlugin getWorldGuardPlugin() {       
        // Return value variable, by default null
        WorldGuardPlugin plugin = null;
       
        // Try to get the plugin instance
        Plugin p = getServer().getPluginManager().getPlugin("WorldGuard");
       
        // Make sure its not null and its WorldGuard
        if ((p != null) && (p instanceof WorldGuardPlugin)) {
            // Save the instance of WorldGuard in the return value variable;
            plugin = (WorldGuardPlugin) p;
            this.getLogger().info("[ConditionalEventsAddon] WorldGuard v" + p.getDescription().getVersion() + " detected! Enabling WorldGuard Support.");
        }
       
        // Return the instance (or null if no WorldGuard found)
        return plugin;
    }  

}