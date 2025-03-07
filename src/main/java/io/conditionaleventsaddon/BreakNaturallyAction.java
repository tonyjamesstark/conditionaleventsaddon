package io.conditionaleventsaddon;

import java.util.logging.Logger;
import java.lang.Boolean;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ce.ajneb97.api.ConditionalEventsAction;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.ChatColor;

public class BreakNaturallyAction extends ConditionalEventsAction {

    Logger log = null;
    Plugin worldGuard = null;

    public BreakNaturallyAction(Logger _log, Plugin _worldGuard) {
        super("break_naturally");
        this.log = _log;
        this.worldGuard = _worldGuard;
    }

    @Override
    public void execute(Player player, String actionLine) {
        // this.log.info("executing BreakNaturallyAction");
        // Format: break_naturally: <x>;<y>;<z>
        String[] sep = actionLine.split(";");
        // this.log.info(sep.toString());

        if (sep.length < 3){
            this.log.severe(
                String.format("BreakNaturallyAction: Insufficient parameters in config: %s", actionLine));
            return;
        }

        String user = player.getName();
        double x = Double.parseDouble(sep[0]);
        double y = Double.parseDouble(sep[1]);
        double z = Double.parseDouble(sep[2]);
        World world = player.getWorld();

        // ItemStack tool = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        // tool.addEnchantment(Enchantment.SILK_TOUCH, 1);

        Location loc = new Location(world, x, y, z);
        Sound breakSound = world.getBlockAt(loc).getSoundGroup().getBreakSound();
        // this.log.info(loc.toString());

        // boolean success = world.getBlockAt(loc).breakNaturally();
        if(world.getBlockAt(loc).getType() == Material.AIR){
            // prevents infinite loop on using this action in 'type: block_break' event
            return;
        }

        if (this.worldGuard != null){
            WorldGuardPlugin wg = (WorldGuardPlugin) this.worldGuard;
            LocalPlayer localPlayer = wg.inst().wrapPlayer(player);

            boolean canBypass = WorldGuard.getInstance().getPlatform().getSessionManager().hasBypass(localPlayer, localPlayer.getWorld());

            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();

            if (!canBypass && !query.testState(BukkitAdapter.adapt(loc), localPlayer, Flags.BUILD)) {
                // Can't build
                BaseComponent[] msg =
                    new ComponentBuilder("You cannot build here.").color(ChatColor.RED).create();
                player.spigot().sendMessage(msg);
                return;
            }
        }
            

        boolean success = player.breakBlock(player.getWorld().getBlockAt(loc));
        // this.log.info(Boolean.toString(success));

        if (!success){
            this.log.severe("Natural breaking unsuccessful");
        }
        else{
            // default volume and pitch for the vast majority of blocks is 1.0, 0.8
            // https://minecraft.wiki/w/Block_sound_type
            player.playSound(player, breakSound, (float) 1.0, (float) 0.8);
        }
    } 
}