package me.libraryaddict.disguise.utilities;

public class OldDisguiseSounds
{
    public static void setSounds()
    {
        DisguiseSound.ARROW.setSounds(null, null, null, null, "random.bowhit");

        DisguiseSound.BAT.setSounds("mob.bat.hurt", null, "mob.bat.death", "mob.bat.idle", "damage.fallsmall", "mob.bat.loop",
                "damage.fallbig", "mob.bat.takeoff");

        DisguiseSound.BLAZE.setSounds("mob.blaze.hit", null, "mob.blaze.death", "mob.blaze.breathe", "damage.fallsmall",
                "damage.fallbig");

        DisguiseSound.CAVE_SPIDER.setSounds("mob.spider.say", "mob.spider.step", "mob.spider.death", "mob.spider.say");

        DisguiseSound.CHICKEN.setSounds("mob.chicken.hurt", "mob.chicken.step", "mob.chicken.hurt", "mob.chicken.say",
                "damage.fallsmall", "mob.chicken.plop", "damage.fallbig");

        DisguiseSound.COW.setSounds("mob.cow.hurt", "mob.cow.step", "mob.cow.hurt", "mob.cow.say");

        DisguiseSound.CREEPER.setSounds("mob.creeper.say", "step.grass", "mob.creeper.death", null);

        DisguiseSound.DONKEY.setSounds("mob.horse.donkey.hit", "step.grass", "mob.horse.donkey.death", "mob.horse.donkey.idle",
                "mob.horse.gallop", "mob.horse.leather", "mob.horse.donkey.angry", "mob.horse.wood", "mob.horse.armor",
                "mob.horse.soft", "mob.horse.land", "mob.horse.jump", "mob.horse.angry");

        DisguiseSound.ELDER_GUARDIAN.setSounds("mob.guardian.elder.hit", null, "mob.guardian.elder.death",
                "mob.guardian.elder.death");

        DisguiseSound.ENDER_DRAGON.setSounds("mob.enderdragon.hit", null, "mob.enderdragon.end", "mob.enderdragon.growl",
                "damage.fallsmall", "mob.enderdragon.wings", "damage.fallbig");

        DisguiseSound.ENDERMAN.setSounds("mob.endermen.hit", "step.grass", "mob.endermen.death", "mob.endermen.idle",
                "mob.endermen.scream", "mob.endermen.portal", "mob.endermen.stare");

        DisguiseSound.ENDERMITE.setSounds("mob.silverfish.hit", "mob.silverfish.step", "mob.silverfish.kill",
                "mob.silverfish.say");

        DisguiseSound.GHAST.setSounds("mob.ghast.scream", null, "mob.ghast.death", "mob.ghast.moan", "damage.fallsmall",
                "mob.ghast.fireball", "damage.fallbig", "mob.ghast.affectionate_scream", "mob.ghast.charge");

        DisguiseSound.GIANT.setSounds("damage.hit", "step.grass", null, null);

        DisguiseSound.GUARDIAN.setSounds("mob.guardian.hit", null, "mob.guardian.death", "mob.guardian.death");

        DisguiseSound.HORSE.setSounds("mob.horse.hit", "step.grass", "mob.horse.death", "mob.horse.idle", "mob.horse.gallop",
                "mob.horse.leather", "mob.horse.wood", "mob.horse.armor", "mob.horse.soft", "mob.horse.land", "mob.horse.jump",
                "mob.horse.angry", "mob.horse.leather");

        DisguiseSound.IRON_GOLEM.setSounds("mob.irongolem.hit", "mob.irongolem.walk", "mob.irongolem.death",
                "mob.irongolem.throw");

        DisguiseSound.MAGMA_CUBE.setSounds("mob.slime.attack", "mob.slime.big", null, null, "mob.slime.small");

        DisguiseSound.MULE.setSounds("mob.horse.donkey.hit", "step.grass", "mob.horse.donkey.death", "mob.horse.donkey.idle");

        DisguiseSound.MUSHROOM_COW.setSounds("mob.cow.hurt", "mob.cow.step", "mob.cow.hurt", "mob.cow.say");

        DisguiseSound.OCELOT.setSounds("mob.cat.hitt", "step.grass", "mob.cat.hitt", "mob.cat.meow", "mob.cat.purreow",
                "mob.cat.purr");

        DisguiseSound.PIG.setSounds("mob.pig.say", "mob.pig.step", "mob.pig.death", "mob.pig.say");

        DisguiseSound.PIG_ZOMBIE.setSounds("mob.zombiepig.zpighurt", null, "mob.zombiepig.zpigdeath", "mob.zombiepig.zpig",
                "mob.zombiepig.zpigangry");

        DisguiseSound.PLAYER.setSounds(ReflectionManager.is1_7() ? "game.player.hurt" : "damage.hit", "step.grass",
                ReflectionManager.is1_7() ? "game.player.hurt" : "damage.hit", null);

        DisguiseSound.RABBIT.setSounds("mob.rabbit.hurt", "mob.rabbit.hop", "mob.rabbit.death", "mob.rabbit.idle");

        DisguiseSound.SHEEP.setSounds("mob.sheep.say", "mob.sheep.step", null, "mob.sheep.say", "mob.sheep.shear");

        DisguiseSound.SILVERFISH.setSounds("mob.silverfish.hit", "mob.silverfish.step", "mob.silverfish.kill",
                "mob.silverfish.say");

        DisguiseSound.SKELETON.setSounds("mob.skeleton.hurt", "mob.skeleton.step", "mob.skeleton.death", "mob.skeleton.say");

        DisguiseSound.SKELETON_HORSE.setSounds("mob.horse.skeleton.hit", "step.grass", "mob.horse.skeleton.death",
                "mob.horse.skeleton.idle", "mob.horse.gallop", "mob.horse.leather", "mob.horse.wood", "mob.horse.armor",
                "mob.horse.soft", "mob.horse.land", "mob.horse.jump", "mob.horse.angry");

        DisguiseSound.SLIME.setSounds("mob.slime.attack", "mob.slime.big", null, null, "mob.slime.small");

        DisguiseSound.SPIDER.setSounds("mob.spider.say", "mob.spider.step", "mob.spider.death", "mob.spider.say");

        DisguiseSound.UNDEAD_HORSE.setSounds("mob.horse.zombie.hit", "step.grass", "mob.horse.zombie.death",
                "mob.horse.zombie.idle", "mob.horse.gallop", "mob.horse.leather", "mob.horse.wood", "mob.horse.armor",
                "mob.horse.soft", "mob.horse.land", "mob.horse.jump", "mob.horse.angry");

        DisguiseSound.VILLAGER.setSounds("mob.villager.hit", null, "mob.villager.death", "mob.villager.idle",
                "mob.villager.haggle", "mob.villager.no", "mob.villager.yes");

        DisguiseSound.WITCH.setSounds("mob.witch.hurt", null, "mob.witch.death", "mob.witch.idle");

        DisguiseSound.WITHER.setSounds("mob.wither.hurt", null, "mob.wither.death", "mob.wither.idle", "damage.fallsmall",
                "mob.wither.spawn", "damage.fallbig", "mob.wither.shoot");

        DisguiseSound.WITHER_SKELETON.setSounds("mob.skeleton.hurt", "mob.skeleton.step", "mob.skeleton.death",
                "mob.skeleton.say");

        DisguiseSound.WOLF.setSounds("mob.wolf.hurt", "mob.wolf.step", "mob.wolf.death", "mob.wolf.bark", "mob.wolf.panting",
                "mob.wolf.whine", "mob.wolf.howl", "mob.wolf.growl", "mob.wolf.shake");

        DisguiseSound.ZOMBIE.setSounds("mob.zombie.hurt", "mob.zombie.step", "mob.zombie.death", "mob.zombie.say",
                "mob.zombie.infect", "mob.zombie.woodbreak", "mob.zombie.metal", "mob.zombie.wood");

        DisguiseSound.ZOMBIE_VILLAGER.setSounds("mob.zombie.hurt", "mob.zombie.step", "mob.zombie.death", "mob.zombie.say",
                "mob.zombie.infect", "mob.zombie.woodbreak", "mob.zombie.metal", "mob.zombie.wood");
    }
}
