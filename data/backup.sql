PRAGMA foreign_keys = ON;

BEGIN TRANSACTION;

DROP TABLE IF EXISTS factions;
CREATE TABLE factions(
                faction_id INTEGER PRIMARY KEY ASC,
                faction_name TEXT);


DROP TABLE IF EXISTS users;
CREATE TABLE users(
                 account_id INTEGER PRIMARY KEY ASC,
                 user_name TEXT,
                 email TEXT,
                 password TEXT,
                 status INTEGER DEFAULT 0);

DROP TABLE IF EXISTS unverified_users;
CREATE TABLE unverified_users(
                 temp_id INTEGER PRIMARY KEY ASC,
                 user_name TEXT,
                 email TEXT,
                 password TEXT);

DROP TABLE IF EXISTS sectors;
CREATE TABLE sectors(
                sector_id INTEGER PRIMARY KEY ASC,
                sector_name TEXT,
                galactic_x INTEGER,
                galactic_y INTEGER,
                faction_id INTEGER,
                FOREIGN KEY(faction_id) REFERENCES factions(faction_id)
                );

DROP TABLE IF EXISTS npcs;
CREATE TABLE npcs(
		npc_id INTEGER PRIMARY KEY ASC,
		npc_name TEXT,
		x_pos FLOAT,
		y_pos FLOAT,
		faction_id INTEGER,
		sector_id INTEGER,
		structure_id INTEGER,
		FOREIGN KEY(faction_id) REFERENCES factions(faction_id),
		FOREIGN KEY(sector_id) REFERENCES sectors(sector_id)
);

DROP TABLE IF EXISTS characters;
CREATE TABLE characters(
                 character_id INTEGER PRIMARY KEY ASC,
                 account_id INTEGER,
                 character_name TEXT,
                 character_model INTEGER,
                 x_pos FLOAT,
                 y_pos FLOAT,
                 sector_id INTEGER,
                 FOREIGN KEY(account_id) REFERENCES users(account_id),
                 FOREIGN KEY(sector_id) REFERENCES sectors(sector_id)
                 );



DROP TABLE IF EXISTS ships;
CREATE TABLE ships(
                ship_id INTEGER PRIMARY KEY ASC,
                ship_name TEXT,
                ship_class TEXT,
                sector_id INTEGER,
                x_pos FLOAT,
                y_pos FLOAT,
                local_x_pos INTEGER,
                local_y_pos INTEGER,
                FOREIGN KEY(sector_id) REFERENCES sectors(sector_id)
                );


DROP TABLE IF EXISTS stations;
CREATE TABLE stations(
                station_id INTEGER PRIMARY KEY ASC,
                station_name TEXT,
                station_class TEXT,
                sector_id INTEGER,
                x_pos FLOAT,
                y_pos FLOAT,
                local_x_pos INTEGER,
                local_y_pos INTEGER,
                FOREIGN KEY(sector_id) REFERENCES sectors(sector_id)
                );


DROP TABLE IF EXISTS ship_rooms;
CREATE TABLE ship_rooms(
		room_id INTEGER,
		ship_id INTEGER,
		room_type TEXT,
		room_name TEXT,
		FOREIGN KEY(ship_id) REFERENCES ships(ship_id)
);

DROP TABLE IF EXISTS station_rooms;
CREATE TABLE station_rooms(
		room_id INTEGER,
		station_id INTEGER,
		room_type TEXT,
		room_name TEXT,
		FOREIGN KEY(station_id) REFERENCES ships(station_id)
);

INSERT INTO "factions" VALUES(0, 'None');
INSERT INTO "factions" VALUES(1, 'The Kingdom of Vri');
INSERT INTO "factions" VALUES(2, 'The Ex-i');
INSERT INTO "factions" VALUES(3, 'The Lawless');
INSERT INTO "factions" VALUES(4, 'The Bastion of Old Mengiron');
INSERT INTO "factions" VALUES(5, 'The Unknown');

INSERT INTO "sectors" VALUES(27400, '0-0', 0, 0, 3);
INSERT INTO "sectors" VALUES(27401, '0-1', 0, 1, 3);
INSERT INTO "sectors" VALUES(27402, '0-2', 0, 2, 3);
INSERT INTO "sectors" VALUES(27403, '0-3', 0, 3, 0);
INSERT INTO "sectors" VALUES(27404, '0-4', 0, 4, 0);
INSERT INTO "sectors" VALUES(27405, '0-5', 0, 5, 0);
INSERT INTO "sectors" VALUES(27406, '0-6', 0, 6, 0);

INSERT INTO "sectors" VALUES(27407, '1-0', 1, 0, 3);
INSERT INTO "sectors" VALUES(27408, '1-1', 1, 1, 0);
INSERT INTO "sectors" VALUES(27409, '1-2', 1, 2, 0);
INSERT INTO "sectors" VALUES(27410, '1-3', 1, 3, 0);
INSERT INTO "sectors" VALUES(27411, '1-4', 1, 4, 0);
INSERT INTO "sectors" VALUES(27412, '1-5', 1, 5, 0);
INSERT INTO "sectors" VALUES(27413, '1-6', 1, 6, 0);

INSERT INTO "sectors" VALUES(27414, '2-0', 2, 0, 0);
INSERT INTO "sectors" VALUES(27415, '2-1', 2, 1, 1);
INSERT INTO "sectors" VALUES(27416, '2-2', 2, 2, 1);
INSERT INTO "sectors" VALUES(27417, '2-3', 2, 3, 0);
INSERT INTO "sectors" VALUES(27418, '2-4', 2, 4, 0);
INSERT INTO "sectors" VALUES(27419, '2-5', 2, 5, 2);
INSERT INTO "sectors" VALUES(27420, '2-6', 2, 6, 2);

INSERT INTO "sectors" VALUES(27421, '3-0', 3, 0, 0);
INSERT INTO "sectors" VALUES(27422, '3-1', 3, 1, 1);
INSERT INTO "sectors" VALUES(27423, '3-2', 3, 2, 1);
INSERT INTO "sectors" VALUES(27424, '3-4', 3, 4, 0);
INSERT INTO "sectors" VALUES(27425, '3-5', 3, 5, 2);
INSERT INTO "sectors" VALUES(27426, '3-6', 3, 6, 2);

INSERT INTO "sectors" VALUES(27427, '4-0', 4, 0, 0);
INSERT INTO "sectors" VALUES(27428, '4-1', 4, 1, 1);
INSERT INTO "sectors" VALUES(27429, '4-2', 4, 2, 1);
INSERT INTO "sectors" VALUES(27430, '4-3', 4, 3, 1);
INSERT INTO "sectors" VALUES(27431, '4-4', 4, 4, 0);
INSERT INTO "sectors" VALUES(27432, '4-5', 4, 5, 0);
INSERT INTO "sectors" VALUES(27433, '4-6', 4, 6, 4);

INSERT INTO "sectors" VALUES(27434, '5-0', 5, 0, 0);
INSERT INTO "sectors" VALUES(27435, '5-1', 5, 1, 0);
INSERT INTO "sectors" VALUES(27436, '5-2', 5, 2, 1);
INSERT INTO "sectors" VALUES(27437, '5-3', 5, 3, 0);
INSERT INTO "sectors" VALUES(27438, '5-4', 5, 4, 0);
INSERT INTO "sectors" VALUES(27439, '5-5', 5, 5, 0);
INSERT INTO "sectors" VALUES(27440, '5-6', 5, 6, 0);

INSERT INTO "sectors" VALUES(27441, '6-0', 6, 0, 0);
INSERT INTO "sectors" VALUES(27442, '6-1', 6, 1, 0);
INSERT INTO "sectors" VALUES(27443, '6-2', 6, 2, 0);
INSERT INTO "sectors" VALUES(27444, '6-3', 6, 3, 0);
INSERT INTO "sectors" VALUES(27445, '6-4', 6, 4, 0);
INSERT INTO "sectors" VALUES(27446, '6-5', 6, 5, 0);
INSERT INTO "sectors" VALUES(27447, '6-6', 6, 6, 0);


INSERT INTO "users" VALUES(1, 'sirlachlan', 'pass', '$2a$04$R3jpsZg0OpudMBy.8Cbno.XX1DmvTFyUpLkcVZmt5qjWm.UKZodLO', 0);
INSERT INTO "users" VALUES(2, 'lachlanmac', 'pass', '$2a$04$4wbYlcHJ71Zfb42PyUhaP.GEUZboWaOZq739FQYfd94LMGuGkPFnG', 0);

INSERT INTO "users" VALUES(1001, 'testclient1', 'pass', '$2a$04$4wbYlcHJ71Zfb42PyUhaP.GEUZboWaOZq739FQYfd94LMGuGkPFnG', 0);
INSERT INTO "users" VALUES(1002, 'testclient2', 'pass', '$2a$04$4wbYlcHJ71Zfb42PyUhaP.GEUZboWaOZq739FQYfd94LMGuGkPFnG', 0);
INSERT INTO "users" VALUES(1003, 'testclient3', 'pass', '$2a$04$4wbYlcHJ71Zfb42PyUhaP.GEUZboWaOZq739FQYfd94LMGuGkPFnG', 0);



INSERT INTO "ships" VALUES(100, 'Ragnarok', 'Cruiser_A', 27422, 0, 0, 0, 0);
INSERT INTO "ships" VALUES(101, 'Shenigan', 'Cruiser_A', 27422, 0, 0, 1, 1);

INSERT INTO "stations" VALUES(1, 'DarNuraOutpost', 'Outpost_A', 27422, 0, 0, 3, 3);


INSERT INTO "ship_rooms" VALUES(0, 100, 'bridge', 'The Bridge');
INSERT INTO "ship_rooms" VALUES(1, 100, 'quarters', 'The Captains Quarters');
INSERT INTO "ship_rooms" VALUES(2, 100, 'quarters', 'The First Officers Quarters');
INSERT INTO "ship_rooms" VALUES(3, 100, 'shop', 'Outfitting');
INSERT INTO "ship_rooms" VALUES(4, 100, 'cafe', 'Cafe Solei');
INSERT INTO "ship_rooms" VALUES(5, 100, 'engineering', 'Engineering Dept. 1');		


INSERT INTO "characters" VALUES(3588, 1, 'Lachlan', 3954, 10, 10, 27422);
INSERT INTO "characters" VALUES(5488, 2, 'Drahkon', 3954, 84, 30, 27422);
INSERT INTO "characters" VALUES(997, 1003, 'Olivia', 3954, 84, 30, 27422);
INSERT INTO "characters" VALUES(998, 1001, 'testclientone', 3954, 2300, 3850, 27422);
INSERT INTO "characters" VALUES(999, 1002, 'testclienttwo', 3954, 2320, 3890, 27422);


INSERT INTO "npcs" VALUES(424, 'TheFirstGuy', 4100, 700, 1, 27422, 100);
INSERT INTO "npcs" VALUES(425, 'TheSecondGuy', 4150, 710, 1, 27422, 100);

COMMIT;
