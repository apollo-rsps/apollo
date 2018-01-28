object Arrows : AmmoType({
    fired from "shortbows" and "longbows"

    projectile {
        startHeight = 41
        endHeight = 37
        delay = 41
        lifetime = 60
        pitch = 15
    }

    "bronze" {
        requires level 1
        30% dropChance

        graphics {
            projectile = 10
            attack = 19
        }
    }

    "iron" {
        requires level 1
        35% dropChance

        graphics {
            projectile = 9
            attack = 18
        }
    }

    "steel" {
        requires level 5
        40% dropChance

        graphics {
            projectile = 11
            attack = 20
        }
    }

    "mithril" {
        requires level 20
        45% dropChance

        graphics {
            projectile = 12
            attack = 21
        }
    }

    "adamant" {
        requires level 30
        50% dropChance

        graphics {
            projectile = 13
            attack = 22
        }
    }

    "rune" {
        requires level 40
        60% dropChance

        graphics {
            projectile = 15
            attack = 24
        }
    }
})