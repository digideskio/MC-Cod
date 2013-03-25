package JBCod;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CarePackageHandler implements Listener{
	public ArrayList<Location> chests = new ArrayList<Location>();
	@EventHandler
	public void onThrow(PlayerEggThrowEvent e) {
		e.setHatching(false);
	}

	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Egg) {
			Player p = (Player) event.getEntity().getShooter();
			Location loc = event.getEntity().getLocation();
			loc.setY(event.getEntity().getLocation().getY()+20);
			loc.setX(event.getEntity().getLocation().getX());
			loc.setZ(event.getEntity().getLocation().getZ());
			Byte blockData = 0x0;
			p.getWorld().spawnFallingBlock(loc, Material.CHEST, blockData);			
		}
	}
	@EventHandler
	public void sand(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType().equals(Material.CHEST)){
			Location loc = e.getClickedBlock().getLocation();
			if(!chests.contains(loc)){
				Chest c = (Chest) loc.getBlock().getState();
				Inventory inv = c.getInventory();
				ItemStack ammo = new ItemStack(Material.GHAST_TEAR,64);
				ItemMeta meta = ammo.getItemMeta();
				meta.setDisplayName(ChatColor.RED+""+ChatColor.BOLD+"Ammo");
				ammo.setItemMeta(meta);
				inv.addItem(ammo);
				c.update();
				chests.add(loc);
			}
			
			
		}
	}
}