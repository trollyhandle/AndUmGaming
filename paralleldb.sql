SET SQL_SAFE_UPDATES=0;

DROP DATABASE parallelU;
CREATE DATABASE parallelU;
USE parallelU;

#SET FOREIGN_KEY_CHECKS=1;
# Tables


CREATE TABLE player (
	playerID INT PRIMARY KEY AUTO_INCREMENT, 
    firstname VARCHAR(31), 
    lastname VARCHAR(31), 
    username VARCHAR(31) UNIQUE, 
    pword VARCHAR(31), 
    email VARCHAR(31));

CREATE TABLE game (
	gameID INT PRIMARY KEY AUTO_INCREMENT,
    qty_players INT);
CREATE TABLE playing_in_game (
	playerID INT,
	FOREIGN KEY (playerID) REFERENCES player (playerID),
    gameID INT,
    FOREIGN KEY (gameID) REFERENCES game (gameID));
CREATE TABLE turn (
	turnID INT PRIMARY KEY, # Auto incrementing turns don't make sense with multiple games
    gameID INT,
    FOREIGN KEY (gameID) REFERENCES game (gameID),
    playerID INT,
    FOREIGN KEY (playerID) REFERENCES player (playerID),
	dice_result INT);
CREATE TABLE dev_card (
	dev_cardID INT PRIMARY KEY, # 1 = Knight, #2 = VP, #3 = Monopoly, #4 = Road Building, #5 = Year of Plenty
    title varchar(31) UNIQUE,
    card_text varchar(255) UNIQUE);

CREATE TABLE buy_dev_card (
	playerID INT,
    FOREIGN KEY (playerID) REFERENCES player (playerID),
    turnID INT,
	FOREIGN KEY (turnID) REFERENCES turn (turnID),
    dev_cardID INT, # Once used, is -1 = Knight, -2 = VP, -3 = Monopoly, -4 = Road Building, -5 = Year of Plenty
	FOREIGN KEY (dev_cardID) REFERENCES dev_card (dev_cardID));
CREATE TABLE tile (
	tileID INT PRIMARY KEY AUTO_INCREMENT,
    resource_type INT, # 0 = Wood, 1 = Brick, 2 = Sheep, 3 = Wheat, 4 = Ore, 5 = Desert
    dice_number INT);
CREATE TABLE build (
    turnID INT,
    FOREIGN KEY (turnID) REFERENCES turn (turnID),
    EVID INT, # location of edge/vertex that identifies road/settlement
    # Edge/Vertex ID is three digits: 0 before vertices and 1 in front of edges on catan map
    building_type INT); # 0 = Road, 1 = Settlement, 2 = City
CREATE TABLE player_resource_data (
	turnID INT, 
	FOREIGN KEY (turnID) REFERENCES turn (turnID),
    playerID INT,
    FOREIGN KEY (playerID) REFERENCES player (playerID), # other players receive resources on opponent's turns
    resource_type INT, # 0 = Wood, 1 = Brick, 2 = Sheep, 3 = Wheat, 4 = Ore
    qty INT); # Income qty. Can be negative when players pay for things

# 5 main dev card texts
INSERT INTO dev_card (dev_cardID, title, card_text)
VALUES (1, "Knight", "Move the robber. Steal 1 resource card from the owner of an adjacent settlement or city."),
	(2, "University", "1 Victory Point!"),
    (3, "Monopoly", "When you play this card, announce 1 type of resource. All other players must give you all their resources of that type."),
    (4, "Road Building", "Place 2 new roads as if you had just built them"),
    (5, "Year of Plenty", "Take any 2 resources from the bank. Add them to you hadn. They can be 2 of the same resource or 2 different resources.");

INSERT INTO dev_card (dev_cardID) VALUES (0), (-1), (-2), (-3), (-4), (-5); # For when cards are used, 0 to record when robber hits

# User instantiations
INSERT INTO player (firstname, lastname, username, pword, email) 
VALUES ("Abraham", "Lincoln", "alincoln", "4score7", "emancipation@proclamation.com"),
("Amadeus", "Mozart", "amozart", "TeamEdward", "classical@music.com"),
("Thomas", "Jefferson", "tjefferson", "Deism", "founding@father.com"),
("Benjamin", "Franklin", "bfranklin", "$hundred$dollars", "discovered@electricity.com"),
("Theodore", "Roosevelte", "troosevelte", "BigStickDiplomacy", "national@parks.com");



############
## GAME 1 ##
############

# Create Game
INSERT INTO game (qty_players) VALUES (3);

# Insert all three players into the first game
INSERT INTO playing_in_game (gameID, playerID)
VALUES (1, 1),
	(1, 2),
	(1, 3);
    
# Insert board data that came from an actual game
INSERT INTO tile (resource_type, dice_number)
VALUES (0, 8),
	(4, 4),
	(2, 11),
    (2, 10),
    (5, 0),
    (3, 3),
    (1, 12),
    (2, 5),
    (1, 9),
    (4, 11),
    (0, 6),
    (4, 9),
    (3, 2),
    (0, 4),
    (3, 5),
    (2, 10),
    (3, 6),
    (1, 3),
    (0, 8);

##### INITIAL GAME SETUP #####

# Initial turn setup, player 1 is NULL because we need a player, no result
INSERT INTO turn (turnID, gameID, playerID, dice_result)
VALUES 
(1, 1, 1, 0),
(2, 1, 2, 0),
(3, 1, 3, 0),
(4, 1, 3, 0),
(5, 1, 2, 0),
(6, 1, 1, 0); # turn 6 

# Players choose where their settlements and roads will go 
INSERT INTO build (turnID, EVID, building_type)
VALUES 
	(1, 35,  1), 	# build 1st player's settlement
	(1, 146, 0), 	# build 1st player's road
    (2, 41,  1), 	# build 2nd player's settlement 
    (2, 156, 0), 	# build 2nd player's road
    (3, 18,  1),	# build 3rd player's settlement 
    (3, 124, 0),	# build 3rd player's road
    (4, 44,  1),	# build 3rd player's 2nd settlement 
    (4, 160, 0),	# build 3rd player's 2nd road
    (5, 33,  1),	# build 2nd player's 2nd settlement
	(5, 136, 0),	# build 2nd player's 2nd road
    (6, 30,  1),	# build 1st player's 2nd settlement
    (6, 142, 0);	# build 1st player's 2nd road

# Insert beginning values
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(4, 3, 0, 1),
(4, 3, 2, 1),
(4, 3, 3, 1),
(5, 2, 0, 1),
(5, 2, 3, 1),
(5, 2, 4, 1),
(6, 1, 0, 1),
(6, 1, 1, 1),
(6, 1, 3, 1);



# Turn 7 (actual first turn)
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (7, 1, 1, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(7, 1, 0, 1), # Income
(7, 2, 0, 1),
(7, 2, 3, 1),

(7, 1, 0, -1), # Build Road for player 1
(7, 1, 1, -1); 
INSERT INTO build (turnID, EVID, building_type) VALUES (7, 135, 0); 	# build 1st player's road



# Turn 8 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (8, 1, 2, 10);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES
(8, 1, 2, 1), # Income
(8, 3, 2, 2);



# Turn 9 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (9, 1, 3, 11);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES
(9, 2, 4, 1); # Income



# Turn 10 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (10, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES
(10, 1, 4, 1), # p1 Stole ore with Robber 
(10, 2, 4, -1), # p2 Lost ore with Robber

(10, 1, 2, -1), # Buy Dev Card
(10, 1, 3, -1),
(10, 1, 4, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (10, 1, 0); # p1 uses robber
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (10, 1, 1); # first player buys dev card (knight)



# Turn 11 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (11, 1, 2, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES
(11, 2, 0, 1), # Stole wood with Robber 
(11, 1, 0, -1); # Lost wood with Robber
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (11, 2, 0); # p2 uses robber



# Turn 12 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (12, 1, 3, 5);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(12, 2, 3, 1), #Income
(12, 3, 2, 1),
(12, 3, 3, 1);



# Turn 13 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (13, 1, 1, 2);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(13, 1, 3, 1), # Income
(13, 1, 2, 1), # Stole wood with Robber 
(13, 3, 2, -1); # Lost wood with Robber

INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (13, 1, -1);



# Turn 14 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (14, 1, 2, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(14, 1, 0, 1), # Income
(14, 2, 0, 1), 

(14, 2, 0, -4), # Lost 4 wood thru 4:1
(14, 2, 1, 1); # Gained 1 brick thru 4:1



# Turn 15 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (15, 1, 3, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(15, 1, 0, 1), # Income
(15, 2, 0, 1); 



# Turn 16 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (16, 1, 1, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(16, 1, 0, 1),
(16, 2, 0, 1),
(16, 2, 3, 1);



# Turn 17 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (17, 1, 2, 10);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(17, 1, 2, 1),
(17, 3, 2, 2),

(17, 2, 0, -1), # Build Road for player 2
(17, 2, 1, -1); 
INSERT INTO build (turnID, EVID, building_type) VALUES (17, 128, 0); 	# build 2nd player's road


# Turn 18 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (18, 1, 3, 8);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(18, 3, 4, 1), #get ore 4:1
(18, 3, 2, -4), # pay 4 sheep for 4:1

(18, 3, 2, -1), # Buy Dev Card
(18, 3, 3, -1),
(18, 3, 4, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (18, 3, 1); # 3rd player gets a knight



# Turn 19 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (19, 1, 1, 5); # No resources



# Turn 20 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (20, 1, 2, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(20, 1, 0, 1), #Income
(20, 2, 0, 1),

(20, 2, 1, 1), # Trade 4:1 to brick
(20, 2, 3, -4), # Trade 4:1 from wheat
(20, 2, 0, -1), # 2nd player Build road
(20, 2, 1, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (20, 120, 0); 	# build 2nd player's road



# Turn 21 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (21, 1, 3, 8); # No income this turn



# Turn 22 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (22, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(22, 1, 0, 1), # p1 steals wood from p2 with robber
(22, 2, 0, -1),
(22, 1, 1, 1), # p1 trades 4:1 W->B
(22, 1, 0, -4),
(22, 1, 0, -1), # p1 buys a house
(22, 1, 1, -1),
(22, 1, 2, -1),
(22, 1, 3, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (22, 1, 0); # p1 robs p2
INSERT INTO build (turnID, EVID, building_type) VALUES (22, 20, 1); 	# build p1's house



# Turn 23 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (23, 1, 2, 3);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES (23, 2, 1, 1); # Income



# Turn 24 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (24, 1, 3, 2);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(24, 1, 3, 1), # Income
(24, 3, 2, 1), # Knight 3rd steals sheep from 1st
(24, 1, 2, -1); 
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (24, 3, -1); # p3 plays knight 


# Turn 25 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (25, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(25, 1, 2, 1), # Robber, 1st steals sheep from 3rd player
(25, 3, 2, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (25, 1, 0); # p1 robs sheep from p3



# Turn 26 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (26, 1, 2, 11);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(26, 1, 4, 1), #Income
(26, 2, 4, 1);



# Turn 27 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (27, 1, 3, 9);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(27, 1, 1, 2), #Income
(27, 1, 4, 1),
(27, 3, 1, 1),

(27, 3, 0, -1), # 2nd player Build road
(27, 3, 1, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (27, 123, 0); 	# build 2nd player's road



# Turn 28
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (28, 1, 1, 12); # No income



# Turn 29 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (29, 1, 2, 5);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(29, 3, 2, 1); #Income

# Turn 30 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (30, 1, 3, 11);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(30, 1, 4, 1), # Income 
(30, 2, 4, 1);

# Turn 31 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (31, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(31, 1, 0, 1), #4:1 1st player trades 4 ore to 1 wood
(31, 1, 4, -4),
(31, 1, 4, 1), # 1st player robber steals ore from 2nd
(31, 2, 4, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (31, 1, 0); # 1st player steals ore from 2nd player



# Turn 32 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (32, 1, 2, 5);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(32, 2, 3, 1), #income
(32, 3, 2, 1),
(32, 3, 3, 1);


# Turn 33 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (33, 1, 3, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(33, 1, 0, 1), # Income
(33, 2, 0, 1),
(33, 2, 3, 1),

(33, 2, 2, 1), # 3rd player trades Sheep for 2nd player's ore 
(33, 3, 4, 1),
(33, 2, 4, -1),
(33, 3, 2, -1),


(33, 3, 2, -1), # 3rd player gets a dev card
(33, 3, 3, -1),
(33, 3, 4, -1);

INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (33, 3, 1); # 3rd player buys knight



# Turn 34 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (34, 1, 1, 10);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(34, 1, 2, 1), # income
(34, 3, 2, 2),

(34, 1, 0, -2), # p1 buys 2 roads
(34, 1, 1, -2);
INSERT INTO build (turnID, EVID, building_type) VALUES (34, 150, 0); 	# build 1st player's road
INSERT INTO build (turnID, EVID, building_type) VALUES (34, 155, 0); 	# build 1st player's road



# Turn 35 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (35, 1, 2, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(35, 2, 3, 1), # P2 uses robber to steal wheat from P1
(35, 1, 3, -1),

(35, 2, 0, -1), # p2 buys a house
(35, 2, 1, -1),
(35, 2, 2, -1),
(35, 2, 3, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (35, 2, 0); #p2 robs sheep from p1 
INSERT INTO build (turnID, EVID, building_type) VALUES (35, 11, 1); 	# build 2nd player's house



# Turn 36 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (36, 1, 3, 10);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(36, 1, 2, 1), # income
(36, 3, 2, 2),

(36, 1, 2, -1), #knight, p1 takes sheep from p3
(36, 3, 2, 1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (36, 3, -1); # 3rd player plays knight



# Turn 37 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (37, 1, 1, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(37, 2, 3, 1); # income



# Turn 38 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (38, 1, 2, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(38, 2, 3, 1); # income



# Turn 39 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (39, 1, 3, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(39, 1, 0, 1), # Income
(39, 2, 0, 1),
(39, 2, 4, 1);



# Turn 40 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (40, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES
(40, 1, 3, 1), # p2 steals wheat from p1
(40, 2, 3, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (40, 1, 0); # p1 robs wheat from p2 



# Turn 41 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (41, 1, 2, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(41, 1, 0, 1), # Income
(41, 2, 0, 1),
(41, 2, 3, 1),

(41, 2, 1, 1), # p2 trades 4W -> 1B
(41, 2, 3, -4),

(41, 2, 0, -1), # build road for 2nd player
(41, 2, 1, -1); 
INSERT INTO build (turnID, EVID, building_type) VALUES (41, 113, 0); 	# build 2nd player's road

# Turn 42 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (42, 1, 3, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(42, 1, 0, 1), # Income
(42, 2, 0, 1),
(42, 2, 3, 1);



# Turn 43 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (43, 1, 1, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(43, 1, 0, 1), # Income
(43, 2, 0, 1),
(43, 2, 4, 1);



# Turn 44 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (44, 1, 2, 8);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(44, 3, 0, 1); # Income



# Turn 45 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (45, 1, 3, 10);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(45, 1, 2, 1), #Income
(45, 3, 2, 2),

(45, 3, 1, 1), # Trade 4:1 to brick
(45, 3, 2, -4), # Trade 4:1 from sheep

(45, 3, 0, -1), # p3 builds house
(45, 3, 1, -1),
(45, 3, 2, -1),
(45, 3, 3, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (45, 20, 1); 	# p3 builds house



# Turn 46 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (46, 1, 1, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(46, 1, 0, 1), #income
(46, 2, 0, 1),
(46, 2, 4, 1),

(46, 1, 0, -4), # P1, 4W->1B
(46, 1, 1, 1);



# Turn 47 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (47, 1, 2, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(47, 1, 0, 1), # Income
(47, 2, 0, 1),
(47, 2, 3, 1),

(47, 2, 3, -2), # p2 buys city 
(47, 2, 4, -3);
INSERT INTO build (turnID, EVID, building_type) VALUES (47, 41, 2); 	# p2 builds city


# Turn 48 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (48, 1, 3, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(48, 1, 0, 1), # Income
(48, 2, 0, 1),
(48, 2, 3, 2);



# Turn 49 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (49, 1, 1, 5);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(49, 2, 3, 1), #income
(49, 3, 2, 2),
(49, 3, 3, 1), 

(49, 1, 0, -1), # p1 builds house
(49, 1, 1, -1),
(49, 1, 2, -1),
(49, 1, 3, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (49, 39, 1); 	# p1 builds house



# Turn 50 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (50, 1, 2, 3);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(50, 2, 3, 1),

(50, 2, 3, -4), # p2 4W -> B
(50, 2, 1, 1),

(50, 2, 0, -1), # p2 builds road
(50, 2, 1, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (50, 112, 0); 	# p2 builds road



# Turn 51 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (51, 1, 3, 5);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(51, 2, 3, 1), #income
(51, 3, 2, 2),
(51, 3, 3, 1),

(51, 3, 2, -1), # p3 trades sheep for p2's ore
(51, 2, 4, -1),
(51, 3, 4, 1),
(51, 2, 2, 1),

(51, 3, 2, -4), # p3 trades 2 sheep for 1 ore, twice
(51, 3, 4, 2), 

(51, 3, 3, -2), # p3 builds city
(51, 3, 4, -3);
INSERT INTO build (turnID, EVID, building_type) VALUES (51, 18, 2); 	# p3 builds city



# Turn 52 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (52, 1, 1, 5);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(52, 2, 3, 1), # Income
(52, 3, 2, 3),
(52, 3, 3, 1);



# Turn 53
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (53, 1, 2, 10);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(53, 1, 2, 1), # Income
(53, 3, 2, 3),

(53, 2, 0, -4), # p2 trades 4W -> 1B
(53, 2, 1, 1),

(53, 2, 0, -1), # p2 builds house
(53, 2, 1, -1),
(53, 2, 2, -1),
(53, 2, 3, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (53, 9, 1); # p2 builds house



# Turn 54
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (54, 1, 3, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES
(54, 3, 2, -4), # robber stole 4 sheep from p3

(54, 2, 3, -1), # p3 robs 1 wheat from p2 
(54, 3, 3, 1), 

(54, 3, 2, -2), # p3 trades 2:1 sheep to ore
(54, 3, 4, 1), 

(54, 3, 2, -1), # p3 buys dev card
(54, 3, 3, -1), 
(54, 3, 4, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (54, 3, 1); # p3 buys knight 
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (54, 3, 0); # p3 robs wheat from p2 



# Turn 55
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (55, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(55, 3, 3, -1), # p1 robs p3 once
(55, 1, 3, 1); 
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (54, 3, 0); # p1 robs wheat from p3 



# Turn 56
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (56, 1, 2, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(56, 1, 0, 1), # income
(56, 1, 3, 1),
(56, 2, 0, 1),
(56, 2, 3, 2);


# Turn 57 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (57, 1, 3, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(56, 1, 0, 1), # income
(56, 1, 3, 1),
(56, 2, 0, 1),
(56, 2, 3, 2);



# Turn 58
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (58, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES
(58, 1, 0, -1), #p1 lost 2 sheep, 2 wheat, 1 wood to robber
(58, 1, 2, -2),
(58, 1, 3, -2),

(58, 1, 0, 1),# p1 takes 1 wood from p2
(58, 2, 0, -1),

(58, 1, 0, -4), # p1 trades 4:1 wood -> brick
(58, 1, 1, 1);



# Turn 59
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (59, 1, 2, 9);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(59, 1, 1, 2),
(59, 1, 4, 1),
(59, 3, 1, 2);



# Turn 60
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (60, 1, 3, 12); # No income



# Turn 61
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (61, 1, 1, 8);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(61, 2, 0, 1), # Income
(61, 3, 0, 1),

(61, 1, 2, -1), # p1 buys dev card
(61, 1, 3, -1), 
(61, 1, 4, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (61, 1, 1); # p1 buys knight 

# Turn 62
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (62, 1, 2, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(62, 1, 0, 1), # Income
(62, 1, 3, 1),
(62, 2, 0, 1),
(62, 2, 3, 2);


# Turn 63
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (63, 1, 3, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(63, 1, 0, 1), # Income
(63, 2, 0, 2),

(63, 3, 0, -1), # p3 builds road
(63, 3, 1, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (63, 118, 0); 	# p3 builds road

# Turn 64 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (64, 1, 1, 8);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(64, 2, 0, 1), #income 
(64, 3, 0, 1),

(64, 1, 0, 2), # p1 plays road building
(64, 1, 1, 2), 
(64, 1, 0, -4), # p1 builds 4 roads incl. road building
(64, 1, 1, -4);

INSERT INTO build (turnID, EVID, building_type) VALUES (64, 162, 0), (64, 166, 0), (64, 147, 0), (64, 148, 0); 	# p1 builds 3x road
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (64, 3, -4); # p1 plays road building 


# Turn 65
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (65, 1, 2, 11);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(65, 1, 4, 1), # Income
(65, 2, 4, 1),

(65, 2, 0, -4), # p2 trades 4:1 wood -> ore
(65, 2, 4, 1),
(65, 2, 3, -4), # p2 trades 4:1 wheat -> ore
(65, 2, 4, 1),

(65, 2, 3, -2), # p2 builds city
(65, 2, 4, -3);
INSERT INTO build (turnID, EVID, building_type) VALUES (65, 33, 2); 	# p2 builds city

# Turn 66
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (66, 1, 3, 12); # no income



# Turn 67
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (67, 1, 1, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(67, 1, 0, 1), # income
(67, 2, 0, 2);


# Turn 68
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (68, 1, 2, 10);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(68, 1, 2, 1), # income
(68, 2, 2, 1),
(68, 3, 2, 3);



# Turn 69
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (69, 1, 3, 9);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(69, 1, 1, 2), # income
(69, 1, 4, 1),
(69, 3, 1, 2),

(69, 3, 2, -2), # p3 trades 2:1 sheep -> wheat
(69, 3, 3, 1),

(69, 3, 0, -1), # p3 buys a house
(69, 3, 1, -1),
(69, 3, 2, -1),
(69, 3, 3, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (69, 16, 1);  # build p3's house


# Turn 70 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (70, 1, 1, 9);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(70, 1, 1, 2), # income
(70, 1, 4, 1),
(70, 3, 1, 2),

(70, 1, 1, -4), # p2 trades 4 brick for 2 wheat thru 2:1 port
(70, 1, 3, 2),

(70, 1, 0, -1), # p1 buys a house
(70, 1, 1, -1),
(70, 1, 2, -1),
(70, 1, 3, -1),

(70, 1, 3, -2), # p1 builds city
(70, 1, 4, -3);
INSERT INTO build (turnID, EVID, building_type) VALUES (70, 48, 1); # build p1's house
INSERT INTO build (turnID, EVID, building_type) VALUES (70, 35, 2);  # p1 builds city

# Turn 71 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (71, 1, 2, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(71, 1, 0, 2), # income
(71, 1, 3, 2),
(71, 2, 0, 2),
(71, 2, 3, 2),

(71, 2, 2, -1), # p2 trades sheep and wheat for  p3's brick
(71, 2, 3, -1),
(71, 3, 1, -1), 
(71, 2, 1, 1),
(71, 3, 2, 1),
(71, 3, 3, 1),

(71, 2, 0, -1), # p2 buys road
(71, 2, 1, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (71, 107, 0); # p2 builds road


# Turn 72 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (72, 1, 3, 6);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(72, 1, 0, 2), # income
(72, 1, 3, 2),
(72, 2, 0, 2),
(72, 2, 3, 2),
(72, 3, 4, 2), # year of plenty income

(72, 3, 2, -1), # p3 buys dev card
(72, 3, 3, -1), 
(72, 3, 4, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (72, 3, 1), (72, 3, -5); # p3 buys knight, plays year of plenty


# Turn 73 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (73, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(73, 1, 0, -2), #p1 lost 2 wood, 2 wheat to robber
(73, 1, 3, -2),
(73, 2, 0, -5), #p2 lost 5 wood to robber

(73, 1, 0, 1),# p1 takes 1 wood from p2
(73, 2, 0, -1);



# Turn 74 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (74, 1, 2, 9);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(74, 1, 1, 2),
(74, 1, 4, 2),
(74, 3, 1, 2),

(74, 2, 3, -1), # p2 trades wheat for p3's brick
(74, 3, 1, -1),
(74, 2, 1, 1),
(74, 3, 3, 1);


# Turn 75 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (75, 1, 3, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(75, 1, 0, 1),
(75, 2, 0, 2),
(75, 2, 4, 1),

(75, 3, 1, -4), # p3 trades 4:1 brick -> sheep
(75, 3, 2, 1),

(75, 3, 3, 1), # KNIGHT played: p3 takes 1 wheat from p1
(75, 1, 3, -1),

(75, 3, 2, -1), # p3 buys dev card
(75, 3, 3, -1), 
(75, 3, 4, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (75, 3, -1); # p3 plays knight 
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (75, 3, 1); # p3 buys knight 


# Turn 76 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (76, 1, 1, 7);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(76, 1, 0, -3), #p1 lost 4 wood, 2 brick to robber
(76, 1, 1, -1),

(76, 1, 3, 1), # p1 steals 1 wheat from p3
(76, 3, 3, -1),


(76, 1, 4, -2), # p1 gains 1 sheep by trading in 2 ore
(76, 1, 2, 1),

(76, 1, 0, -1), # p3 buys a house
(76, 1, 1, -1),
(76, 1, 2, -1),
(76, 1, 3, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (76, 53, 1);  # build p3's house

# Turn 77 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (77, 1, 2, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(77, 1, 0, 1), #income
(77, 2, 0, 2),
(77, 2, 4, 1),

(77, 2, 0, -4), # p2 trades 4:1 wood -> sheep
(77, 2, 2, 1),

(77, 2, 0, -1), # p1 buys a house
(77, 2, 1, -1),
(77, 2, 2, -1),
(77, 2, 3, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (77, 37, 1);  # build p1's house

# Turn 78 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (78, 1, 3, 8);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(78, 2, 0, 2), # income
(78, 3, 0, 2),

(78, 3, 0, 1), # KNIGHT PLAYED : p3 takes 1 wood from p1
(78, 1, 0, -1);
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (78, 3, -1); # p3 plays knight



# Turn 79 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (79, 1, 1, 8);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(79, 2, 0, 2), # income
(79, 3, 0, 2);




# Turn 80 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (80, 1, 2, 4);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(80, 1, 0, 1),
(80, 2, 0, 2),
(80, 2, 4, 2),

(80, 2, 0, -3), #p2 trades 3 wood for 1 wheat
(80, 2, 3, 1),

(80, 2, 3, -2), # p2 builds city
(80, 2, 4, -3);
INSERT INTO build (turnID, EVID, building_type) VALUES (80, 2, 2); 	# p2 builds city


# Turn 81 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (81, 1, 3, 10);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(81, 1, 2, 2), #income
(81, 2, 2, 1),
(81, 3, 2, 3),

(81, 3, 0, -4), # p3 trades 4 wood and 2 sheep for 1 brick, 1 wheat, 1 ore
(81, 3, 2, -2),
(81, 3, 1, 1),
(81, 3, 3, 1),
(81, 3, 4, 1),

(81, 3, 0, -1), # p3 builds road
(81, 3, 1, -1),

(81, 3, 2, -1), # p3 buys dev card
(81, 3, 3, -1), 
(81, 3, 4, -1);
INSERT INTO build (turnID, EVID, building_type) VALUES (81, 118, 0); 	# p3 builds road
INSERT INTO buy_dev_card (turnID, playerID, dev_cardID) VALUES (81, 3, 1); # p3 buys knight 


# Turn 82 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (82, 1, 1, 8);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(82, 2, 0, 3), # income
(82, 3, 0, 2);


# Turn 83 
INSERT INTO turn (turnID, gameID, playerID, dice_result) VALUES (83, 1, 2, 11);
INSERT INTO player_resource_data (turnID, playerID, resource_type, qty)
VALUES 
(83, 1, 4, 1), # income
(83, 2, 4, 2),

(83, 2, 0, -3), # p2 trades 3 ore and 3 wood for 2 brick
(83, 2, 4, -3),
(83, 2, 1, 2),

(83, 2, 0, -2), # buy 2 roads
(83, 2, 1, -2);
INSERT INTO build (turnID, EVID, building_type) VALUES (83, 144, 0), (83, 151, 0); 	# p3 builds road
# Resources are all correct by this line

# Giving road to p2 after game so longest road VPs work out
INSERT INTO build (turnID, EVID, building_type) VALUES (83, 157, 0); 	
########################################################################################################################



# Find out how many victory points a person has at a specific turn
DROP FUNCTION IF EXISTS qty_knights;
DELIMITER //

CREATE FUNCTION qty_knights (player_num INT, turn_num INT)
RETURNS INT
BEGIN
	DECLARE knights INT;
    
	SELECT COUNT(BDC.dev_cardID) INTO knights   # Show which player played how many knights
	FROM buy_dev_card BDC, turn T, player P
	WHERE T.turnId = BDC.turnID
		AND T.playerID = P.playerID
        AND player_num = P.playerID # where player_num = player_id
        AND turn_num >= T.turnID # where turn_num is the turn in question (so includes data on all prev)
		AND BDC.dev_cardID = -1; # knight used is buy_dev_card(*, *, -1)
    
    RETURN knights;
END; 

// DELIMITER ;

SELECT qty_knights(3,80) AS knights; # double checking it works




# Find out how many roads a person has at a specific turn
DROP FUNCTION IF EXISTS qty_roads;
DELIMITER //

CREATE FUNCTION qty_roads (player_num INT, turn_num INT)
RETURNS INT

BEGIN
	DECLARE roads INT;
    
	SELECT COUNT(B.building_type) INTO roads   # Show which player played how many knights
	FROM build B, turn T, player P
	WHERE T.turnId = B.turnID
		AND T.playerID = P.playerID
        AND player_num = P.playerID # where player_num = player_id
        AND turn_num >= T.turnID # where turn_num is the turn in question (so includes data on all prev)
		AND B.building_type = 0; # road id is 0
    
    RETURN roads;
END; 

// DELIMITER ;

SELECT qty_roads(1,83) AS roads; # double check it works


# Find out how many VPs a person has at a specific turn (minus longest road/largest army)
DROP PROCEDURE IF EXISTS calc_VPs;
DELIMITER //
CREATE PROCEDURE calc_VPs (turn_num INT)

BEGIN
    
	SELECT turn_num AS turn, P.username, (COUNT(B.building_type))
	FROM build B 
		JOIN turn T ON T.turnID = B.turnID
        JOIN player P ON P.playerID = T.playerID
	WHERE B.building_type > 0
		AND T.turnID <= turn_num # make sure that we have all data on or before turn requested
	GROUP BY T.playerID;
    
END; 

// DELIMITER ;

CALL calc_VPs (83); # double check it works


# Find which resource was harvested the most this game
SELECT MAX(counted) as max_resources
	FROM (SELECT SUM(qty) AS counted
		FROM player_resource_data P
		WHERE qty > 0
		GROUP BY resource_type) AS counts;
        
# This is a table confirming above, and we find that that resource is wood 
CREATE VIEW max_resources_view AS
	SELECT resource_type, SUM(qty) AS counted
		FROM player_resource_data P
		WHERE P.qty > 0
		GROUP BY P.resource_type;

SELECT * FROM max_resources_view;

# select turns where one user gains 5 or more resources  
SELECT P.username, P.playerID, T.turnID, SUM(D.qty)
FROM player_resource_data D, player P
	JOIN turn T ON P.playerID = T.playerID
WHERE D.qty > 0 AND T.playerID = D.playerID AND T.turnID = D.turnID
GROUP BY D.turnID
HAVING SUM(D.qty) >= 5;


# select the distinct resource addition entries (there are 
# often duplicates) which have a quantity greater than 3
SELECT DISTINCT * FROM player_resource_data WHERE qty >= 3;


# select emails from users and titles from dev cards
SELECT email FROM player 
UNION 
SELECT title FROM dev_card
ORDER BY email ASC;


# Oops! We misspelled "hand" in "Year of Plenty's" card_text. Let's change that!
UPDATE dev_card 
SET card_text = "Take any 2 resources from the bank. Add them to you hand. They can be 2 of the same resource or 2 different resources."
WHERE title = "Year of Plenty";

# Select the users who have a city
SELECT P.username
FROM player P
WHERE EXISTS (SELECT B.building_type
	FROM turn T JOIN build B on B.turnID = T.turnID
    WHERE P.playerID = T.playerID AND B.bucommentscommentsilding_type = 2);