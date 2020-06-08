package org.apollo.game.io.player

const val GET_ACCOUNT_QUERY = """
    SELECT a.password_hash, a.rank
    FROM account AS a
    WHERE a.email = ?
    LIMIT 1;
"""

const val GET_PLAYER_QUERY = """
    SELECT p.last_login, p.x, p.y, p.height
    FROM player AS p
    WHERE p.display_name = ?
    LIMIT 1;
"""

const val GET_APPEARANCE_QUERY = """
    SELECT a.gender, a.styles, a.colours
    FROM appearance AS a
    INNER JOIN player AS p
    ON p.id = a.player_id
    WHERE p.display_name = ?
    LIMIT 1;
"""

const val GET_ATTRIBUTES_QUERY = """
    SELECT a.attr_type, a.name, a.value
    FROM attribute AS a
    INNER JOIN player AS p
    ON p.id = a.player_id
    WHERE p.display_name = ?;
"""

const val GET_STATS_QUERY = """
    SELECT s.skill, s.stat, s.experience
    FROM stat AS s
    INNER JOIN player AS p
    ON p.id = s.player_id
    WHERE p.display_name = ?;
"""

const val GET_TITLE_QUERY = """
    SELECT t.left_part, t.center_part, t.right_part
    FROM title AS t
    INNER JOIN player AS p
    ON p.id = t.player_id
    WHERE p.display_name = ?
    LIMIT 1;
"""

const val GET_ITEMS_QUERY = """
    SELECT i.slot, i.item_id, i.quantity, i.inventory_id
    FROM item AS i
    INNER JOIN player AS p
    ON p.id = i.player_id
    WHERE p.display_name = ?;
"""