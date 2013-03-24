package JBCod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class test extends JavaPlugin implements Listener {

	private Logger log = Logger.getLogger("Minecraft");
	private HashMap<String, Long> timeout = new HashMap<String, Long>(10);
	private HashMap<String, GamePlayer> gamePlayerList = new HashMap<String, GamePlayer>(
			10);
	private HashMap<String, Gun> gunList = new HashMap<String, Gun>(10);
	private GameEngine engine;
	private CarePackageHandler carePackage;

	@Override
	public void onEnable() {
		log.info("Started up my bitch.");
		World w = Bukkit.createWorld(new WorldCreator("playworld"));
		engine = new GameEngine(180);
		carePackage = new CarePackageHandler();
		gunList.put("gun", new Gun("gun", Material.WOOD_HOE, 5, 3, 1, 3, 3));
		registerEvents();
	}

	@Override
	public void onDisable() {
		log.info("Man fuck you.");
	}

	private void registerEvents() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getServer().getPluginManager().registerEvents(engine, this);
		this.getServer().getPluginManager().registerEvents(carePackage, this);
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		gamePlayerList.put(e.getPlayer().getName(),
				new GamePlayer(e.getPlayer()));
		if (Bukkit.getServer().getOnlinePlayers().length > 10) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				@Override
				public void run() {
					engine.startGameManager(new ArrayList<GamePlayer>(
							gamePlayerList.values()));
				}

			}, 120L);
			Bukkit.broadcastMessage("Starting game in three seconds.");
		}
	}

	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		gamePlayerList.remove(e.getPlayer().getName());
	}

	@EventHandler
	public void invMoveEvent(InventoryClickEvent e) {
		if(!e.getView().getPlayer().isOp()){
		e.setCancelled(true);
		}
	}

	@EventHandler
	public void onShoot(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		GamePlayer gP = gamePlayerList.get(e.getPlayer().getName());

		Gun g = null;

		if (p.getItemInHand().hasItemMeta()) {
			if (gunList.containsKey(e.getPlayer().getItemInHand().getItemMeta()
					.getDisplayName())) {
				g = gunList.get(e.getPlayer().getItemInHand().getItemMeta()
						.getDisplayName());
			} else {
				return;
			}
		}

		if (p.getItemInHand().hasItemMeta()) {

			if (!engine.isInProgress())
				return;

			if (e.getAction() == Action.RIGHT_CLICK_AIR) {
				if (timeout.containsKey(p.getName())) {
					if (timeout.get(p.getName()).longValue() > System
							.currentTimeMillis())
						return;
					else
						timeout.put(p.getName(),
								System.currentTimeMillis() + g.getTimeOut());
				} else {
					timeout.put(p.getName(),
							System.currentTimeMillis() + g.getTimeOut());
				}

				Location loc = p
						.getEyeLocation()
						.toVector()
						.add(p.getLocation().getDirection().multiply(2))
						.toLocation(p.getWorld(), p.getLocation().getYaw(),
								p.getLocation().getPitch());
				int velocity = g.getVelocity();
				int num = g.getNumberBulletsToFire();
				int damage = g.getDamage();
				int spray = g.getSpray();
				if (gP.isZoomedIn())
					spray -= g.getSpray()/2;
				Random r = new Random();
				for (int i = 0; i < num; i++) {
					Snowball snow = p.getWorld().spawn(
							loc.add(r.nextDouble() * spray, 0, r.nextDouble() * spray),
							Snowball.class);
					snow.setMetadata(
							"dv",
							new FixedMetadataValue(this, Integer
									.valueOf(damage)));
					snow.setMetadata("team", new FixedMetadataValue(this, gP
							.getTeam().getTeamName()));
					snow.setShooter(p);
					snow.setVelocity(p.getEyeLocation().getDirection()
							.multiply(velocity));
				}
				int amt = e.getPlayer().getInventory().getItem(1).getAmount();
				if (amt > 0)
					e.getPlayer().getInventory().getItem(1).setAmount(amt - 1);
				else
					e.getPlayer().getInventory().getItem(1)
							.setType(Material.AIR);
			} else if (e.getAction() == Action.LEFT_CLICK_AIR) {
				if (g != null)
					gP.toggleZoom(g.getZoomMod());
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK
					|| e.getAction() == Action.LEFT_CLICK_BLOCK) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void damageEvent(EntityDamageByEntityEvent e) {

		if (!engine.isInProgress())
			return;

		if (!(e.getDamager() instanceof Player || e.getDamager() instanceof Snowball)) {
			e.setCancelled(true);
			return;
		}

		if (e.getEntity() instanceof Player) {
			GamePlayer gP = gamePlayerList.get(((Player) e.getEntity())
					.getName());
			if (gP.isInvincible()) {
				getLogger().info("invincible error bitch");
				return;
			}
		}

		if (e.getDamager() instanceof Snowball) {
			int damage = e.getDamager().getMetadata("dv").get(0).asInt();
			e.setDamage(damage);
			if (e.getEntity() instanceof Player) {
				String snowBallTeam = e.getDamager().getMetadata("team").get(0)
						.asString();
				String shooterTeam = e.getEntity().getMetadata("team").get(0)
						.asString();
				if (snowBallTeam.equalsIgnoreCase(shooterTeam)) {
					getLogger().info("Friendly fire.");
					return;
				}
			}
		}
		e.getEntity()
				.getLocation()
				.getWorld()
				.playEffect(e.getEntity().getLocation(), Effect.STEP_SOUND, 55,
						30);
	}

	@EventHandler
	public void deathHandler(EntityDeathEvent e) {
		if (!engine.isInProgress()) {
			return;
		}

		if (!engine.isInProgress() && e.getEntity() instanceof Player) {
			getLogger().info(
					"Somebody died while the engine wasn't chugging, bitch.");
			return;
		}
		if (e.getEntity().getKiller() instanceof Player) {
			GamePlayer killer = gamePlayerList.get(e.getEntity().getKiller()
					.getName());
			killer.getPlayer().sendMessage("Nice kill dude.");

			killer.addKills();
			killer.addKillStreak();

			killer.getPlayer()
					.getInventory()
					.setItem(8, new ItemStack(Material.WOOL, killer.getKills()));
		}

		if (e.getEntity() instanceof Player) {
			GamePlayer dier = gamePlayerList.get(((Player) e.getEntity())
					.getName());
			dier.getPlayer().sendMessage("Nice death dude.");

			dier.addDeaths();
			dier.resetKillStreak();

			dier.getPlayer().getInventory()
					.setItem(7, new ItemStack(Material.WOOL, dier.getDeaths()));
		}
		e.getDrops().clear();
		e.setDroppedExp(0);
	}

	@EventHandler
	public void onScoreBoardOpenEvent(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void respawnHandler(PlayerRespawnEvent e) {
		if (engine.isInProgress()) {
			Bukkit.createWorld(new WorldCreator(engine.getPlayWorld().getName()));
			Location spawnLoc = engine.getRespawnLocation(e.getPlayer()
					.getName());
			if (spawnLoc != null) {
				e.setRespawnLocation(new Location(Bukkit.getWorld(engine
						.getPlayWorld().getName()), spawnLoc.getX(), spawnLoc
						.getY(), spawnLoc.getZ()));
				engine.getGamePlayer(e.getPlayer().getName()).suitUp();
			} else {
				getLogger()
						.info("Engine is being a dumb shit and returning a null spawn location for the player.");
			}
		} else {
			getLogger().info("respawn not during engine.");
		}
	}

	@EventHandler
	public void onWindowShot(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Snowball) {
			Block b = e.getEntity().getLocation().getBlock();
			breakWindow(b);
			breakWindow(b.getRelative(BlockFace.DOWN));
			breakWindow(b.getRelative(BlockFace.UP));
			breakWindow(b.getRelative(BlockFace.WEST));
			breakWindow(b.getRelative(BlockFace.EAST));
			breakWindow(b.getRelative(BlockFace.EAST_NORTH_EAST));
			breakWindow(b.getRelative(BlockFace.EAST_SOUTH_EAST));
			breakWindow(b.getRelative(BlockFace.WEST_NORTH_WEST));
			breakWindow(b.getRelative(BlockFace.WEST_SOUTH_WEST));
			breakWindow(b.getRelative(BlockFace.NORTH_NORTH_EAST));
			breakWindow(b.getRelative(BlockFace.NORTH_NORTH_WEST));
			breakWindow(b.getRelative(BlockFace.NORTH_WEST));
			breakWindow(b.getRelative(BlockFace.NORTH));
			breakWindow(b.getRelative(BlockFace.SOUTH));
			breakWindow(b.getRelative(BlockFace.SOUTH_SOUTH_EAST));
			breakWindow(b.getRelative(BlockFace.SOUTH_SOUTH_WEST));
			breakWindow(b.getRelative(BlockFace.SOUTH_WEST));
		}
	}

	public void breakWindow(final Block b) {
		if (b.getType() != Material.GLASS) {
			return;
		} else {
			b.breakNaturally();
			b.getLocation().getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, 20, 30);
			breakWindow(b.getRelative(BlockFace.DOWN));
			breakWindow(b.getRelative(BlockFace.UP));
			breakWindow(b.getRelative(BlockFace.NORTH));
			breakWindow(b.getRelative(BlockFace.SOUTH));
			breakWindow(b.getRelative(BlockFace.WEST));
			breakWindow(b.getRelative(BlockFace.EAST));
		}
	}

	public Plugin getPlugin() {
		return this;
	}

}
