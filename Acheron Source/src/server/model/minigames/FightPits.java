package server.model.minigames;

import server.model.players.Client;
import server.util.Misc;
import server.Server;

/**
 * @author Sanity
 */

public class FightPits {

	public int[] playerInPits = new int[200];

	private int GAME_TIMER = 140;
	private int GAME_START_TIMER = 120;

	@SuppressWarnings("unused")
	private int gameTime = -1;
	private int gameStartTimer = 120;
	@SuppressWarnings("unused")
	private int properTimer = 0;
	public int mobsRemaining = 0;
	public boolean gameOn = false;

	public String pitsChampion = "Nobody";

	public void process() {
		if (gameStartTimer > 0) {
			gameStartTimer--;
			//if(gameStartTimer % 30 == 0) {
			updateWaitRoom();
			//}
		}
		if (gameStartTimer == 0 && mobsRemaining <= 1) {
			startGame();
		}
		if (mobsRemaining == 1) {
			endPitsGame(getLastPlayerName());
		}
	}

	@SuppressWarnings("static-access")
	public String getLastPlayerName() {
		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] > 0)
				return Server.playerHandler.players[playerInPits[j]].playerName;
		}
		return "Nobody";
	}

	@SuppressWarnings("static-access")
	public void updateWaitRoom() {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client) Server.playerHandler.players[j];
				if (c.getPA().inPitsWait() || c.inPits) {
					if (gameOn == false) {
						c.getPA().sendFrame126("Next Game Begins In : " +gameStartTimer, 2806);
						//c.getPA().sendFrame126("Champion: JalYt-Ket-" +Misc.capitalize(pitsChampion), 2805);
						c.getPA().sendFrame36(560, 1);
						c.getPA().walkableInterface(2804);
					}
					if (gameOn == true) {
						c.getPA().sendFrame126("Foes Remaining: " +mobsRemaining, 2806);
						//c.getPA().sendFrame126("Champion: JalYt-Ket-" +Misc.capitalize(pitsChampion), 2805);
						c.getPA().sendFrame36(560, 1);
						c.getPA().walkableInterface(2804);
				}
			}
		}
		}
	}
		
	
//		int displayTime = gameStartTimer;
//		//System.out.println("time: " + gameStartTimer);
//			for (int j = 0; j < Server.playerHandler.players.length; j++) {
//				if (Server.playerHandler.players[j] != null) {
//					Client c = (Client) Server.playerHandler.players[j];
//					if (c.getPA().inPitsWait() || c.inPits) {
//						c.getPA().sendFrame126("Survival", 15894);
//						c.getPA().sendFrame126("Next Game In: " + displayTime + " seconds", 15895);
//						c.getPA().sendFrame126("Current Points: " + c.survivalPoints, 15897);
//						//c.getPA().sendFrame126("(Need 1 to 25 players)", 21008);
//						//c.getPA().sendFrame126("Points: " + c.pcPoints + "", 21009);
//						//c.getPA().sendQuest("Survival", 15894);
////						if (!gameOn) {
////								c.getPA().sendQuest("Next Game In : " + displayTime + " seconds.", 15895);
////						} else {
////							c.getPA().sendQuest("Game in progress", 15895);
////						}
////						c.getPA().sendQuest("Current Points:" + c.survivalPoints, 15897);
//						c.getPA().sendQuest("Foes Remaining: " + mobsRemaining, 15898);
//						c.getPA().sendQuest("", 15899);
//						c.getPA().sendQuest("", 15896);
//						c.getPA().sendQuest("", 15900);
//						c.getPA().sendQuest("", 15901);
//						c.getPA().sendQuest("", 15902);
//						c.getPA().sendQuest("", 15903);
//						c.getPA().sendQuest("", 15904);
//						c.getPA().sendQuest("", 15905);
//						c.getPA().sendQuest("", 15906);
//						c.getPA().walkableInterface(15892);
//
//					}
//				}
//			}
//	}

	@SuppressWarnings("static-access")
	public void startGame() {
		if (getWaitAmount() < 2) {
			gameStartTimer = GAME_START_TIMER;
			// System.out.println("Unable to start fight pits game due to lack of
			// players.");/
			return;
		}
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client) Server.playerHandler.players[j];
				if (c.getPA().inPitsWait())
					addToPitsGame(j);
			}
		}
		System.out.println("Fight Pits game started.");
		gameStartTimer = GAME_START_TIMER + GAME_TIMER;
	}

	@SuppressWarnings("static-access")
	public int getWaitAmount() {
		int count = 0;
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Client c = (Client) Server.playerHandler.players[j];
				if (c.getPA().inPitsWait())
					count++;
			}
		}
		return count;
	}

	public void removePlayerFromPits(int playerId) {
		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] == playerId) {
				@SuppressWarnings("static-access")
				Client c = (Client) Server.playerHandler.players[playerInPits[j]];
				c.getPA().startTeleport(2399, 5173, 0, "modern");
				playerInPits[j] = -1;
				mobsRemaining--;
				c.inPits = false;
				break;
			}
		}
	}

	@SuppressWarnings("static-access")
	public void endPitsGame(String champion) {
		@SuppressWarnings("unused")
		boolean giveReward = false;
		if (mobsRemaining == 1)
			giveReward = true;
		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] < 0)
				continue;
			if (Server.playerHandler.players[playerInPits[j]] == null)
				continue;
			Client c = (Client) Server.playerHandler.players[playerInPits[j]];
			c.getPA().startTeleport(2399, 5173, 0, "modern");
			c.inPits = false;
		}
		playerInPits = new int[200];
		pitsChampion = champion;
		mobsRemaining = 0;
		pitsSlot = 0;
		gameStartTimer = GAME_START_TIMER;
		System.out.println("Fight Pits game ended.");
	}

	private int pitsSlot = 0;

	@SuppressWarnings("static-access")
	public void addToPitsGame(int playerId) {
		if (Server.playerHandler.players[playerId] == null)
			return;
		mobsRemaining++;
		Client c = (Client) Server.playerHandler.players[playerId];
		playerInPits[pitsSlot++] = playerId;
		c.getPA().startTeleport(2392 + Misc.random(12), 5139 + Misc.random(25), 0, "modern");
		c.inPits = true;
		c.getDH().talk(588, "", "FIGHT!", "", "", 2617);
		gameOn = true;
	}
}