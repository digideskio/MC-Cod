package JBCod;

import java.util.ArrayList;

@SuppressWarnings("unused")
public abstract class GameMode {
	private GameEngine engine;
	private ArrayList<GamePlayer> playerList;
	
	public abstract boolean scoreChangeEvent(int oldScore, int newScore);
	public abstract void startGame();
	public void setGamePlayerList(ArrayList<GamePlayer> newList) {
		playerList = newList;
	}
	
}
