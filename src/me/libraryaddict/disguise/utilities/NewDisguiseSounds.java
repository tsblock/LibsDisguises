package me.libraryaddict.disguise.utilities;

import org.bukkit.Sound;

public class NewDisguiseSounds
{
    public static void setSounds()
    {
        DisguiseSound.ARROW.setSounds(null, null, null, null, Sound.ENTITY_ARROW_HIT, Sound.ENTITY_ARROW_SHOOT);

        DisguiseSound.BAT.setSounds(Sound.ENTITY_BAT_HURT, null, Sound.ENTITY_BAT_DEATH, Sound.ENTITY_BAT_AMBIENT,
                Sound.ENTITY_PLAYER_SMALL_FALL, Sound.ENTITY_BAT_LOOP, Sound.ENTITY_PLAYER_BIG_FALL, Sound.ENTITY_BAT_TAKEOFF);

        DisguiseSound.BLAZE.setSounds(Sound.ENTITY_BLAZE_HURT, null, Sound.ENTITY_BLAZE_DEATH, Sound.ENTITY_BLAZE_AMBIENT,
                Sound.ENTITY_PLAYER_SMALL_FALL, Sound.ENTITY_PLAYER_BIG_FALL);

        DisguiseSound.CAVE_SPIDER.setSounds(Sound.ENTITY_SPIDER_AMBIENT, Sound.ENTITY_SPIDER_STEP, Sound.ENTITY_SPIDER_DEATH,
                Sound.ENTITY_SPIDER_AMBIENT);

        DisguiseSound.CHICKEN.setSounds(Sound.ENTITY_CHICKEN_HURT, Sound.ENTITY_CHICKEN_STEP, Sound.ENTITY_CHICKEN_HURT,
                Sound.ENTITY_CHICKEN_AMBIENT, Sound.ENTITY_PLAYER_SMALL_FALL, Sound.ENTITY_CHICKEN_EGG,
                Sound.ENTITY_PLAYER_BIG_FALL);

        DisguiseSound.COW.setSounds(Sound.ENTITY_COW_HURT, Sound.ENTITY_COW_STEP, Sound.ENTITY_COW_DEATH,
                Sound.ENTITY_COW_AMBIENT);

        DisguiseSound.CREEPER.setSounds(Sound.ENTITY_CREEPER_HURT, "step.grass", Sound.ENTITY_CREEPER_DEATH, null);

        DisguiseSound.DONKEY.setSounds(Sound.ENTITY_DONKEY_HURT, "step.grass", Sound.ENTITY_DONKEY_DEATH,
                Sound.ENTITY_DONKEY_AMBIENT, Sound.ENTITY_HORSE_GALLOP, Sound.ENTITY_HORSE_SADDLE, Sound.ENTITY_DONKEY_ANGRY,
                Sound.ENTITY_HORSE_STEP_WOOD, Sound.ENTITY_HORSE_ARMOR, Sound.ENTITY_HORSE_LAND, Sound.ENTITY_HORSE_JUMP,
                Sound.ENTITY_HORSE_ANGRY);

        DisguiseSound.ELDER_GUARDIAN.setSounds(Sound.ENTITY_ELDER_GUARDIAN_HURT, null, Sound.ENTITY_ELDER_GUARDIAN_DEATH,
                Sound.ENTITY_ELDER_GUARDIAN_AMBIENT);

        DisguiseSound.ENDER_DRAGON.setSounds(Sound.ENTITY_ENDERDRAGON_HURT, null, Sound.ENTITY_ENDERDRAGON_DEATH,
                Sound.ENTITY_ENDERDRAGON_AMBIENT, Sound.ENTITY_PLAYER_SMALL_FALL, Sound.ENTITY_ENDERDRAGON_FLAP,
                Sound.ENTITY_PLAYER_BIG_FALL);

        DisguiseSound.ENDERMAN.setSounds(Sound.ENTITY_ENDERMEN_HURT, "step.grass", Sound.ENTITY_ENDERMEN_DEATH,
                Sound.ENTITY_ENDERMEN_AMBIENT, Sound.ENTITY_ENDERMEN_SCREAM, Sound.ENTITY_ENDERMEN_TELEPORT,
                Sound.ENTITY_ENDERMEN_STARE);

        DisguiseSound.ENDERMITE.setSounds(Sound.ENTITY_SILVERFISH_HURT, Sound.ENTITY_ENDERMITE_STEP, Sound.ENTITY_ENDERMITE_DEATH,
                Sound.ENTITY_ENDERMITE_AMBIENT);

        DisguiseSound.GHAST.setSounds(Sound.ENTITY_GHAST_HURT, null, Sound.ENTITY_GHAST_DEATH, Sound.ENTITY_GHAST_AMBIENT,
                Sound.ENTITY_PLAYER_SMALL_FALL, Sound.ENTITY_GHAST_SHOOT, Sound.ENTITY_PLAYER_BIG_FALL, Sound.ENTITY_GHAST_SCREAM,
                Sound.ENTITY_GHAST_WARN);

        DisguiseSound.GIANT.setSounds(Sound.ENTITY_PLAYER_HURT, "step.grass", null, null);

        DisguiseSound.GUARDIAN.setSounds(Sound.ENTITY_GUARDIAN_HURT, null, Sound.ENTITY_GUARDIAN_DEATH,
                Sound.ENTITY_ELDER_GUARDIAN_AMBIENT);

        DisguiseSound.HORSE.setSounds(Sound.ENTITY_HORSE_HURT, "step.grass", Sound.ENTITY_HORSE_DEATH, Sound.ENTITY_HORSE_AMBIENT,
                Sound.ENTITY_HORSE_GALLOP, Sound.ENTITY_HORSE_SADDLE, Sound.ENTITY_DONKEY_ANGRY, Sound.ENTITY_HORSE_STEP_WOOD,
                Sound.ENTITY_HORSE_ARMOR, Sound.ENTITY_HORSE_LAND, Sound.ENTITY_HORSE_JUMP, Sound.ENTITY_HORSE_ANGRY);

        DisguiseSound.IRON_GOLEM.setSounds(Sound.ENTITY_IRONGOLEM_HURT, Sound.ENTITY_IRONGOLEM_STEP, Sound.ENTITY_IRONGOLEM_DEATH,
                Sound.ENTITY_IRONGOLEM_ATTACK);

        DisguiseSound.MAGMA_CUBE.setSounds(Sound.ENTITY_MAGMACUBE_HURT, Sound.ENTITY_MAGMACUBE_JUMP, null, null);

        DisguiseSound.MULE.setSounds(Sound.ENTITY_MULE_HURT, "step.grass", Sound.ENTITY_MULE_DEATH, Sound.ENTITY_MULE_AMBIENT);

        DisguiseSound.MUSHROOM_COW.setSounds(Sound.ENTITY_COW_HURT, Sound.ENTITY_COW_STEP, Sound.ENTITY_COW_HURT,
                Sound.ENTITY_COW_AMBIENT);

        DisguiseSound.OCELOT.setSounds(Sound.ENTITY_CAT_HURT, "step.grass", Sound.ENTITY_CAT_HURT, Sound.ENTITY_CAT_AMBIENT,
                Sound.ENTITY_CAT_PURR, Sound.ENTITY_CAT_PURREOW);

        DisguiseSound.PIG.setSounds(Sound.ENTITY_PIG_HURT, Sound.ENTITY_PIG_STEP, Sound.ENTITY_PIG_DEATH,
                Sound.ENTITY_PIG_AMBIENT);

        DisguiseSound.PIG_ZOMBIE.setSounds(Sound.ENTITY_ZOMBIE_PIG_HURT, null, Sound.ENTITY_ZOMBIE_PIG_DEATH,
                Sound.ENTITY_ZOMBIE_PIG_AMBIENT, Sound.ENTITY_ZOMBIE_PIG_ANGRY);

        DisguiseSound.PLAYER.setSounds(Sound.ENTITY_PLAYER_HURT, "step.grass", Sound.ENTITY_PLAYER_DEATH, null);

        DisguiseSound.RABBIT.setSounds(Sound.ENTITY_RABBIT_HURT, Sound.ENTITY_RABBIT_JUMP, Sound.ENTITY_RABBIT_DEATH,
                Sound.ENTITY_RABBIT_AMBIENT);

        DisguiseSound.SHEEP.setSounds(Sound.ENTITY_SHEEP_HURT, Sound.ENTITY_SHEEP_STEP, null, Sound.ENTITY_SHEEP_AMBIENT,
                Sound.ENTITY_SHEEP_SHEAR);

        DisguiseSound.SHULKER.setSounds(Sound.ENTITY_SHULKER_HURT, null, Sound.ENTITY_SHULKER_DEATH, Sound.ENTITY_SHULKER_AMBIENT,
                Sound.ENTITY_SHULKER_OPEN, Sound.ENTITY_SHULKER_CLOSE, Sound.ENTITY_SHULKER_HURT_CLOSED,
                Sound.ENTITY_SHULKER_TELEPORT);

        DisguiseSound.SILVERFISH.setSounds(Sound.ENTITY_SILVERFISH_HURT, Sound.ENTITY_SILVERFISH_STEP,
                Sound.ENTITY_SILVERFISH_DEATH, Sound.ENTITY_SILVERFISH_AMBIENT);

        DisguiseSound.SKELETON.setSounds(Sound.ENTITY_SKELETON_HURT, Sound.ENTITY_SKELETON_STEP, Sound.ENTITY_SKELETON_DEATH,
                Sound.ENTITY_SKELETON_AMBIENT);

        DisguiseSound.SKELETON_HORSE.setSounds(Sound.ENTITY_SKELETON_HORSE_HURT, "step.grass", Sound.ENTITY_SKELETON_HORSE_DEATH,
                Sound.ENTITY_SKELETON_HORSE_AMBIENT, Sound.ENTITY_HORSE_GALLOP, Sound.ENTITY_HORSE_SADDLE,
                Sound.ENTITY_DONKEY_ANGRY, Sound.ENTITY_HORSE_STEP_WOOD, Sound.ENTITY_HORSE_ARMOR, Sound.ENTITY_HORSE_LAND,
                Sound.ENTITY_HORSE_JUMP, Sound.ENTITY_HORSE_ANGRY);

        DisguiseSound.SLIME.setSounds(Sound.ENTITY_SLIME_HURT, Sound.ENTITY_SLIME_JUMP, Sound.ENTITY_SLIME_DEATH, null);

        DisguiseSound.SNOWMAN.setSounds(Sound.ENTITY_SNOWMAN_HURT, null, Sound.ENTITY_SNOWMAN_DEATH, Sound.ENTITY_SNOWMAN_AMBIENT,
                Sound.ENTITY_SNOWMAN_SHOOT);

        DisguiseSound.SPIDER.setSounds(Sound.ENTITY_SPIDER_AMBIENT, Sound.ENTITY_SPIDER_STEP, Sound.ENTITY_SPIDER_DEATH,
                Sound.ENTITY_SPIDER_AMBIENT);

        DisguiseSound.SQUID.setSounds(Sound.ENTITY_SQUID_HURT, null, Sound.ENTITY_SQUID_DEATH, Sound.ENTITY_SQUID_AMBIENT);

        DisguiseSound.UNDEAD_HORSE.setSounds(Sound.ENTITY_ZOMBIE_HORSE_HURT, "step.grass", Sound.ENTITY_ZOMBIE_HORSE_DEATH,
                Sound.ENTITY_ZOMBIE_HORSE_AMBIENT, Sound.ENTITY_HORSE_GALLOP, Sound.ENTITY_HORSE_SADDLE,
                Sound.ENTITY_DONKEY_ANGRY, Sound.ENTITY_HORSE_STEP_WOOD, Sound.ENTITY_HORSE_ARMOR, Sound.ENTITY_HORSE_LAND,
                Sound.ENTITY_HORSE_JUMP, Sound.ENTITY_HORSE_ANGRY);

        DisguiseSound.VILLAGER.setSounds(Sound.ENTITY_VILLAGER_HURT, null, Sound.ENTITY_VILLAGER_DEATH,
                Sound.ENTITY_VILLAGER_AMBIENT, Sound.ENTITY_VILLAGER_TRADING, Sound.ENTITY_VILLAGER_NO,
                Sound.ENTITY_VILLAGER_YES);

        DisguiseSound.WITCH.setSounds(Sound.ENTITY_WITCH_HURT, null, Sound.ENTITY_WITCH_DEATH, Sound.ENTITY_WITCH_AMBIENT);

        DisguiseSound.WITHER.setSounds(Sound.ENTITY_WITHER_HURT, null, Sound.ENTITY_WITHER_DEATH, Sound.ENTITY_WITHER_AMBIENT,
                Sound.ENTITY_PLAYER_SMALL_FALL, Sound.ENTITY_WITHER_SPAWN, Sound.ENTITY_PLAYER_BIG_FALL,
                Sound.ENTITY_WITHER_SHOOT);

        DisguiseSound.WITHER_SKELETON.setSounds(Sound.ENTITY_SKELETON_HURT, Sound.ENTITY_SKELETON_STEP,
                Sound.ENTITY_SKELETON_DEATH, Sound.ENTITY_SKELETON_AMBIENT);

        DisguiseSound.WOLF.setSounds(Sound.ENTITY_WOLF_HURT, Sound.ENTITY_WOLF_STEP, Sound.ENTITY_WOLF_DEATH,
                Sound.ENTITY_WOLF_AMBIENT, Sound.ENTITY_WOLF_GROWL, Sound.ENTITY_WOLF_PANT, Sound.ENTITY_WOLF_HOWL,
                Sound.ENTITY_WOLF_SHAKE, Sound.ENTITY_WOLF_WHINE);

        DisguiseSound.ZOMBIE.setSounds(Sound.ENTITY_ZOMBIE_HURT, Sound.ENTITY_ZOMBIE_STEP, Sound.ENTITY_ZOMBIE_DEATH,
                Sound.ENTITY_ZOMBIE_AMBIENT, Sound.ENTITY_ZOMBIE_INFECT, Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD,
                Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR);

        DisguiseSound.ZOMBIE_VILLAGER.setSounds(Sound.ENTITY_ZOMBIE_VILLAGER_HURT, Sound.ENTITY_ZOMBIE_VILLAGER_STEP,
                Sound.ENTITY_ZOMBIE_VILLAGER_DEATH, Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, Sound.ENTITY_ZOMBIE_INFECT,
                Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, Sound.ENTITY_ZOMBIE_ATTACK_DOOR_WOOD, Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR);
    }
}
