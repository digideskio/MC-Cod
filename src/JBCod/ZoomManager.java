package JBCod;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ZoomManager {
	public static void addZoomEffect(GamePlayer p, int mod) {
		p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,
							1000000000, mod));
	}

	public static void removeZoomEffect(GamePlayer p) {
		p.getPlayer().removePotionEffect(PotionEffectType.SLOW);
	}
}
