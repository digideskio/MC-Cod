package JBCod;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;

public class Team {
	private String name;
	private Color teamColor = null;
	private ArrayList<GamePlayer> members = new ArrayList<GamePlayer>(5);
	private Location spawnLocation;
	private int kills = 0;

	/**
	 * Make a new team.
	 * 
	 * @param name
	 *            The name for the team
	 */
	public Team(String name) {
		this.name = name;
	}

	/**
	 * Make a new team.
	 * 
	 * @param name
	 *            The name for the team.
	 * @param teamColor
	 *            The team color.
	 */
	public Team(String name, Color col) {
		this.name = name;
		this.teamColor = col;
	}

	/**
	 * Get the team color.
	 * 
	 * @return The team's color.
	 */
	public Color getColor() {
		return teamColor;
	}

	/**
	 * Get the team name.
	 * 
	 * @return The name of the team.
	 */
	public String getTeamName() {
		return name;
	}

	/**
	 * Set a new color for the armor.
	 * 
	 * @param newColor
	 *            New color for the team.
	 */
	public void setColor(Color newColor) {
		this.teamColor = newColor;
	}

	/**
	 * Set a new name for the team.
	 * 
	 * @param newName
	 *            New name for the team.
	 */
	public void setName(String newName) {
		this.name = newName;
	}

	/**
	 * Add a member to the team.
	 * 
	 * @param player
	 *            The player to add to the team.
	 */
	public void addMember(GamePlayer player) {
		members.add(player);
	}

	/**
	 * Get the number of members on the team.
	 * 
	 * @return The number of players on the team.
	 */
	public int numberOfMembers() {
		return members.size();
	}

	/**
	 * Check if a player is on the team.
	 * 
	 * @param who
	 *            The player to check.
	 * @return Whether or not the player is on the team.
	 */
	public boolean isMember(GamePlayer who) {
		return members.contains(who);
	}

	public Location getSpawnLocation() {
		return spawnLocation;
	}

	public void setSpawnLocation(Location spawnLocation) {
		this.spawnLocation = spawnLocation;
	}

	public int getKills() {
		return kills;
	}

	public void addKill() {
		this.kills++;
	}
}
