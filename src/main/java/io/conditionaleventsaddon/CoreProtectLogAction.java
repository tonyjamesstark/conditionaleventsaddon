package io.conditionaleventsaddon;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.World;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import ce.ajneb97.api.ConditionalEventsAction;

import net.coreprotect.CoreProtect;
import net.coreprotect.CoreProtectAPI;

public class CoreProtectLogAction extends ConditionalEventsAction {

    CoreProtectAPI cp = null;

    public CoreProtectLogAction() {
        super("log_removal");
        this.cp = this.getCoreProtect();
    }

    @Override
    public void execute(Player player, String actionLine) {
        // Format: log_removal: <player>;<x>;<y>;<z>;<material>;<block_data>
        String[] sep = actionLine.split(";");

        if (sep.length < 6){
            System.out.println(
                String.format("CoreProtectLogAction: Insufficient parameters passed: %s", actionLine));
            return;
        }

        String user = sep[0];
        double x = Double.parseDouble(sep[1]);
        double y = Double.parseDouble(sep[2]);
        double z = Double.parseDouble(sep[3]);
        Material mat = Material.getMaterial(sep[4]);
        BlockData data = mat.createBlockData(sep[5]);

        World world = Bukkit.getWorld(sep[0]);
        Location loc = new Location(world, x, y, z);

        this.cp.logRemoval(user, loc, mat, data);
    }

    // from https://docs.coreprotect.net/api/version/v10/
    private CoreProtectAPI getCoreProtect() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("CoreProtect");

        // Check that CoreProtect is loaded
        if (plugin == null || !(plugin instanceof CoreProtect)) {
            return null;
        }

        // Check that the API is enabled
        CoreProtectAPI _cp = ((CoreProtect) plugin).getAPI();
        if (_cp.isEnabled() == false) {
            return null;
        }

        // Check that a compatible version of the API is loaded
        if (_cp.APIVersion() < 10) {
            return null;
        }

        return _cp;
    }
}