# Senior Project: Runescape Minigame

As far as I know, this does not run on linux. I made an effort to get it working, but I was unable to progress very far. These steps are to make it compile and run on windows. There is also the cache for the client that has to be at the root of your (C:) drive. The cache is in the Runescape directory named acheron.cache The compilation must be done in cmd. I tested it in powershell and it did not work. There are also sometimes weird errors with the signlink that are discussed slightly further below, but if you have any issues compiling, let me know and I have ways around all of them. Definitely unfortunate, but I do not have enough experience with different devices to be able to really know upfront what to expect



## Server Compilation
Go to Runescape/Acheron Source and run this command in cmd. 
```    
javac -classpath deps/log4j-1.2.15.jar;deps/jython.jar;deps/xstream.jar;deps/mina.jar;deps/mysql.jar;deps/poi.jar;deps/slf4j.jar;deps/slf4j-nop.jar -d bin src\server\event\*.java src\server\model\items\*.java src\server\model\minigames\*.java src\server\model\npcs\*.java src\server\model\objects\*.java src\server\model\players\*.java src\server\model\players\skills\*.java src\server\model\players\packets\*.java src\server\model\shops\*.java src\server\net\*.java src\server\task\*.java src\server\util\*.java src\server\world\*.java src\server\util\log\*.java src\server\*.java src\server\world\map\*.java
```

## Running the server
Also from Runescape/Acheron Source/ run the command

```
java -Xmx1000m -cp bin;deps/poi.jar;deps/mysql.jar;deps/mina.jar;deps/slf4j.jar;deps/slf4j-nop.jar;deps/jython.jar;log4j-1.2.15.jar; server.Server
```

## Client Compilation

 Go to Runescape/Acheron Client/ and run

```
javac *.java
```

## Running the Client

Also from Runescape/Acheron Client/ run the command

```
java client
```

## Compiling signlink

You might not need to do this, and if you use openJDK then you can use the batch file to compile it. I'm not really sure what it does, but I sometimes got an error that made me need to run the compiler. If there are any weird errors when compiling the other parts, this is most likely the issue. If you have any problems let me know and I should be able to get it running pretty quickly. Do not bother compiling this unless you get errors compiling the other parts.

inside of Runescape/Acheron Client/sign 

```
javac signlink.java
```

You can log in with any username and password other than the ones listed below. Any new character made will not spawn at the minigame. You must click what looks like a spellbook on the very right edge of the screen about halfway down. From there, click the only spell that is not grayed out. It is the first spell. It will teleport you to the survival area.

There are 3 accounts premade to see different aspect of the minigame. All 3 of the accounts have admin privilages and can use the command 
```
::survival value
```
where value is any integer from 0 to max int. This command will give the player value number of survival points. Use to buy things from the shop to test it without playing for survival points.

Account1: has decent stats that are high enough to make it most of the way through easy. This account is wearing gear that existed in the game already.
username: 123
password: 123

Account 2: has high enough stats to wear the tier 1 armor set and weapon that I made, and is currently equipped with them.
username: 1234
password: 1234

Account3: has the second tier gear equipped, and can still die in hard if not played correctly.
username: 12345
password: 12345

My code is scattered a little bit of everywhere, but the bulk of it for the minigame itself is in this.
Everything in this file is my own code based on how they wrote their code previously, which is why there are large switch statements.
```
Runescape\Acheron Source\src\server\model\minigames\Survival.java
```

There are just some fields in that are booleans, and the int for your survival points
```
Runescape\Acheron Source\src\server\model\players\Player.java
```

This file contains two functions that I wrote at line 3216; resetSurvival() and enterSurvival() are for what you would expect. When you enter the portal, the portal calls the enterSurvival function and it begins. When you die or use the exit portal, resetSurvival is called. ctrl+f survival will show where my code is used in this file. It is mostly for preventing you from teleporting out, and making you keep your items when you die.
```
Runescape\Acheron Source\src\server\model\players\PlayerAssistant.java
```

This file is a mess. They have an 1800 line switch statement in it containing every object you can click. The cases between 1192 and 1267 are all the objects for the minigame. They all say what they are in the comments above the case. The healer is also in this file at 2413, and shopkeeper at 2539
```
Runescape\Acheron Source\src\server\model\players\ActionHandler.java
```

This file is where a lot of the actual logic behind the minigame is. when you enter survival, the number of enemies to kill is set as a field in the player.java class. This function takes that and compares it to how many enemies there are to kill, which is assigned the same way. It keeps track of how many are killed and when all enemies have been killed, it awards points to the player, and increments which wave is to spawn. It then calls the spawnWave function 
```
Runescape\Acheron Source\src\server\model\npcs\NPCHandler.java
```

There are a few things in this file for the shop, but I just copied the code they used for the other special shops and replaced it with the information for my shop. ctrl + f survival will show where it is. Its pretty scattered so it will be easier this way
```
Runescape\Acheron Source\src\server\model\shops\ShopAssistant.java
```

This next file I just had to add my items to a bunch of conditions for the level requirements at line 767 to 777
```
Runescape\Acheron Source\src\server\model\items\itemAssistant.java
```

This file creates a new event for the spawnWave
```
Runescape\Acheron Source\src\server\model\players\Client.java
```

To get to the area that has what I added, click the spellbook and the teleport that was mentioned when I started mentioning the accounts earlier in the README. From there, you can click one of 3 portals; easy, medium, and hard from left to right respectively. There is also the shopkeeper than you can spend the survival points that you MUST right click and click trade in order to shop. There is a monk that will restore your health every 10 minutes, and your special attack. There is also an altar that will restore your prayer. Once you click a portal, you will get teleported into the area for the event to start. A few seconds will pass and enemies will spawn. As you kill the waves, you will gain points to spend in the shop. To test the shop there is the ::survival command mentioned above to get points without playing for a while. Once you complete all 10 waves, you are teleported back to the main room again where you can play more, or access any of the other features. There is also a bank chest where you can put the items you start with that you dont want to bring with you. You dont lose items on death unless you log out and back in while in the area. The bank code was done before I got the server, and is terrible. If you use the deposit all button, you will not be able to move for about 15 seconds.
