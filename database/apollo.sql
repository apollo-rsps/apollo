CREATE EXTENSION citext;

CREATE TYPE rank AS ENUM ('player', 'moderator', 'administrator');
CREATE TYPE gender AS ENUM ('male', 'female');

CREATE TYPE attribute_type AS ENUM ('long', 'boolean', 'string', 'double');

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
    'thieving',
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
    x                    smallint NOT NULL CHECK (x >= 0 AND x <= 16384),
    y                    smallint NOT NULL CHECK (y >= 0 AND y <= 16384),
    height               smallint NOT NULL CHECK (height >= 0 AND height <= 3),
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

CREATE TABLE title
(
    left_part   text,
    center_part text,
    right_part  text,
    player_id   integer references player (id),
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
    name      text           NOT NULL,
    attr_type attribute_type NOT NULL,
    value     text          NOT NULL,
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

CREATE PROCEDURE create_appearance(p_display_name text, p_gender gender, p_styles integer[7],
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

CREATE PROCEDURE create_title(p_display_name text, p_left_part text, p_center_part text, p_right_part text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO title (left_part, center_part, right_part, player_id)
    VALUES (p_left_part, p_center_part, p_right_part, (SELECT id FROM player WHERE display_name = p_display_name));

    COMMIT;
END ;
$$;

CREATE PROCEDURE create_attribute(p_display_name text, p_attr_type attribute_type, p_name varchar, p_value text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO attribute (attr_type, name, value, player_id)
    VALUES (p_attr_type, p_name, p_value, (SELECT id FROM player WHERE display_name = p_display_name));

    COMMIT;
END ;
$$;

CREATE PROCEDURE create_item(p_display_name text, p_inv_id integer, p_slot integer, p_item_id integer,
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

CREATE PROCEDURE create_stat(p_skill skill, p_stat integer, p_experience integer, p_display_name text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO stat (skill, stat, experience, player_id)
    VALUES (p_skill, p_stat, p_experience, (SELECT id FROM player WHERE display_name = p_display_name));

    COMMIT;
END;
$$;

CREATE PROCEDURE create_account(p_email varchar, p_password_hash varchar, p_rank rank)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO account (email, password_hash, rank)
    VALUES (p_email, p_password_hash, p_rank);

    COMMIT;
END;
$$;

CREATE PROCEDURE create_player(p_email citext, p_display_name text, p_x integer, p_y integer,
                               p_height integer)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO player (display_name, x, y, height, account_id)
    VALUES (p_display_name, p_x, p_y, p_height, (SELECT id FROM account WHERE email = p_email));

    COMMIT;
END;
$$;

CREATE PROCEDURE set_player(p_email citext, p_display_name text, p_last_login timestamp, p_x integer,
                            p_y integer, p_height integer)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE player AS p
    SET display_name         = p_display_name,
        last_login           = p_last_login,
        x                    = p_x,
        y                    = p_y,
        height               = p_height
    WHERE p.account_id = (SELECT id FROM account WHERE email = p_email);

    COMMIT;
END;
$$;

CREATE PROCEDURE set_account(p_email citext, p_rank rank)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE account AS a
    SET rank = p_rank
    WHERE email = p_email;

    COMMIT;
END;
$$;

CREATE PROCEDURE set_appearance(p_display_name text, p_gender gender, p_styles integer[7],
                                p_colours integer[5])
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE appearance AS a
    SET gender  = p_gender,
        styles  = p_styles,
        colours = p_colours
    WHERE a.player_id = (SELECT id FROM player WHERE display_name = p_display_name);

    COMMIT;
END;
$$;

CREATE PROCEDURE set_title(p_display_name text, p_left_part text, p_center_part text, p_right_part text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    UPDATE title
    SET left_part   = p_left_part,
        center_part = p_center_part,
        right_part  = p_right_part
    WHERE player_id = (SELECT id FROM player WHERE display_name = p_display_name);

    COMMIT;
END;
$$;

CREATE PROCEDURE set_item(p_display_name text, p_inv_id integer, p_slot integer, p_item_id integer,
                          p_quantity integer)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO item (inventory_id, slot, item_id, quantity, player_id)
    VALUES (p_inv_id, p_slot, p_item_id, p_quantity, (SELECT id FROM player WHERE display_name = p_display_name))
    ON CONFLICT (inventory_id, slot, player_id) DO UPDATE SET item_id = p_item_id, quantity = p_quantity;

    COMMIT;
END ;
$$;

CREATE PROCEDURE set_stat(p_skill skill, p_stat integer, p_experience integer, p_display_name text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO stat (skill, stat, experience, player_id)
    VALUES (p_skill, p_stat, p_experience, (SELECT id FROM player WHERE display_name = p_display_name))
    ON CONFLICT (player_id, skill, stat) DO UPDATE SET stat = p_stat, experience = p_experience;

    COMMIT;
END;
$$;

CREATE PROCEDURE set_attribute(p_display_name text, p_attr_type attribute_type, p_name varchar, p_value text)
    LANGUAGE plpgsql
AS
$$
BEGIN
    INSERT INTO attribute (attr_type, name, value, player_id)
    VALUES (p_attr_type, p_name, p_value, (SELECT id FROM player WHERE display_name = p_display_name))
    ON CONFLICT (player_id, name) DO UPDATE SET value = p_value;

    COMMIT;
END ;
$$;

CREATE PROCEDURE delete_item(p_display_name text, p_inv_id integer, p_slot integer)
    LANGUAGE plpgsql
AS
$$
BEGIN
    DELETE
    FROM item
    WHERE inventory_id = p_inv_id
      AND slot = p_slot
      AND player_id = (SELECT id FROM player WHERE display_name = p_display_name);

    COMMIT;
END ;
$$;

CREATE FUNCTION get_account(p_email text)
    RETURNS table
            (
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

CREATE FUNCTION get_player(p_display_name text)
    RETURNS table
            (
                last_login           timestamp,
                x                    smallint,
                y                    smallint,
                height               smallint
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT p.last_login, p.x, p.y, p.height
        FROM player AS p
        WHERE p.display_name = p_display_name
        LIMIT 1;
END;
$$
    LANGUAGE PLPGSQL;

CREATE FUNCTION get_appearance(p_display_name text)
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

CREATE FUNCTION get_title(p_display_name varchar)
    RETURNS table
            (
                left_part   text,
                center_part text,
                right_part  text
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT t.left_part, t.center_part, t.right_part
        FROM title AS t
                 INNER JOIN player AS p
                            ON p.id = t.player_id
        WHERE p.display_name = p_display_name
        LIMIT 1;
END;
$$
    LANGUAGE PLPGSQL;

CREATE FUNCTION get_skills(p_display_name text)
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

CREATE FUNCTION get_attributes(p_display_name text)
    RETURNS table
            (
                attr_type attribute_type,
                name      text,
                value     text
            )
AS
$$
BEGIN
    RETURN QUERY
        SELECT a.attr_type, a.name, a.value
        FROM attribute AS a
                 INNER JOIN player AS p
                            ON p.id = a.player_id
        WHERE p.display_name = p_display_name;
END;
$$
    LANGUAGE PLPGSQL;

CREATE FUNCTION get_items(p_display_name text)
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

CALL create_account('Sino'::citext, '$s0$e0801$U7iSxE4PoOGAg3wUkJkC2w==$WGCDBrNsBNosBEG8Uucz0YWZMv+T4NBJnQZRhcLCr6s=',
                    'administrator');
CALL create_player('Sino'::citext, 'Sino', 3254, 3420, 0);

CALL create_account('Sfix'::citext, '$s0$e0801$U7iSxE4PoOGAg3wUkJkC2w==$WGCDBrNsBNosBEG8Uucz0YWZMv+T4NBJnQZRhcLCr6s=',
                    'administrator');
CALL create_player('Sfix'::citext, 'Sfix', 3222, 3222, 0);

CALL create_title('Sino', '', '', '');
CALL create_title('Sfix', '', '', '');

CALL create_appearance('Sino', 'male', '{ 0, 10, 18, 26, 33, 36, 42 }', '{ 0, 0, 0, 0, 0 }');
CALL create_appearance('Sfix', 'male', '{ 0, 10, 18, 26, 33, 36, 42 }', '{ 0, 0, 0, 0, 0 }');

CALL create_stat('attack', 1, 0, 'Sino');
CALL create_stat('strength', 1, 0, 'Sino');
CALL create_stat('defence', 1, 0, 'Sino');
CALL create_stat('hitpoints', 10, 1183, 'Sino');
CALL create_stat('ranged', 1, 0, 'Sino');
CALL create_stat('prayer', 1, 0, 'Sino');
CALL create_stat('magic', 1, 0, 'Sino');
CALL create_stat('cooking', 1, 0, 'Sino');
CALL create_stat('fishing', 1, 0, 'Sino');
CALL create_stat('woodcutting', 1, 0, 'Sino');
CALL create_stat('firemaking', 1, 0, 'Sino');
CALL create_stat('mining', 1, 0, 'Sino');
CALL create_stat('smithing', 1, 0, 'Sino');
CALL create_stat('thieving', 1, 0, 'Sino');
CALL create_stat('agility', 1, 0, 'Sino');
CALL create_stat('herblore', 1, 0, 'Sino');
CALL create_stat('crafting', 1, 0, 'Sino');
CALL create_stat('fletching', 1, 0, 'Sino');
CALL create_stat('runecraft', 1, 0, 'Sino');
CALL create_stat('slayer', 1, 0, 'Sino');
CALL create_stat('farming', 1, 0, 'Sino');
CALL create_stat('hunter', 1, 0, 'Sino');
CALL create_stat('construction', 1, 0, 'Sino');

CALL create_stat('attack', 1, 0, 'Sfix');
CALL create_stat('strength', 1, 0, 'Sfix');
CALL create_stat('defence', 1, 0, 'Sfix');
CALL create_stat('hitpoints', 10, 1183, 'Sfix');
CALL create_stat('ranged', 1, 0, 'Sfix');
CALL create_stat('prayer', 1, 0, 'Sfix');
CALL create_stat('magic', 1, 0, 'Sfix');
CALL create_stat('cooking', 1, 0, 'Sfix');
CALL create_stat('fishing', 1, 0, 'Sfix');
CALL create_stat('woodcutting', 1, 0, 'Sfix');
CALL create_stat('firemaking', 1, 0, 'Sfix');
CALL create_stat('mining', 1, 0, 'Sfix');
CALL create_stat('smithing', 1, 0, 'Sfix');
CALL create_stat('thieving', 1, 0, 'Sfix');
CALL create_stat('agility', 1, 0, 'Sfix');
CALL create_stat('herblore', 1, 0, 'Sfix');
CALL create_stat('crafting', 1, 0, 'Sfix');
CALL create_stat('fletching', 1, 0, 'Sfix');
CALL create_stat('runecraft', 1, 0, 'Sfix');
CALL create_stat('slayer', 1, 0, 'Sfix');
CALL create_stat('farming', 1, 0, 'Sfix');
CALL create_stat('hunter', 1, 0, 'Sfix');
CALL create_stat('construction', 1, 0, 'Sfix');
