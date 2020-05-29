-- TODO: look into flyway-and liquibase to be able to get rid of these drops
DROP PROCEDURE IF EXISTS create_account(p_email varchar, p_password varchar, p_rank rank);
DROP PROCEDURE IF EXISTS create_stat(p_skill skill, p_stat integer, p_experience integer, p_display_name text);
DROP PROCEDURE IF EXISTS create_player(p_email varchar, p_display_name text, p_x integer, p_y integer, p_height integer, p_gender gender, p_styles integer[7], p_colours integer[5]);
DROP PROCEDURE IF EXISTS create_appearance(p_display_name text, p_gender gender, p_styles integer[7], p_colours integer[5]);
DROP PROCEDURE IF EXISTS create_attribute(p_display_name text, p_name varchar, p_value integer);
DROP PROCEDURE IF EXISTS create_item(p_display_name text, p_inv_id integer, p_slot integer, p_item_id integer, p_quantity integer);
DROP PROCEDURE IF EXISTS create_stat(p_skill skill, p_stat integer, p_experience integer, p_display_name text);

DROP TABLE IF EXISTS appearance;
DROP TABLE IF EXISTS attribute;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS stat;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS account;

DROP TYPE IF EXISTS title;
DROP TYPE IF EXISTS location;
DROP TYPE IF EXISTS gender;
DROP TYPE IF EXISTS rank;
DROP TYPE IF EXISTS skill;

DROP DOMAIN IF EXISTS x_coord;
DROP DOMAIN IF EXISTS y_coord;
DROP DOMAIN IF EXISTS height_coord;

DROP EXTENSION IF EXISTS citext;

CREATE EXTENSION citext;

CREATE TYPE rank AS ENUM ('player', 'moderator', 'administrator');
CREATE TYPE gender AS ENUM ('male', 'female');

CREATE DOMAIN x_coord AS int CHECK (VALUE >= 0 AND VALUE <= 16384);
CREATE DOMAIN y_coord AS int CHECK (VALUE >= 0 AND VALUE <= 16384);
CREATE DOMAIN height_coord AS int CHECK (VALUE >= 0 AND VALUE <= 3);

-- TODO: Position is a reserved postgres keyword; is location a good fit otherwise?
CREATE TYPE location AS
(
    x      x_coord,
    y      y_coord,
    height height_coord
);

CREATE TYPE title AS
(
    left_part   text,
    center_part text,
    right_part  text
);

CREATE TYPE skill AS ENUM (
    'attack',
    'strength',
    'defence',
    'hitpoints',
    'ranged',
    'prayer',
    'magic',
    'cooking',
    'fishing',
    'woodcutting',
    'firemaking',
    'mining',
    'smithing',
    'agility',
    'herblore',
    'crafting',
    'fletching',
    'runecraft',
    'slayer',
    'farming',
    'hunter',
    'construction'
    );

CREATE TABLE account
(
    id       serial PRIMARY KEY,
    email    citext NOT NULL,
    password text   NOT NULL,
    rank     rank   NOT NULL
);

CREATE TABLE player
(
    id                   serial PRIMARY KEY,
    display_name         text     NOT NULL,
    location             location NOT NULL,
    title                title    NOT NULL DEFAULT ROW ('', '', ''),
    games_room_skill_lvl smallint NOT NULL DEFAULT 0,
    account_id           integer references account (id)
);

CREATE UNIQUE INDEX player_display_name_index ON player (display_name);

CREATE TABLE appearance
(
    gender    gender            NOT NULL,
    styles    smallint ARRAY[7] NOT NULL,
    colours   smallint ARRAY[5] NOT NULL,
    player_id integer references player (id),
    PRIMARY KEY (player_id)
);

CREATE TABLE item
(
    inventory_id smallint CHECK (inventory_id >= 0),
    slot         smallint CHECK (slot >= 0),
    item_id      integer CHECK (item_id >= 0),
    quantity     integer CHECK (quantity >= 0),
    player_id    integer references player (id),
    PRIMARY KEY (inventory_id, slot, player_id)
);

CREATE TABLE attribute
(
    name      text    NOT NULL,
    value     integer NOT NULL DEFAULT 0,
    player_id integer references player (id),
    PRIMARY KEY (player_id, name)
);

CREATE TABLE stat
(
    skill      skill NOT NULL,
    stat       smallint CHECK (stat >= 0 AND stat <= 127),
    experience integer CHECK (experience >= 0 AND experience <= 200000000),
    player_id  integer REFERENCES player (id),
    PRIMARY KEY (player_id, skill, stat)
);

CREATE OR REPLACE PROCEDURE create_appearance(p_display_name text, p_gender gender, p_styles integer[7],
                                              p_colours integer[5])
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO appearance (gender, styles, colours, player_id)
    VALUES (p_gender, p_styles, p_colours, (SELECT id FROM player WHERE display_name = p_display_name));

    COMMIT;
END ;
$$;

CREATE OR REPLACE PROCEDURE create_attribute(p_display_name text, p_name varchar, p_value integer)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO attribute (name, value, player_id)
    VALUES (p_name, p_value, (SELECT id FROM player WHERE display_name = p_display_name));

    COMMIT;
END ;
$$;

CREATE OR REPLACE PROCEDURE create_item(p_display_name text, p_inv_id integer, p_slot integer, p_item_id integer,
                                        p_quantity integer)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO item (inventory_id, slot, item_id, quantity, player_id)
    VALUES (p_inv_id, p_slot, p_item_id, p_quantity, (SELECT id FROM player WHERE display_name = p_display_name));

    COMMIT;
END ;
$$;

CREATE OR REPLACE PROCEDURE create_stat(p_skill skill, p_stat integer, p_experience integer, p_display_name text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO stat (skill, stat, experience, player_id)
    VALUES (p_skill, p_stat, p_experience, (SELECT id FROM player WHERE display_name = p_display_name));

    COMMIT;
END;
$$;

CREATE OR REPLACE PROCEDURE create_account(p_email varchar, p_password varchar, p_rank rank)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO account (email, password, rank)
    VALUES (p_email, p_password, p_rank);

    COMMIT;
END;
$$;

CREATE OR REPLACE PROCEDURE create_player(p_email varchar, p_display_name text, p_x integer, p_y integer,
                                          p_height integer, p_gender gender, p_styles integer[7], p_colours integer[5])
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO player (display_name, location, account_id)
    VALUES (p_display_name, ROW (p_x, p_y, p_height), (SELECT id FROM account WHERE email = p_email));

    CALL create_appearance(p_display_name, p_gender, p_styles, p_colours);

    -- TODO: find a way to iterate through the possible values of an enum
    CALL create_stat('attack', 1, 0, p_display_name);
    CALL create_stat('strength', 1, 0, p_display_name);
    CALL create_stat('defence', 1, 0, p_display_name);
    CALL create_stat('hitpoints', 10, 1184, p_display_name);
    CALL create_stat('ranged', 1, 0, p_display_name);
    CALL create_stat('prayer', 1, 0, p_display_name);
    CALL create_stat('magic', 1, 0, p_display_name);
    CALL create_stat('cooking', 1, 0, p_display_name);
    CALL create_stat('fishing', 1, 0, p_display_name);
    CALL create_stat('woodcutting', 1, 0, p_display_name);
    CALL create_stat('firemaking', 1, 0, p_display_name);
    CALL create_stat('mining', 1, 0, p_display_name);
    CALL create_stat('smithing', 1, 0, p_display_name);
    CALL create_stat('agility', 1, 0, p_display_name);
    CALL create_stat('herblore', 1, 0, p_display_name);
    CALL create_stat('crafting', 1, 0, p_display_name);
    CALL create_stat('fletching', 1, 0, p_display_name);
    CALL create_stat('runecraft', 1, 0, p_display_name);
    CALL create_stat('slayer', 1, 0, p_display_name);
    CALL create_stat('farming', 1, 0, p_display_name);
    CALL create_stat('hunter', 1, 0, p_display_name);
    CALL create_stat('construction', 1, 0, p_display_name);

    COMMIT;
END;
$$;

CALL create_account('Sino@gmail.com', 'hello123', 'administrator');
CALL create_player('Sino@gmail.com', 'Sino', 3254, 3420, 0, 'male', '{ 0, 10, 18, 26, 33, 36, 42 }',
                   '{ 0, 0, 0, 0, 0 }');