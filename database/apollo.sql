DROP TABLE IF EXISTS appearance;
DROP TABLE IF EXISTS position;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS skill;
DROP TABLE IF EXISTS player;

DROP TYPE IF EXISTS gender_type;

CREATE TYPE privilege_level_type AS ENUM ('regular', 'pmod', 'staff');
CREATE TYPE gender_type AS ENUM ('male', 'female');

CREATE TABLE player
(
    id              serial PRIMARY KEY,
    username        VARCHAR(255)         NOT NULL,
    password        VARCHAR(255)         NOT NULL,
    privilege_level privilege_level_type NOT NULL DEFAULT 'regular'
);

CREATE UNIQUE INDEX player_username_index ON player (username);

CREATE TABLE appearance
(
    id        serial PRIMARY KEY,
    sex       gender_type       NOT NULL DEFAULT 'male',
    styles    smallint ARRAY[7] NOT NULL DEFAULT '{ 0, 10, 18, 26, 33, 36, 42 }',
    colors    smallint ARRAY[5] NOT NULL DEFAULT '{ 0, 0, 0, 0, 0 }',
    player_id integer references player (id)
);

CREATE TABLE position
(
    id        serial PRIMARY KEY,
    x         smallint NOT NULL CHECK (x >= 0 AND x <= 16384) DEFAULT 3222,
    y         smallint NOT NULL CHECK (y >= 0 AND y <= 16384) DEFAULT 3222,
    z         smallint NOT NULL CHECK (z >= 0 AND z <= 3)     DEFAULT 0,
    player_id integer references player (id)
);

CREATE TABLE item
(
    id           SERIAL PRIMARY KEY,
    inventory_id smallint CHECK (inventory_id >= 0),
    slot         smallint CHECK (slot >= 0),
    item_id      integer CHECK (item_id >= 0),
    quantity     integer CHECK (quantity >= 0)
);

CREATE TABLE skill
(
    id         SERIAL PRIMARY KEY,
    skill_id   smallint CHECK (skill_id >= 0),
    stat       smallint CHECK (stat >= 0 AND stat <= 127),
    experience float CHECK (experience >= 0 AND experience <= 200000000),
    player_id  integer REFERENCES player (id)
);

CREATE OR REPLACE PROCEDURE create_new_player(p_username varchar, p_password varchar, p_x integer, p_z integer,
                                              p_altitude integer)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO player (username, password)
    VALUES (p_username, p_password);

    INSERT INTO appearance (player_id)
    VALUES ((SELECT id FROM player WHERE username = p_username));

    INSERT INTO position (x, y, z, player_id)
    VALUES (p_x, p_z, p_altitude, (SELECT id FROM player WHERE username = p_username));

    FOR i IN 0..22
        LOOP
            INSERT INTO skill (skill_id, stat, experience, player_id)
            VALUES (i, 1, 0, (SELECT id FROM player WHERE username = p_username));
        END LOOP;

    COMMIT;
END;
$$;

CALL create_new_player('Sino', 'hello123', 3254, 3420, 0);