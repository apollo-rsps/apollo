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
    id            serial PRIMARY KEY,
    email         citext NOT NULL,
    password_hash text   NOT NULL,
    rank          rank   NOT NULL
);

CREATE TABLE player
(
    id                   serial PRIMARY KEY,
    display_name         text     NOT NULL,
    last_login           timestamp,
    location             location NOT NULL,
    title                title    NOT NULL DEFAULT ROW ('', '', ''),
    games_room_skill_lvl smallint NOT NULL DEFAULT 0,
    energy_units         smallint NOT NULL CHECK (energy_units >= 0 AND energy_units <= 10000),
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

CREATE OR REPLACE PROCEDURE create_account(p_email varchar, p_password_hash varchar, p_rank rank)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO account (email, password_hash, rank)
    VALUES (p_email, p_password_hash, p_rank);

    COMMIT;
END;
$$;

CREATE OR REPLACE PROCEDURE create_player(p_email citext, p_display_name text, p_x integer, p_y integer,
                                          p_height integer, p_energy_units integer)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO player (display_name, location, energy_units, account_id)
    VALUES (p_display_name, ROW (p_x, p_y, p_height), p_energy_units, (SELECT id FROM account WHERE email = p_email));

    COMMIT;
END;
$$;

CREATE OR REPLACE FUNCTION get_account(p_email varchar)
    RETURNS table
            (
                email         citext,
                password_hash text,
                rank          rank
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT a.password_hash, a.rank
        FROM account AS a
        WHERE a.email = p_email
        LIMIT 1;
END;
$$
    LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION get_player(p_display_name varchar)
    RETURNS table
            (
                last_login           timestamp,
                location             location,
                title                title,
                games_room_skill_lvl smallint,
                energy_units         smallint
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT p.location, p.title, p.games_room_skill_lvl, p.energy_units
        FROM player AS p
        WHERE p.display_name = p_display_name
        LIMIT 1;
END;
$$
    LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION get_appearance(p_display_name varchar)
    RETURNS table
            (
                gender  gender,
                styles  smallint[7],
                colours smallint[5]
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT a.gender, a.styles, a.colours
        FROM appearance AS a
                 INNER JOIN player AS p
                            ON p.id = a.player_id
        WHERE p.display_name = p_display_name
        LIMIT 1;
END;
$$
    LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION get_skills(p_display_name varchar)
    RETURNS table
            (
                skill      skill,
                stat       smallint,
                experience integer
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT s.skill, s.stat, s.experience
        FROM stat AS s
                 INNER JOIN player AS p
                            ON p.id = s.player_id
        WHERE p.display_name = p_display_name;
END;
$$
    LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION get_attributes(p_display_name varchar)
    RETURNS table
            (
                name  varchar,
                value integer
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT a.name, a.value
        FROM attribute AS a
                 INNER JOIN player AS p
                            ON p.id = a.player_id
        WHERE p.display_name = p_display_name;
END;
$$
    LANGUAGE PLPGSQL;

CREATE OR REPLACE FUNCTION get_items(p_display_name varchar)
    RETURNS table
            (
                slot         smallint,
                item_id      integer,
                quantity     integer,
                inventory_id smallint
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT i.slot, i.item_id, i.quantity, i.inventory_id
        FROM item AS i
                 INNER JOIN player AS p
                            ON p.id = i.player_id
        WHERE p.display_name = p_display_name;
END;
$$
    LANGUAGE PLPGSQL;

CALL create_account('Sino@gmail.com'::citext, 'hello123', 'administrator');
CALL create_player('Sino@gmail.com'::citext, 'Sino', 3254, 3420, 0, 10000);

CALL create_account('Sfix@gmail.com'::citext, 'hello123', 'administrator');
CALL create_player('Sfix@gmail.com'::citext, 'Sfix', 3222, 3222, 0, 0);