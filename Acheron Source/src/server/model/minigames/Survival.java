package server.model.minigames;

import server.model.players.Client;
import server.util.Misc;
import server.Server;

public class Survival {
	public int npc;
	private final int[][] easyWAVES = { { 1265, 1265, 1265, 1265, 1265 }, { 74, 74, 74, 74, 74 },
			{ 75, 75, 75, 75, 75 }, { 1153, 1153, 1153, 1153, 1153 }, { 76, 76, 76, 76, 76 }, 
			{ 63, 63, 63, 63, 63 }, { 1218, 1218, 1218, 1218, 1218 }, { 1633, 1633, 1633, 1633, 1633 }, 
			{ 1587, 1587, 1587, 1587, 1587 }, { 111, 111, 111, 111, 111 } };

	private final int[][] mediumWAVES = { { 1587, 1587, 1587, 1587, 1587 }, { 93, 93, 93, 93, 93 }, 
			{ 111, 111, 111, 111, 111}, { 1616, 1616, 1616, 1616, 1616 }, { 1637, 1637, 1637, 1637, 1637 }, 
			{ 1582, 1582, 1582, 1582, 1582 }, { 1624, 1624, 1624, 1624, 1624 }, { 1558, 1558, 1558, 1558, 1558 },
			{ 1961, 1961, 1961, 1961, 1961 }, { 1565, 1565, 1565, 1565, 1565 } };

	private final int[][] hardWAVES = { { 2455, 2455, 2455, 2455, 2455 }, { 1615, 1615, 1615, 1615, 1615 },
			{2591, 2591, 2591, 2591, 2591}, {2604, 2604, 2604, 2604, 2604 }, {2614, 2614, 2614, 2614, 2614 },
			{2030, 2030, 2030, 2030, 2030 }, {2026, 2026, 2026, 2026, 2026 }, {1460, 1460, 1460, 1460, 1460 },
			{1157, 1157, 1157, 1157, 1157 }, {84, 84, 84, 84, 84 } };

	private int[][] coordinates = { { 2772, 9630 }, { 2762, 9637 }, { 2767, 9638 }, { 2763, 9629 }, { 2772, 9634 } };

	// 2743 = 360, 2627 = 22, 2630 = 45, 2631 = 90, 2741 = 180
	public void spawnWave(Client c) {

		if (c.inSurvival) {
			if(c.waveID == 10)
				endSurvival(c);
			c.hasCollected = false;
			if (c != null) {
				if (c.waveID >= easyWAVES.length) {
					c.waveID = 0;
					return;
				}
				if (c.waveID < 0) {
					return;
				}
				int npcAmount = easyWAVES[c.waveID].length;
				for (int j = 0; j < npcAmount-1; j++) {
					if(c.inEasySurvival){
						npc = easyWAVES[c.waveID][j];
					} else if(c.inMediumSurvival){
						npc = mediumWAVES[c.waveID][j];
					} else if(c.inHardSurvival){
						npc = hardWAVES[c.waveID][j];
					}
					int X = coordinates[j][0];
					int Y = coordinates[j][1];
					int H = c.heightLevel;
					int hp = getHp(npc);
					int max = getMax(npc);
					int atk = getAtk(npc);
					int def = getDef(npc);
					if(!c.inSurvival)
						break;
					Server.npcHandler.spawnNpc(c, npc, X, Y, H, 1, hp, max, atk, def, true, false);
				}
				if(c.inEasySurvival){
					npc = easyWAVES[c.waveID][0];
					} else if(c.inMediumSurvival){
						npc = mediumWAVES[c.waveID][0];
					} else if(c.inHardSurvival){
						npc = hardWAVES[c.waveID][0];
					}
				int X = coordinates[4][0];
				int Y = coordinates[4][1];
				int H = c.heightLevel;
				int hp = getHp(npc)*2;
				int max = getMax(npc)*2;
				int atk = getAtk(npc)*2;
				int def = getDef(npc)/4;
				Server.npcHandler.spawnNpc(c, npc, X, Y, H, 1, hp, max, atk, def, true, true);
				c.survivalToKill = npcAmount;
				c.survivalKilled = 0;
			}
		}
	}

	public void endSurvival(Client c){
		String difficultyString = difficulty(c);
		c.sendMessage("You have completed survival on " + difficultyString
			+ " and have received " + getPoints(10, c) + " bonus points!");
		c.survivalPoints += getPoints(10, c);
		c.inEasySurvival = false;
		c.inHardSurvival = false;
		c.inMediumSurvival = false;
		c.inSurvival = false;
		c.getPA().movePlayer(2750,9638,0);
	}

	public String difficulty(Client c){
		if(c.inEasySurvival)
			return "easy";
		if(c.inMediumSurvival)
			return "medium";
		if(c.inHardSurvival)
			return "hard";
		return "null";
	}
	public int getPoints(int wave, Client c){
		switch(wave) {
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			if(c.inEasySurvival)
				return 4;
			if(c.inMediumSurvival)
				return 6;
			if(c.inHardSurvival)
				return 10;
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			if(c.inEasySurvival)
				return 6;
			if(c.inMediumSurvival)
				return 10;
			if(c.inHardSurvival)
				return 14;
			case 10:
			if(c.inEasySurvival)
				return 15;
			if(c.inMediumSurvival)
				return 30;
			if(c.inHardSurvival)
				return 45;
		}
		return 0;
	}

	public int getHp(int npc) {
		switch (npc) {
			case 1265:
				return 50;
			case 74:
				return 24;
			case 75:
				return 24;
			case 1153:
				return 40;
			case 63:
				return 35;
			case 76:
				return 30;
			case 1633:
				return 45;
			case 1218:
				return 50;
			case 1587:
				return 60;
			case 111:
				return 70;
			case 93://medium
				return 59;
			case 1616:
				return 75;
			case 1637:
				return 75;
			case 1582:
				return 111;
			case 1624:
				return 130;
			case 1558:
				return 72;
			case 1961:
				return 90;
			case 1565:
				return 100;
			case 2455:// hard
				return 120;
			case 1615:
				return 150;
			case 2591:
				return 100;
			case 2604:
				return 120;
			case 2614:
				return 200;
			case 2030:
				return 100;
			case 2026:
				return 100;
			case 1460:
				return 130;
			case 1157:
				return 171;
			case 84:
				return 200;
		}
		return 1;
	}

	public int getMax(int npc) {
		switch (npc) {
			case 1265:
			case 74:
			case 75:
			case 1153:
			case 76:
			case 63:
				return 3;
			case 1633:
				return 4;
			case 1218:
				return 5;
			case 1587:
				return 6;
			case 111:
				return 7;
			case 93://medium
				return 5;
			case 1616:
				return 5;
			case 1637:
				return 6;
			case 1582:
				return 11;
			case 1624:
				return 10;
			case 1558:
				return 10;
			case 1961:
				return 24;
			case 1565:
				return 21;
			case 2455:// hard
				return 8;
			case 1615:
				return 8;
			case 2591:
				return 13;
			case 2604:
				return 17;
			case 2614:
				return 19;
			case 2030:
				return 25;
			case 2026:
				return 29;
			case 1460:
				return 14;
			case 1157:
				return 12;
			case 84:
				return 25;
		}
		return 5;
	}

	public int getAtk(int npc) {
		switch (npc) {
			case 1265:
				return 5;
			case 74:
				return 17;
			case 75:
				return 19;
			case 1153:
				return 20;
			case 76:
				return 21;
			case 63:
				return 30;
			case 1633:
				return 52;
			case 1218:
				return 30;
			case 1587:
				return 30;
			case 111:
				return 40;
			case 93://medium
				return 32;
			case 1616:
				return 30;
			case 1637:
				return 45;
			case 1582:
				return 65;
			case 1624:
				return 120;
			case 1558:
				return 100;
			case 1961:
				return 90;
			case 1565:
				return 100;
			case 2455:// hard
				return 68;
			case 1615:
				return 97;
			case 2591:
				return 120;
			case 2604:
				return 140;
			case 2614:
				return 200;
			case 2030:
				return 100;
			case 2026:
				return 100;
			case 1460:
				return 130;
			case 1157:
				return 110;
			case 84:
				return 145;
		}
		return 100;
	}

	public int getDef(int npc) {
		switch (npc) {
			case 1265:
				return 1;
			case 74:
				return 17;
			case 75:
				return 17;
			case 1153:
				return 17;
			case 76:
				return 17;
			case 63:
				return 30;
			case 1633:
				return 22;
			case 1218:
				return 30;
			case 1587:
				return 30;
			case 111:
				return 40;
			case 93://medium
				return 36;
			case 1616:
				return 75;
			case 1637:
				return 120;
			case 1582:
				return 65;
			case 1624:
				return 40;
			case 1558:
				return 70;
			case 1961:
				return 90;
			case 1565:
				return 120;
			case 2455:// hard
				return 71;
			case 1615:
				return 135;
			case 2591:
				return 80;
			case 2604:
				return 100;
			case 2614:
				return 190;
			case 2030:
				return 100;
			case 2026:
				return 120;
			case 1460:
				return 200;
			case 1157:
				return 110;
			case 84:
				return 200;
		}
		return 100;
	}
}
