package JBCod;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import Util.ChatHelpers;

public class SignTeleport {
	public HashMap<String, Location> sign = new HashMap<String, Location>();

	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if (sign.containsValue(e.getClickedBlock().getLocation())) {
			if(sign.containsKey("play")){
				
			}else if(sign.containsKey("leave")){
				
			}else if(sign.containsKey("shop")){
				
			}
		}
	}

	public void onSignPlace(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (e.getLine(0).equalsIgnoreCase("[mc-cod]")) {
			if(e.getLine(1).equalsIgnoreCase("Play")){
				sign.put("play", e.getBlock().getLocation());
				p.sendMessage(ChatHelpers.codInfo+"Created Arena Sign");
			}else if (e.getLine(1).equalsIgnoreCase("Shop")){
				//would go to Cod shop
				sign.put("shop", e.getBlock().getLocation());
				p.sendMessage(ChatHelpers.codInfo+"Created Shop sign");
			}else if(e.getLine(1).equalsIgnoreCase("Leave")){
				//would bring you to mini-game lobby
				sign.put("leave", e.getBlock().getLocation());
				p.sendMessage(ChatHelpers.codInfo+"Created Leave sign");
			}else{
				p.sendMessage(ChatHelpers.codError+"Must enter a destination!");
			}
		}
	}
}
