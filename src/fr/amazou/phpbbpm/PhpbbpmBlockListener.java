/*
 * phpbbpm block listener
 */
package fr.amazou.phpbbpm;

import java.util.Arrays;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

/**
 *
 * @author Zougi
 */
public class PhpbbpmBlockListener extends BlockListener {
    private static Phpbbpm plugin;
    private Logger log;
    
    public PhpbbpmBlockListener(Phpbbpm instance) {
        plugin = instance;
        log = plugin.getLog();
    }
    
    @Override
    public void onSignChange(SignChangeEvent event) {
        if (event.getLine(0).trim().equalsIgnoreCase("PhpbbPM")) {
            SqlManager sql = new SqlManager();
            sql.setPlayer(event.getPlayer());
            if (sql.StoreSign(event.getBlock().getLocation())) {
                int pmNb = sql.getNbUnreadMsg_solo();
                event.setLine(2, String.format("%s%d unread msg",
                        ChatColor.RED, pmNb));
            } else {
                event.getPlayer().sendMessage(ChatColor.YELLOW + "Can't store sign");
            }
            sql.Close();
        }
    }


    @Override
    public void onBlockBreak(BlockBreakEvent event) {
        Material mat = event.getBlock().getType();
        if ((mat.equals(Material.SIGN) || mat.equals(Material.SIGN_POST) || mat.equals(Material.WALL_SIGN))) {
            Sign sign = (Sign)event.getBlock().getState();
            if (sign.getLine(0).trim().equalsIgnoreCase("PhpbbPM")) {
                SqlManager sql = new SqlManager();
                sql.DeleteSign(sign.getBlock().getLocation());
                sql.Close();
            }
        }
    }
    
}
