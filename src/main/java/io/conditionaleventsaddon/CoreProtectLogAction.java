package io.conditionaleventsaddon;

import java.util.logging.Logger;
import java.lang.Boolean;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.BlockState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ce.ajneb97.api.ConditionalEventsAction;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class CoreProtectLogAction extends ConditionalEventsAction {

    Logger log = null;
    CoreProtectAPI api = null;

    public CoreProtectLogAction(Logger _log) {
        super("log_removal");
        this.log = _log;
        this.api = this.getCoreProtect();
    }

    @Override
    public void execute(Player player, String actionLine) {
        // this.log.info("executing coreprotectlogaction");
        // Format: log_removal: <x>;<y>;<z>
        String[] sep = actionLine.split(";");
        // this.log.info(sep.toString());

        if (sep.length < 3){
            this.log.severe(
                String.format("CoreProtectLogAction: Insufficient parameters in config: %s", actionLine));
            return;
        }

        String user = player.getName(); //sep[0];
        double x = Double.parseDouble(sep[0]);
        double y = Double.parseDouble(sep[1]);
        double z = Double.parseDouble(sep[2]);
        World world = player.getWorld();
        // Material mat = Material.getMaterial(sep[5]);
        // BlockData data = mat.createBlockData(sep[6]);

        Location loc = new Location(world, x, y, z);
        // this.log.info(loc.toString());

        // BlockState state = world.getBlockState(new Location(world, x, y, z));
        BlockState state = world.getBlockState(loc);
        // this.log.info(state.toString());

        if (this.api == null){
            this.log.severe("No access to CoreProtectAPI");
            return;
        }
        // this.log.info("Testing API...");
        // this.api.testAPI();

        // boolean success = this.cp.logRemoval(user, loc, mat, data);
        boolean success = this.api.logRemoval(user, state);
        // this.log.info(Boolean.toString(success));

        if (!success){
            this.log.severe("Logging unsuccessful");
        }
    }

    // from https://docs.coreprotect.net/api/version/v10/
    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI _api = ((CoreProtect) plugin).getAPI();
        if (_api.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (_api.APIVersion() < 10) {
            return null;
        }

        return _api;
    }
}