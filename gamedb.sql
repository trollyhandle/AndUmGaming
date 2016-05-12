SET SQL_SAFE_UPDATES=0;

DROP DATABASE testing;
CREATE DATABASE testing;
USE testing;

#SET FOREIGN_KEY_CHECKS=1;
# Tables


CREATE TABLE user (
	userID INT PRIMARY KEY AUTO_INCREMENT, 
    firstname VARCHAR(31), 
    lastname VARCHAR(31), 
    username VARCHAR(31) UNIQUE, 
    pword VARCHAR(31), 
    email VARCHAR(31));
CREATE TABLE game (
	gameID INT PRIMARY KEY AUTO_INCREMENT,
    qty_players INT);
CREATE TABLE playing_in_game (
	gameID INT,
    FOREIGN KEY (gameID) REFERENCES game (gameID),
    userID INT,
    FOREIGN KEY (userID) REFERENCES player (userID));
# 5/12 Work    
CREATE TABLE vertex (
	vertexID INT PRIMARY KEY AUTO_INCREMENT,
	q INT, r INT);
CREATE TABLE edge (
	destination INT,
    FOREIGN KEY (destination) REFERENCES vertex (vertexID), 
	source INT,
	direction INT,
    owner INT);
CREATE TABLE player_resources (
	userID INT, 
    FOREIGN KEY (userID) REFERENCES user (userID), 
	gameID INT,
    FOREIGN KEY (gameID) REFERENCES game (gameID), 
    wood INT DEFAULT 0, 
    brick INT DEFAULT 0,
    sheep INT DEFAULT 0,
    wheat INT DEFAULT 0,
    ore INT DEFAULT 0);