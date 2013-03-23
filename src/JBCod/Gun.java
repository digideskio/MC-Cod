package JBCod;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gun {
	private String name;
	private Material type;
	private int velocity;
	private int zoomMod;
	private int spray;
	private int numberBulletsToFire;
	private int damage;
	private int timeOut;
	

	public Gun(String name) {
		this.name = name;
	}
	
	public Gun(String name, Material material) {
		this.name = name;
		this.type = material;
	}
	
	public Gun(String name, Material material, int fireVelocity) {
		this.name = name;
		this.type = material;
		this.setVelocity(fireVelocity);
	}
	
	public Gun(String name, Material material, int fireVelocity, int zoomMod) {
		this.name = name;
		this.type = material;
		this.setVelocity(fireVelocity);
		this.setZoomMod(zoomMod);
	}
	
	public Gun(String name, Material material, int fireVelocity, int zoomMod, int spray) {
		this.name = name;
		this.type = material;
		this.setVelocity(fireVelocity);
		this.setZoomMod(zoomMod);
		this.setSpray(spray);
	}
	
	public Gun(String name, Material material, int fireVelocity, int zoomMod, int spray, int numberBulletsToFire) {
		this.name = name;
		this.type = material;
		this.setVelocity(fireVelocity);
		this.setZoomMod(zoomMod);
		this.setSpray(spray);
		this.setNumberBulletsToFire(numberBulletsToFire);
	}
	
	public Gun(String name, Material material, int fireVelocity, int zoomMod, int spray, int numberBulletsToFire, int damage) {
		this.name = name;
		this.type = material;
		this.setVelocity(fireVelocity);
		this.setZoomMod(zoomMod);
		this.setSpray(spray);
		this.setNumberBulletsToFire(numberBulletsToFire);
		this.setDamage(damage);
	}
	
	public Gun(String name, Material material, int fireVelocity, int zoomMod, int spray, int numberBulletsToFire, int damage, int timeOut) {
		this.name = name;
		this.type = material;
		this.setVelocity(fireVelocity);
		this.setZoomMod(zoomMod);
		this.setSpray(spray);
		this.setNumberBulletsToFire(numberBulletsToFire);
		this.setDamage(damage);
		this.timeOut = timeOut;
	}
	
	public int getTimeOut() {
		return timeOut;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setType(Material newType) {
		this.type = newType;
	}
	
	public Material getType() {
		return type;
	}

	public int getZoomMod() {
		return zoomMod;
	}

	public void setZoomMod(int zoomMod) {
		this.zoomMod = zoomMod;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
	}

	public int getSpray() {
		return spray;
	}

	public void setSpray(int spray) {
		this.spray = spray;
	}

	public int getNumberBulletsToFire() {
		return numberBulletsToFire;
	}

	public void setNumberBulletsToFire(int numberBulletsToFire) {
		this.numberBulletsToFire = numberBulletsToFire;
	}
	
	public ItemStack generateItemStack() {
		ItemStack i = new ItemStack(type);
		ItemMeta im = i.getItemMeta();
		im.setDisplayName("gun");
		i.setItemMeta(im);
		return i;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}
}
