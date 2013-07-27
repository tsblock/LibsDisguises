package me.libraryaddict.disguise.DisguiseTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseTypes.Watchers.AgeableWatcher;
import me.libraryaddict.disguise.DisguiseTypes.Watchers.ZombieWatcher;
import net.minecraft.server.v1_6_R2.Entity;
import net.minecraft.server.v1_6_R2.EntityAgeable;
import net.minecraft.server.v1_6_R2.EntityInsentient;
import net.minecraft.server.v1_6_R2.EntityLiving;
import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.EntityTrackerEntry;
import net.minecraft.server.v1_6_R2.ItemStack;
import net.minecraft.server.v1_6_R2.MathHelper;
import net.minecraft.server.v1_6_R2.EnumArt;
import net.minecraft.server.v1_6_R2.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_6_R2.inventory.CraftItemStack;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;

public class Disguise {
    private DisguiseType disguiseType;
    private org.bukkit.entity.Entity entity;
    private boolean replaceSounds;
    private BukkitRunnable runnable;
    private FlagWatcher watcher;

    protected Disguise(DisguiseType newType, boolean doSounds) {
        disguiseType = newType;
        replaceSounds = doSounds;
    }

    public Disguise clone() {
        Disguise disguise = new Disguise(getType(), replaceSounds());
        return disguise;
    }

    public PacketContainer[] constructPacket(org.bukkit.entity.Entity disguisedEntity) {
        PacketContainer[] spawnPackets = new PacketContainer[2];
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        Entity nmsEntity = ((CraftEntity) disguisedEntity).getHandle();
        Location loc = disguisedEntity.getLocation();
        if (getType() == DisguiseType.EXPERIENCE_ORB) {

            spawnPackets[0] = manager.createPacket(Packets.Server.ADD_EXP_ORB);
            StructureModifier<Object> mods = spawnPackets[0].getModifier();
            mods.write(0, disguisedEntity.getEntityId());
            mods.write(1, (int) Math.floor(loc.getX() * 32));
            mods.write(2, (int) Math.floor(loc.getY() * 32) + 2);
            mods.write(3, (int) Math.floor(loc.getZ() * 32));
            mods.write(4, 1);

        } else if (getType() == DisguiseType.PAINTING) {
            spawnPackets[0] = manager.createPacket(Packets.Server.ENTITY_PAINTING);
            StructureModifier<Object> mods = spawnPackets[0].getModifier();
            mods.write(0, disguisedEntity.getEntityId());
            mods.write(1, loc.getBlockX());
            mods.write(2, loc.getBlockY());
            mods.write(3, loc.getBlockZ());
            mods.write(4, ((int) loc.getYaw()) % 4);
            int id = ((MiscDisguise) this).getId();
            if (id == -1)
                id = new Random().nextInt(EnumArt.values().length);
            mods.write(5, EnumArt.values()[id].B);

            // Make the teleport packet to make it visible..
            spawnPackets[1] = manager.createPacket(Packets.Server.ENTITY_TELEPORT);
            mods = spawnPackets[1].getModifier();
            mods.write(0, disguisedEntity.getEntityId());
            mods.write(1, (int) Math.floor(loc.getX() * 32D));
            mods.write(2, (int) Math.floor(loc.getY() * 32D));
            mods.write(3, (int) Math.floor(loc.getZ() * 32D));
            mods.write(4, (byte) (int) (loc.getYaw() * 256.0F / 360.0F));
            mods.write(5, (byte) (int) (loc.getPitch() * 256.0F / 360.0F));

            // Need to fake a teleport packet as well to make the painting visible as a moving.

        } else if (getType().isPlayer()) {

            spawnPackets[0] = manager.createPacket(Packets.Server.NAMED_ENTITY_SPAWN);
            StructureModifier<Object> mods = spawnPackets[0].getModifier();
            mods.write(0, disguisedEntity.getEntityId());
            mods.write(1, ((PlayerDisguise) this).getName());
            mods.write(2, (int) Math.floor(loc.getX() * 32));
            mods.write(3, (int) Math.floor(loc.getY() * 32));
            mods.write(4, (int) Math.floor(loc.getZ() * 32));
            mods.write(5, (byte) (int) (loc.getYaw() * 256F / 360F));
            mods.write(6, (byte) (int) (loc.getPitch() * 256F / 360F));
            ItemStack item = null;
            if (disguisedEntity instanceof Player && ((Player) disguisedEntity).getItemInHand() != null) {
                item = CraftItemStack.asNMSCopy(((Player) disguisedEntity).getItemInHand());
            } else if (disguisedEntity instanceof LivingEntity) {
                item = CraftItemStack.asNMSCopy(((CraftLivingEntity) disguisedEntity).getEquipment().getItemInHand());
            }
            mods.write(7, (item == null ? 0 : item.id));
            mods.write(8, nmsEntity.getDataWatcher());

        } else if (getType().isMob()) {

            Vector vec = disguisedEntity.getVelocity();
            spawnPackets[0] = manager.createPacket(Packets.Server.MOB_SPAWN);
            StructureModifier<Object> mods = spawnPackets[0].getModifier();
            mods.write(0, disguisedEntity.getEntityId());
            mods.write(1, (int) getType().getEntityType().getTypeId());
            double d1 = 3.9D;
            double d2 = vec.getX();
            double d3 = vec.getY();
            double d4 = vec.getZ();
            if (d2 < -d1)
                d2 = -d1;
            if (d3 < -d1)
                d3 = -d1;
            if (d4 < -d1)
                d4 = -d1;
            if (d2 > d1)
                d2 = d1;
            if (d3 > d1)
                d3 = d1;
            if (d4 > d1)
                d4 = d1;
            mods.write(2, nmsEntity.at.a(loc.getX()));
            mods.write(3, (int) Math.floor(loc.getY() * 32D));
            mods.write(4, nmsEntity.at.a(loc.getZ()));
            mods.write(5, (int) (d2 * 8000.0D));
            mods.write(6, (int) (d3 * 8000.0D));
            mods.write(7, (int) (d4 * 8000.0D));
            byte yawValue = (byte) (int) (loc.getYaw() * 256.0F / 360.0F);
            if (getType() == DisguiseType.ENDER_DRAGON)
                yawValue -= 128;
            mods.write(8, yawValue);
            mods.write(9, (byte) (int) (loc.getPitch() * 256.0F / 360.0F));
            if (nmsEntity instanceof EntityLiving)
                mods.write(10, (byte) (int) (((EntityLiving) nmsEntity).aA * 256.0F / 360.0F));
            mods.write(11, nmsEntity.getDataWatcher());
            // Theres a list sometimes written with this. But no problems have appeared!
            // Probably just the metadata to be sent. But the next meta packet after fixes that anyways.

        } else if (getType().isMisc()) {

            int id = getType().getEntityId();
            int data = 0;
            if (((MiscDisguise) this).getId() >= 0)
                if (((MiscDisguise) this).getData() >= 0)
                    data = (((MiscDisguise) this).getId() | ((MiscDisguise) this).getData() << 16);
                else
                    data = ((MiscDisguise) this).getId();
            // This won't actually work. But you can still do it when constructing a disguise
            if (getType() == DisguiseType.FISHING_HOOK)
                data = getEntity().getEntityId();
            else if (getType() == DisguiseType.ITEM_FRAME)
                data = (int) Math.abs(loc.getYaw() % 4);
            spawnPackets[0] = manager.createPacket(Packets.Server.VEHICLE_SPAWN);
            StructureModifier<Object> mods = spawnPackets[0].getModifier();
            mods.write(0, disguisedEntity.getEntityId());
            mods.write(1, (int) Math.floor(loc.getX() * 32D));
            mods.write(2, (int) Math.floor(loc.getY() * 32D));
            mods.write(3, (int) Math.floor(loc.getZ() * 32D));
            if (data > 0) {
                Vector vec = disguisedEntity.getVelocity();
                double d1 = vec.getX();
                double d2 = vec.getY();
                double d3 = vec.getZ();
                double d4 = 3.9D;
                if (d1 < -d4)
                    d1 = -d4;
                if (d2 < -d4)
                    d2 = -d4;
                if (d3 < -d4)
                    d3 = -d4;
                if (d1 > d4)
                    d1 = d4;
                if (d2 > d4)
                    d2 = d4;
                if (d3 > d4)
                    d3 = d4;
                mods.write(4, (int) (d1 * 8000.0D));
                mods.write(5, (int) (d2 * 8000.0D));
                mods.write(6, (int) (d3 * 8000.0D));
            }
            mods.write(7, (int) MathHelper.floor(loc.getPitch() * 256.0F / 360.0F));
            mods.write(8, (int) MathHelper.floor(loc.getYaw() * 256.0F / 360.0F) - 64);
            mods.write(9, id);
            mods.write(10, data);

        }
        if (spawnPackets[1] == null) {
            // Make a packet to turn his head!
            spawnPackets[1] = manager.createPacket(Packets.Server.ENTITY_HEAD_ROTATION);
            StructureModifier<Object> mods = spawnPackets[1].getModifier();
            mods.write(0, disguisedEntity.getEntityId());
            mods.write(1, (byte) (int) Math.floor(loc.getYaw() * 256.0F / 360.0F));
        }
        return spawnPackets;
    }

    public void constructWatcher(JavaPlugin plugin, final org.bukkit.entity.Entity entity) {
        if (this.entity != null)
            throw new RuntimeException("This disguise is already in use! Try .clone()");
        this.entity = entity;
        FlagWatcher tempWatcher = null;
        try {
            tempWatcher = (FlagWatcher) getType().getWatcherClass().getConstructor(Disguise.class).newInstance(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (this instanceof MobDisguise && !((MobDisguise) this).isAdult()) {
            if (tempWatcher instanceof AgeableWatcher)
                tempWatcher.setValue(12, -24000);
            else if (tempWatcher instanceof ZombieWatcher)
                tempWatcher.setValue(12, (byte) 1);
        }
        if (getType() == DisguiseType.WITHER_SKELETON)
            tempWatcher.setValue(13, (byte) 1);
        else if (getType() == DisguiseType.ZOMBIE_VILLAGER)
            tempWatcher.setValue(13, (byte) 1);
        else
            try {
                Variant horseType = Variant.valueOf(getType().name());
                tempWatcher.setValue(19, (byte) horseType.ordinal());
            } catch (Exception ex) {
                // Ok.. So it aint a horse
            }
        Class entityClass = Values.getEntityClass(DisguiseType.getType(getEntity().getType()));
        Class disguiseClass = Values.getEntityClass(getType());
        HashMap<Integer, Object> disguiseValues = Values.getMetaValues(getType());
        HashMap<Integer, Object> entityValues = Values.getMetaValues(DisguiseType.getType(entity.getType()));
        // Start from 2 as they ALL share 0 and 1
        for (int dataNo = 2; dataNo <= 31; dataNo++) {
            // If the watcher already set a metadata on this
            if (tempWatcher.getValue(dataNo, null) != null) {
                // Better check that the value is stable.
                if (disguiseValues.containsKey(dataNo)
                        && tempWatcher.getValue(dataNo, null).getClass() == disguiseValues.get(dataNo).getClass()) {
                    // The classes are the same. The client "shouldn't" crash.
                    continue;
                }
            }
            // If neither of them touch it
            if (!entityValues.containsKey(dataNo) && !disguiseValues.containsKey(dataNo))
                continue;
            // If the disguise has this, but not the entity. Then better set it!
            if (!entityValues.containsKey(dataNo) && disguiseValues.containsKey(dataNo)) {
                tempWatcher.setValue(dataNo, disguiseValues.get(dataNo));
                continue;
            }
            // Else if the disguise doesn't have it. But the entity does. Better remove it!
            if (entityValues.containsKey(dataNo) && !disguiseValues.containsKey(dataNo)) {
                tempWatcher.setValue(dataNo, null);
                continue;
            }
            // Hmm. They both have the datavalue. Time to check if they have different default values!
            if (entityValues.get(dataNo) != disguiseValues.get(dataNo)
                    || !entityValues.get(dataNo).equals(disguiseValues.get(dataNo))) {
                // They do! Set the default value!
                tempWatcher.setValue(dataNo, disguiseValues.get(dataNo));
                continue;
            }
            // Hmm. They both now have data values which are exactly the same. I need to do more intensive background checks.
            // I HAVE to find juicy gossip on these!
            // Maybe if I check that they extend each other..
            // Seeing as I only store the finished forms of entitys. This should raise no problems and allow for more shared
            // datawatchers.
            if (entityClass.isAssignableFrom(disguiseClass) || disguiseClass.isAssignableFrom(entityClass))
                continue;

            // Entity is 0 & 1 - But we aint gonna be checking that
            // EntityAgeable is 16
            // EntityInsentient is 10 & 11
            // EntityZombie is 12 & 13 & 14 - But
            // EntityLiving is 6 & 7 & 8 & 9

            // Lets use switch
            Class baseClass = null;
            switch (dataNo) {
            case 6:
            case 7:
            case 8:
            case 9:
                baseClass = EntityLiving.class;
                break;
            case 10:
            case 11:
                baseClass = EntityInsentient.class;
                break;
            case 16:
                baseClass = EntityAgeable.class;
                break;
            default:
                break;
            }
            // If they both extend the same base class. They OBVIOUSLY share the same datavalue. Right..?
            if (baseClass != null && baseClass.isAssignableFrom(disguiseClass) && baseClass.isAssignableFrom(entityClass))
                continue;
            // Well I can't find a reason I should leave it alone. They will probably conflict.
            // Time to set the value to the disguises value so no conflicts!
            tempWatcher.setValue(dataNo, disguiseValues.get(dataNo));
        }
        watcher = tempWatcher;
        double fallSpeed = 0.0050;
        boolean doesntMove = false;
        boolean movement = false;
        switch (getType()) {
        case ARROW:
        case BAT:
        case BOAT:
        case ENDER_CRYSTAL:
        case ENDER_DRAGON:
        case GHAST:
        case ITEM_FRAME:
        case MINECART:
        case MINECART_CHEST:
        case MINECART_FURNACE:
        case MINECART_HOPPER:
        case MINECART_MOB_SPAWNER:
        case MINECART_TNT:
        case PAINTING:
        case PLAYER:
        case SQUID:
            doesntMove = true;
            break;
        case DROPPED_ITEM:
        case EXPERIENCE_ORB:
        case MAGMA_CUBE:
        case PRIMED_TNT:
            fallSpeed = 0.2;
            movement = true;
            break;
        case WITHER:
        case FALLING_BLOCK:
            fallSpeed = 0.04;
            break;
        case SPIDER:
        case CAVE_SPIDER:
            fallSpeed = 0.0040;
            break;
        case EGG:
        case ENDER_PEARL:
        case ENDER_SIGNAL:
        case FIREBALL:
        case SMALL_FIREBALL:
        case SNOWBALL:
        case SPLASH_POTION:
        case THROWN_EXP_BOTTLE:
        case WITHER_SKULL:
            fallSpeed = 0.0005;
            break;
        case FIREWORK:
            fallSpeed = -0.040;
            break;
        default:
            break;
        }
        final boolean sendMovementPacket = movement;
        final boolean sendVector = !doesntMove;
        final double vectorY = fallSpeed;
        // A scheduler to clean up any unused disguises.
        runnable = new BukkitRunnable() {
            private int i = 0;

            public void run() {
                if (!entity.isValid()) {
                    DisguiseAPI.undisguiseToAll(entity);
                } else {
                    if (getType() == DisguiseType.PRIMED_TNT) {
                        i++;
                        if (i % 40 == 0) {
                            List<Player> players = new ArrayList<Player>();
                            for (EntityPlayer p : getPerverts())
                                players.add(p.getBukkitEntity());
                            ProtocolLibrary.getProtocolManager().updateEntity(getEntity(), players);
                        }
                    }
                    if (sendVector && DisguiseAPI.isVelocitySent() && !entity.isOnGround()) {
                        Vector vector = entity.getVelocity();
                        if (vector.getY() != 0)
                            return;

                        for (EntityPlayer player : getPerverts()) {
                            if (entity != player) {
                                PacketContainer packet = new PacketContainer(Packets.Server.ENTITY_VELOCITY);
                                StructureModifier<Object> mods = packet.getModifier();
                                mods.write(0, entity.getEntityId());
                                mods.write(1, (int) (vector.getX() * 8000));
                                mods.write(2, (int) (8000 * (vectorY * (double) player.ping * 0.069)));
                                mods.write(3, (int) (vector.getZ() * 8000));
                                try {
                                    ProtocolLibrary.getProtocolManager()
                                            .sendServerPacket(player.getBukkitEntity(), packet, false);
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (sendMovementPacket) {
                            PacketContainer packet = new PacketContainer(Packets.Server.REL_ENTITY_MOVE);
                            StructureModifier<Object> mods = packet.getModifier();
                            mods.write(0, entity.getEntityId());
                            for (EntityPlayer player : getPerverts()) {
                                if (entity != player) {
                                    try {
                                        ProtocolLibrary.getProtocolManager().sendServerPacket(player.getBukkitEntity(), packet,
                                                false);
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
        runnable.runTaskTimer(plugin, 1, 1);
    }

    public boolean equals(Disguise disguise) {
        if (getType() != disguise.getType())
            return false;
        if (replaceSounds() != disguise.replaceSounds())
            return false;
        if (!getWatcher().equals(disguise.getWatcher()))
            return false;
        return true;
    }

    public org.bukkit.entity.Entity getEntity() {
        return entity;
    }

    protected EntityPlayer[] getPerverts() {
        EntityTrackerEntry entry = (EntityTrackerEntry) ((WorldServer) ((CraftEntity) entity).getHandle().world).tracker.trackedEntities
                .get(entity.getEntityId());
        if (entry != null) {
            EntityPlayer[] players = (EntityPlayer[]) entry.trackedPlayers.toArray(new EntityPlayer[entry.trackedPlayers.size()]);
            return players;
        }
        return new EntityPlayer[0];
    }

    public BukkitRunnable getScheduler() {
        return runnable;
    }

    public DisguiseType getType() {
        return disguiseType;
    }

    public FlagWatcher getWatcher() {
        return watcher;
    }

    public boolean replaceSounds() {
        return replaceSounds;
    }

    public boolean isMobDisguise() {
        return this instanceof MobDisguise;
    }

    public boolean isMiscDisguise() {
        return this instanceof MiscDisguise;
    }

    public boolean isPlayerDisguise() {
        return this instanceof PlayerDisguise;
    }

    public void setReplaceSounds(boolean areSoundsReplaced) {
        replaceSounds = areSoundsReplaced;
    }

    public void setWatcher(FlagWatcher newWatcher) {
        watcher = newWatcher;
    }
}