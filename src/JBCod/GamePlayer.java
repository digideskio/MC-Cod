package JBCod;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class GamePlayer {
	private Player p;
	private int kills = 0;
	private int deaths = 0;
	private int killStreak = 0;
	private Team team = null;
	private boolean isZoomedIn = false;
	private long invincibleUntil = 0;

	public GamePlayer(Player p) {
		this.p = p;
	}

	public GamePlayer(Player p, Team team) {
		this.p = p;
		this.team = team;
	}

	public void addDeaths() {
		deaths++;
		invincibleUntil = System.currentTimeMillis()+30000;
	}

	public boolean isInvincible() {
		if (System.currentTimeMillis() < invincibleUntil)
			return true;
		else
			return false;
	}
	
	public boolean isZoomedIn() {
		return isZoomedIn;
	}

	public void addKills() {
		kills++;
		this.team.addKill();
	}

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public Player getPlayer() {
		return p;
	}

	public Team getTeam() {
		return team;
	}

	public double getKDR() {
		return kills / ((double) deaths);
	}

	public void toggleZoom(int modifier) {
		if (isZoomedIn) {
			isZoomedIn = false;
			ZoomManager.addZoomEffect(this, modifier);
		} else {
			isZoomedIn = true;
			ZoomManager.removeZoomEffect(this);
		}
	}

	public void changeTeam(Team newTeam) {
		this.team = newTeam;
	}

	public void clearInventory() {
		this.getPlayer().getInventory().clear();
	}

	public void ejectFromGame() {
		this.getPlayer().teleport(
				new Location(Bukkit.getWorld("world"), 100, 64, 100));
		this.getPlayer().getInventory().clear();
		this.getPlayer().sendMessage(
				"Nice job! Kills: " + this.getKills() + ". Deaths: "
						+ this.getDeaths());
	}

	public void suitUp() {
		this.getPlayer().getInventory().clear();
		this.getPlayer()
				.getInventory()
				.setArmorContents(
						new ItemStack[] {
								recolorArmor(new ItemStack(
										Material.LEATHER_BOOTS)),
								recolorArmor(new ItemStack(
										Material.LEATHER_LEGGINGS)),
								recolorArmor(new ItemStack(
										Material.LEATHER_CHESTPLATE)),
								recolorArmor(new ItemStack(
										Material.LEATHER_HELMET)) });

		ItemStack gun = new Gun("gun", Material.WOOD_HOE, 5, 3, 1, 3, 3)
				.generateItemStack();

		ItemStack ammo = new ItemStack(Material.GHAST_TEAR, 64);
		ItemMeta ammoMeta = ammo.getItemMeta();
		ammoMeta.setDisplayName("Ammo");
		ammo.setItemMeta(ammoMeta);

		this.getPlayer().getInventory().setItem(0, gun);
		this.getPlayer().getInventory().setItem(1, ammo);

		this.getPlayer().setCanPickupItems(false);

		this.getPlayer().getInventory()
				.setItem(7, new ItemStack(Material.WOOL, this.getDeaths()));
		this.getPlayer().getInventory()
				.setItem(8, new ItemStack(Material.WOOL, this.getKills()));
	}

	private ItemStack recolorArmor(ItemStack itemToRecolor) {
		ItemStack toColor = itemToRecolor;
		LeatherArmorMeta meta = (LeatherArmorMeta) toColor.getItemMeta();
		meta.setColor((this.getTeam().getColor()));
		toColor.setItemMeta(meta);
		return toColor;
	}

	public int getKillStreak() {
		return killStreak;
	}

	public void addKillStreak() {
		this.killStreak++;
	}

	public void resetKillStreak() {
		this.killStreak = 0;
	}
}
