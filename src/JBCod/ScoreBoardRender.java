package JBCod;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MinecraftFont;

public class ScoreBoardRender extends MapRenderer {

	private ArrayList<String> textToRender;

	public void render(MapView map, MapCanvas canvas, Player player) {
		
		
		int its = 2;
		canvas.drawText(10*its, 10, MinecraftFont.Font, "NAME: KILLS | DEATHS | KDR");
		for (String text : textToRender) {
			canvas.drawText(10*its, 10, MinecraftFont.Font, text);
			its++;
		}
		player.sendMap(map);
	}
	
	public void sendText(ArrayList<String> text) {
		textToRender = text;
	}
}