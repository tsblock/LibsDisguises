package me.libraryaddict.disguise.utilities;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Sound;

/**
 * Only living disguises go in here!
 */
public enum DisguiseSound
{

    ARROW,

    BAT,

    BLAZE,

    CAVE_SPIDER,

    CHICKEN,

    COW,

    CREEPER,

    DONKEY,

    ELDER_GUARDIAN,

    ENDER_DRAGON,

    ENDERMAN,

    ENDERMITE,

    GHAST,

    GIANT,

    GUARDIAN,

    HORSE,

    IRON_GOLEM,

    MAGMA_CUBE,

    MULE,

    MUSHROOM_COW,

    OCELOT,

    PIG,

    PIG_ZOMBIE,

    PLAYER,

    RABBIT,

    SHEEP,

    SHULKER,

    SILVERFISH,

    SKELETON,

    SKELETON_HORSE,

    SLIME,

    SNOWMAN,

    SPIDER,

    SQUID,

    UNDEAD_HORSE,

    VILLAGER,

    WITCH,

    WITHER,

    WITHER_SKELETON,

    WOLF,

    ZOMBIE,

    ZOMBIE_VILLAGER;

    static
    {
        if (ReflectionManager.is1_7() || ReflectionManager.is1_8())
        {
            OldDisguiseSounds.setSounds();
        }
        else
        {
            NewDisguiseSounds.setSounds();
        }
    }

    public enum SoundType
    {
        CANCEL, DEATH, HURT, IDLE, STEP
    }

    public static DisguiseSound getType(String name)
    {
        try
        {
            return valueOf(name);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    private HashSet<String> cancelSounds = new HashSet<>();
    private float damageSoundVolume = 1F;
    private HashMap<SoundType, String> disguiseSounds = new HashMap<>();

    private DisguiseSound()
    {
    }

    public void setSounds(Object hurt, Object step, Object death, Object idle, Object... sounds)
    {
        addSound(hurt, SoundType.HURT);
        addSound(step, SoundType.STEP);
        addSound(death, SoundType.DEATH);
        addSound(idle, SoundType.IDLE);

        for (Object obj : sounds)
        {
            addSound(obj, SoundType.CANCEL);
        }
    }

    private void addSound(Object sound, SoundType type)
    {
        String s;

        if (sound == null)
        {
            return;
        }
        else if (sound instanceof String)
        {
            s = (String) sound;
        }
        else if (sound instanceof Sound)
        {
            s = ReflectionManager.getCraftSound((Sound) sound);
        }
        else
        {
            throw new RuntimeException("Was given a unknown object " + sound);
        }

        switch (type)
        {
        case HURT:
            disguiseSounds.put(SoundType.HURT, s);
            break;
        case STEP:
            disguiseSounds.put(SoundType.STEP, s);
            break;
        case DEATH:
            disguiseSounds.put(SoundType.DEATH, s);
            break;
        case IDLE:
            disguiseSounds.put(SoundType.IDLE, s);
            break;
        case CANCEL:
            cancelSounds.add(s);
        }
    }

    public float getDamageAndIdleSoundVolume()
    {
        return damageSoundVolume;
    }

    public String getSound(SoundType type)
    {
        if (type == null || !disguiseSounds.containsKey(type))
        {
            return null;
        }

        return disguiseSounds.get(type);
    }

    public HashSet<String> getSoundsToCancel()
    {
        return cancelSounds;
    }

    /**
     * Used to check if this sound name is owned by this disguise sound.
     */
    public SoundType getType(String sound, boolean ignoreDamage)
    {
        if (sound == null)
            return SoundType.CANCEL;

        if (isCancelSound(sound))
        {
            return SoundType.CANCEL;
        }

        if (disguiseSounds.containsKey(SoundType.STEP) && disguiseSounds.get(SoundType.STEP).startsWith("step.")
                && sound.startsWith("step."))
        {
            return SoundType.STEP;
        }

        for (SoundType type : SoundType.values())
        {
            if (!disguiseSounds.containsKey(type) || type == SoundType.DEATH || (ignoreDamage && type == SoundType.HURT))
            {
                continue;
            }

            String s = disguiseSounds.get(type);

            if (s != null)
            {
                if (s.equals(sound))
                {
                    return type;
                }
            }
        }

        return null;
    }

    public boolean isCancelSound(String sound)
    {
        return getSoundsToCancel().contains(sound);
    }

    public void removeSound(SoundType type, Sound sound)
    {
        removeSound(type, ReflectionManager.getCraftSound(sound));
    }

    public void removeSound(SoundType type, String sound)
    {
        if (type == SoundType.CANCEL)
        {
            cancelSounds.remove(sound);
        }
        else
        {
            disguiseSounds.remove(type);
        }
    }

    public void setDamageAndIdleSoundVolume(float strength)
    {
        this.damageSoundVolume = strength;
    }

    public void setSound(SoundType type, Sound sound)
    {
        setSound(type, ReflectionManager.getCraftSound(sound));
    }

    public void setSound(SoundType type, String sound)
    {
        if (type == SoundType.CANCEL)
        {
            cancelSounds.add(sound);
        }
        else
        {
            disguiseSounds.put(type, sound);
        }
    }
}
