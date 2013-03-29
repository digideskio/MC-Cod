package JBCod;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class Grenade implements Listener {
	@EventHandler
	public void onRight(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			Player p = e.getPlayer();
			Location loc = p.getLocation();
			loc.setY(loc.getY()+1.5);
			final Item dropped = p.getWorld().dropItem(loc, new ItemStack(Material.SLIME_BALL,1));
			dropped.setVelocity(p.getLocation().getDirection().add(dropped.getVelocity().setX(dropped.getVelocity().getX())).add(dropped.getVelocity().setZ(dropped.getVelocity().getZ())));
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(new test().getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					Location droppedLoc = dropped.getLocation();
					droppedLoc.getWorld().createExplosion(droppedLoc.getX(), droppedLoc.getY(), droppedLoc.getZ(), 5, true, true);
					dropped.remove();
				}
			}, 100L);
			
		}
	}
	
	@EventHandler
	public void pickup(PlayerPickupItemEvent e){
		if(!e.getPlayer().isOp()){
			e.setCancelled(true);
		}
	}

}
