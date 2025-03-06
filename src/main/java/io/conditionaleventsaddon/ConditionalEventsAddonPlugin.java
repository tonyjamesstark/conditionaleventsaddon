package io.conditionaleventsaddon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;

import ce.ajneb97.api.ConditionalEventsAPI;
import ce.ajneb97.api.ConditionalEventsEvent;

public class ConditionalEventsAddonPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        ConditionalEventsAPI.registerApiActions(this,new CoreProtectLogAction());
        
    }

    // from https://ajneb97.gitbook.io/conditionalevents/api
    //Event called when conditions for an event are accomplished and a group
	//of actions is executed.
	@EventHandler
	public void actionsExecuted(ConditionalEventsEvent event){
	   Player player = event.getPlayer(); //Returns null if not a player event
	   String eventName = event.getEvent();
	   // String actionName = event.getAction();  //Changed to actionGroup?
	}

}