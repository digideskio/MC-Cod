package JBCod;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;

public class GameEngine implements Listener {

	private ArrayList<GamePlayer> playerList;
	private Team teamOne;
	private Team teamTwo;
	private World playWorld;
	private int currentTime = 20;
	private int mainThreadTask;
	private boolean inProgress = false;

	public GameEngine(int gameLength) {
		this.currentTime = gameLength;
	}

	public GameEngine(int gameLength, String playWorld) {
		this.currentTime = gameLength;
		this.playWorld = Bukkit.getWorld(playWorld);
	}

	public GameEngine(int gameLength, World playWorld) {
		this.currentTime = gameLength;
		this.playWorld = playWorld;
	}

	public GameEngine() {

	}

	public void setPlayerList(ArrayList<GamePlayer> gp) {
		Collections.shuffle(gp);
		this.playerList = gp;
	}

	public void startGameManager(ArrayList<GamePlayer> gp) {
		Collections.shuffle(gp);
		this.playerList = gp;
		this.teamOne = new Team("Red Team", Color.RED);
		teamOne.setSpawnLocation(new Location(playWorld, 0, 5, 0));
		this.teamTwo = new Team("Blue Team", Color.BLUE);
		teamTwo.setSpawnLocation(new Location(playWorld, 10, 5, 10));
		this.playWorld = Bukkit.getWorld("playworld");
		boolean lastTeam = false;
		for (GamePlayer p : gp) {
			if (lastTeam) {
				teamOne.addMember(p);
				p.changeTeam(teamOne);
			} else {
				teamTwo.addMember(p);
				p.changeTeam(teamTwo);
			}
			lastTeam = !lastTeam;
			p.getPlayer().teleport(new Location(playWorld, 0, 5, 0));
			p.suitUp();
			p.getPlayer().setMetadata("team", new FixedMetadataValue(new test().getPlugin(), p.getTeam().getTeamName()));
		}
		startGame();
	}

	public ArrayList<GamePlayer> getPlayerList() {
		return playerList;
	}

	private void startGame() {
		inProgress = true;
		mainThreadTask = Bukkit
				.getServer()
				.getScheduler()
				.scheduleSyncRepeatingTask(new test().getPlugin(),
						new Runnable() {

							@Override
							public void run() {
								for (GamePlayer p : playerList) {
									p.getPlayer().setLevel(currentTime);
								}
								currentTime--;
								if (currentTime < 0) {
									cleanUpEngine();
									Bukkit.getScheduler().cancelTask(
											mainThreadTask);
									return;
								}
								playWorld.setTime(0);
							}

						}, 20L, 20L);
	}

	private void cleanUpEngine() {
		for (GamePlayer p : playerList) {
			p.clearInventory();
			p.ejectFromGame();
		}
		inProgress = false;
	}

	public String toString() {
		return "hello!";
	}

	public Team getTeamOne() {
		return teamOne;
	}

	public Team getTeamTwo() {
		return teamTwo;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public Location getRespawnLocation(GamePlayer p) {
		if (teamOne.isMember(p)) {
			return teamOne.getSpawnLocation();
		} else {
			return teamTwo.getSpawnLocation();
		}
	}
	
	public Location getRespawnLocation(String p) {
		GamePlayer gP = this.getGamePlayer(p);
		if (teamOne.isMember(gP)) {
			return teamOne.getSpawnLocation();
		} else {
			return teamTwo.getSpawnLocation();
		}
	}
	
	public World getPlayWorld () {
		return playWorld;
	}

	public GamePlayer getGamePlayer(String playerName) {
		for (GamePlayer gP : playerList) {
			if (gP.getPlayer().getName().equalsIgnoreCase(playerName))
				return gP;
		}
		return null;
	}
}
