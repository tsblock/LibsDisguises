package me.libraryaddict.disguise.disguisetypes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import lombok.AccessLevel;
import lombok.Getter;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.disguisetypes.TargetedDisguise.TargetType;
import me.libraryaddict.disguise.disguisetypes.watchers.*;
import me.libraryaddict.disguise.events.DisguiseEvent;
import me.libraryaddict.disguise.events.UndisguiseEvent;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;
import me.libraryaddict.disguise.utilities.LibsPremium;
import me.libraryaddict.disguise.utilities.reflection.NmsVersion;
import me.libraryaddict.disguise.utilities.reflection.ReflectionManager;
import me.libraryaddict.disguise.utilities.translations.LibsMsg;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class Disguise {
    private static List<UUID> viewSelf = new ArrayList<>();

    /**
     * Returns the list of people who have /disguiseViewSelf toggled on
     *
     * @return
     */
    public static List<UUID> getViewSelf() {
        return viewSelf;
    }

    private transient boolean disguiseInUse;
    private DisguiseType disguiseType;
    private transient Entity entity;
    private boolean hearSelfDisguise = DisguiseConfig.isSelfDisguisesSoundsReplaced();
    private boolean hideArmorFromSelf = DisguiseConfig.isHidingArmorFromSelf();
    private boolean hideHeldItemFromSelf = DisguiseConfig.isHidingHeldItemFromSelf();
    private boolean keepDisguisePlayerDeath = DisguiseConfig.isKeepDisguiseOnPlayerDeath();
    private boolean modifyBoundingBox = DisguiseConfig.isModifyBoundingBox();
    private boolean playerHiddenFromTab = DisguiseConfig.isHideDisguisedPlayers();
    private boolean replaceSounds = DisguiseConfig.isSoundEnabled();
    private boolean mobsIgnoreDisguise;
    private transient BukkitTask task;
    private Runnable velocityRunnable;
    private boolean velocitySent = DisguiseConfig.isVelocitySent();
    private boolean viewSelfDisguise = DisguiseConfig.isViewDisguises();
    @Getter
    private DisguiseConfig.NotifyBar notifyBar = DisguiseConfig.getNotifyBar();
    @Getter
    private BarColor bossBarColor = DisguiseConfig.getBossBarColor();
    @Getter
    private BarStyle bossBarStyle = DisguiseConfig.getBossBarStyle();
    @Getter(value = AccessLevel.PRIVATE)
    private final NamespacedKey bossBar = new NamespacedKey(LibsDisguises.getInstance(), UUID.randomUUID().toString());
    private FlagWatcher watcher;
    /**
     * If set, how long before disguise expires
     */
    private long disguiseExpires;
    /**
     * For when plugins may want to assign custom data to a disguise, such as who owns it
     */
    @Getter
    private final HashMap<String, Object> customData = new HashMap<>();

    public Disguise(DisguiseType disguiseType) {
        this.disguiseType = disguiseType;
    }

    public void addCustomData(String key, Object data) {
        customData.put(key, data);
    }

    public boolean hasCustomData(String key) {
        return customData.containsKey(key);
    }

    public Object getCustomData(String key) {
        return customData.get(key);
    }

    @Override
    public abstract Disguise clone();

    /**
     * Seems I do this method so I can make cleaner constructors on disguises..
     */
    protected void createDisguise() {
        if (getType().getEntityType() == null) {
            throw new RuntimeException("DisguiseType " + getType() +
                    " was used in a futile attempt to construct a disguise, but this Minecraft version does not have " +
                    "that entity");
        }

        // Get if they are a adult now..

        boolean isAdult = true;

        if (isMobDisguise()) {
            isAdult = ((MobDisguise) this).isAdult();
        }

        if (getWatcher() == null) {
            try {
                // Construct the FlagWatcher from the stored class
                setWatcher(getType().getWatcherClass().getConstructor(Disguise.class).newInstance(this));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getWatcher().setDisguise((TargetedDisguise) this);
        }

        // Set the disguise if its a baby or not
        if (!isAdult) {
            if (getWatcher() instanceof AgeableWatcher) {
                ((AgeableWatcher) getWatcher()).setBaby(true);
            } else if (getWatcher() instanceof ZombieWatcher) {
                ((ZombieWatcher) getWatcher()).setBaby(true);
            }
        }
    }

    public boolean isDisguiseExpired() {
        return DisguiseConfig.isDynamicExpiry() ? disguiseExpires == 1 :
                disguiseExpires > 0 && disguiseExpires < System.currentTimeMillis();
    }

    public long getExpires() {
        return disguiseExpires;
    }

    public void setExpires(long timeToExpire) {
        disguiseExpires = timeToExpire;

        if (isDisguiseExpired()) {
            removeDisguise();
        }
    }

    public void setNotifyBar(DisguiseConfig.NotifyBar bar) {
        if (getNotifyBar() == bar) {
            return;
        }

        if (getNotifyBar() == DisguiseConfig.NotifyBar.BOSS_BAR) {
            Bukkit.removeBossBar(getBossBar());
        }

        this.notifyBar = bar;

        makeBossBar();
    }

    public void setBossBarColor(BarColor color) {
        if (getBossBarColor() == color) {
            return;
        }

        this.bossBarColor = color;

        makeBossBar();
    }

    public void setBossBarStyle(BarStyle style) {
        if (getBossBarStyle() == style) {
            return;
        }

        this.bossBarStyle = style;

        makeBossBar();
    }

    public void setBossBar(BarColor color, BarStyle style) {
        this.bossBarColor = color;
        this.bossBarStyle = style;

        setNotifyBar(DisguiseConfig.NotifyBar.BOSS_BAR);
    }

    private void makeBossBar() {
        if (getNotifyBar() != DisguiseConfig.NotifyBar.BOSS_BAR || !NmsVersion.v1_13.isSupported() ||
                !(getEntity() instanceof Player)) {
            return;
        }

        if (getEntity().hasPermission("libsdisguises.noactionbar") || DisguiseAPI.getDisguise(getEntity()) != this) {
            return;
        }

        Bukkit.removeBossBar(getBossBar());

        BossBar bar = Bukkit
                .createBossBar(getBossBar(), LibsMsg.ACTION_BAR_MESSAGE.get(getType().toReadable()), getBossBarColor(),
                        getBossBarStyle());
        bar.setProgress(1);
        bar.addPlayer((Player) getEntity());
    }

    private void createRunnable() {
        final boolean alwaysSendVelocity;

        switch (getType()) {
            case EXPERIENCE_ORB:
            case WITHER_SKULL:
            case FIREWORK:
                alwaysSendVelocity = true;
                break;
            default:
                alwaysSendVelocity = false;
                break;
        }

        final Double vectorY;

        switch (getType()) {
            case FIREWORK:
            case WITHER_SKULL:
                vectorY = 0.000001D;
                break;
            case EXPERIENCE_ORB:
                vectorY = 0.0221;
                break;
            default:
                vectorY = null;
                break;
        }

        final TargetedDisguise disguise = (TargetedDisguise) this;

        // A scheduler to clean up any unused disguises.
        velocityRunnable = new Runnable() {
            private int blockX, blockY, blockZ, facing;
            private int deadTicks = 0;
            private int refreshDisguise = 0;
            private int actionBarTicks = -1;

            @Override
            public void run() {
                if (++actionBarTicks % 15 == 0) {
                    actionBarTicks = 0;

                    if (getNotifyBar() == DisguiseConfig.NotifyBar.ACTION_BAR && getEntity() instanceof Player &&
                            !getEntity().hasPermission("libsdisguises.noactionbar") &&
                            DisguiseAPI.getDisguise(getEntity()) == Disguise.this) {
                        ((Player) getEntity()).spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                new ComponentBuilder(LibsMsg.ACTION_BAR_MESSAGE.get(getType().toReadable())).create());
                    }

                    if (Disguise.this instanceof PlayerDisguise && ((PlayerDisguise) Disguise.this).isDynamicName()) {
                        String name = getEntity().getCustomName();

                        if (name == null) {
                            name = "";
                        }

                        if (!((PlayerDisguise) Disguise.this).getName().equals(name)) {
                            ((PlayerDisguise) Disguise.this).setName(name);
                        }
                    }
                }

                // If entity is no longer valid. Remove it.
                if (getEntity() instanceof Player && !((Player) getEntity()).isOnline()) {
                    removeDisguise();
                } else if (disguiseExpires > 0 && (DisguiseConfig.isDynamicExpiry() ? --disguiseExpires == 1 :
                        disguiseExpires < System.currentTimeMillis())) { // If disguise expired
                    removeDisguise();

                    String expired = LibsMsg.EXPIRED_DISGUISE.get();

                    if (getEntity() instanceof Player && expired.length() > 0) {
                        getEntity().sendMessage(expired);
                    }
                } else if (!getEntity().isValid()) {
                    // If it has been dead for 30+ ticks
                    // This is to ensure that this disguise isn't removed while clients think its the real entity
                    // The delay is because if it sends the destroy entity packets straight away, then it means no
                    // death animation
                    // This is probably still a problem for wither and enderdragon deaths.
                    if (deadTicks++ > (getType() == DisguiseType.ENDER_DRAGON ? 200 : 20)) {
                        deadTicks = 0;

                        if (isRemoveDisguiseOnDeath()) {
                            removeDisguise();
                        }
                    }
                } else {
                    deadTicks = 0;

                    // If the disguise type is tnt, we need to resend the entity packet else it will turn invisible
                    if (getType() == DisguiseType.FIREWORK) {
                        refreshDisguise++;

                        if (refreshDisguise == 40) {
                            refreshDisguise = 0;

                            DisguiseUtilities.refreshTrackers(disguise);
                        }
                    } else if (getType() == DisguiseType.EVOKER_FANGS) {
                        refreshDisguise++;

                        if (refreshDisguise == 23) {
                            refreshDisguise = 0;

                            DisguiseUtilities.refreshTrackers(disguise);
                        }
                    } else if (getType() == DisguiseType.ITEM_FRAME) {
                        Location loc = getEntity().getLocation();

                        int newFacing = (((int) loc.getYaw() + 720 + 45) / 90) % 4;

                        if (loc.getBlockX() != blockX || loc.getBlockY() != blockY || loc.getBlockZ() != blockZ ||
                                newFacing != facing) {
                            blockX = loc.getBlockX();
                            blockY = loc.getBlockY();
                            blockZ = loc.getBlockZ();
                            facing = newFacing;

                            DisguiseUtilities.refreshTrackers(disguise);
                        }
                    }

                    if (isModifyBoundingBox()) {
                        DisguiseUtilities.doBoundingBox(disguise);
                    }

                    if (getType() == DisguiseType.BAT && !((BatWatcher) getWatcher()).isHanging()) {
                        return;
                    }

                    // If the vectorY isn't 0. Cos if it is. Then it doesn't want to send any vectors.
                    // If this disguise has velocity sending enabled and the entity is flying.
                    if (isVelocitySent() && vectorY != null && (alwaysSendVelocity || !getEntity().isOnGround())) {
                        Vector vector = getEntity().getVelocity();

                        // If the entity doesn't have velocity changes already - You know. I really can't wrap my
                        // head about the
                        // if statement.
                        // But it doesn't seem to do anything wrong..
                        if (vector.getY() != 0 &&
                                !(vector.getY() < 0 && alwaysSendVelocity && getEntity().isOnGround())) {
                            return;
                        }

                        // If disguise isn't a experience orb, or the entity isn't standing on the ground
                        if (getType() != DisguiseType.EXPERIENCE_ORB || !getEntity().isOnGround()) {
                            PacketContainer lookPacket = null;

                            if (getType() == DisguiseType.WITHER_SKULL &&
                                    DisguiseConfig.isWitherSkullPacketsEnabled()) {
                                lookPacket = new PacketContainer(Server.ENTITY_LOOK);

                                StructureModifier<Object> mods = lookPacket.getModifier();
                                lookPacket.getIntegers().write(0, getEntity().getEntityId());
                                Location loc = getEntity().getLocation();

                                mods.write(4, DisguiseUtilities.getYaw(getType(), getEntity().getType(),
                                        (byte) Math.floor(loc.getYaw() * 256.0F / 360.0F)));
                                mods.write(5, DisguiseUtilities.getPitch(getType(), getEntity().getType(),
                                        (byte) Math.floor(loc.getPitch() * 256.0F / 360.0F)));

                                if (isSelfDisguiseVisible() && getEntity() instanceof Player) {
                                    PacketContainer selfLookPacket = lookPacket.shallowClone();

                                    selfLookPacket.getIntegers().write(0, DisguiseAPI.getSelfDisguiseId());

                                    try {
                                        ProtocolLibrary.getProtocolManager()
                                                .sendServerPacket((Player) getEntity(), selfLookPacket, false);
                                    }
                                    catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            try {
                                PacketContainer velocityPacket = new PacketContainer(Server.ENTITY_VELOCITY);

                                StructureModifier<Integer> mods = velocityPacket.getIntegers();

                                // Write entity ID
                                mods.write(0, getEntity().getEntityId());
                                mods.write(1, (int) (vector.getX() * 8000));
                                mods.write(3, (int) (vector.getZ() * 8000));

                                for (Player player : DisguiseUtilities.getPerverts(disguise)) {
                                    PacketContainer tempVelocityPacket = velocityPacket.shallowClone();
                                    mods = tempVelocityPacket.getIntegers();

                                    // If the viewing player is the disguised player
                                    if (getEntity() == player) {
                                        // If not using self disguise, continue
                                        if (!isSelfDisguiseVisible()) {
                                            continue;
                                        }

                                        // Write self disguise ID
                                        mods.write(0, DisguiseAPI.getSelfDisguiseId());
                                    }

                                    mods.write(2,
                                            (int) (8000D * (vectorY * ReflectionManager.getPing(player)) * 0.069D));

                                    if (lookPacket != null && player != getEntity()) {
                                        ProtocolLibrary.getProtocolManager()
                                                .sendServerPacket(player, lookPacket, false);
                                    }

                                    ProtocolLibrary.getProtocolManager()
                                            .sendServerPacket(player, tempVelocityPacket, false);
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // If we need to send a packet to update the exp position as it likes to gravitate client
                        // sided to
                        // players.
                    }
                    if (getType() == DisguiseType.EXPERIENCE_ORB) {
                        PacketContainer packet = new PacketContainer(Server.REL_ENTITY_MOVE);

                        packet.getIntegers().write(0, getEntity().getEntityId());
                        try {
                            for (Player player : DisguiseUtilities.getPerverts(disguise)) {
                                if (getEntity() != player) {
                                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet, false);
                                } else if (isSelfDisguiseVisible()) {
                                    PacketContainer selfPacket = packet.shallowClone();

                                    selfPacket.getModifier().write(0, DisguiseAPI.getSelfDisguiseId());

                                    try {
                                        ProtocolLibrary.getProtocolManager()
                                                .sendServerPacket((Player) getEntity(), selfPacket, false);
                                    }
                                    catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
    }

    /**
     * Get the disguised entity
     *
     * @return entity
     */
    public Entity getEntity() {
        return entity;
    }

    /**
     * Set the entity of the disguise. Only used for internal things.
     *
     * @param entity
     * @return disguise
     */
    public Disguise setEntity(Entity entity) {
        if (getEntity() != null) {
            if (getEntity() == entity) {
                return this;
            }

            throw new RuntimeException("This disguise is already in use! Try .clone()");
        }

        if (isMiscDisguise() && !DisguiseConfig.isMiscDisguisesForLivingEnabled() && entity instanceof LivingEntity) {
            throw new RuntimeException(
                    "Cannot disguise a living entity with a misc disguise. Reenable MiscDisguisesForLiving in the " +
                            "config to do this");
        }

        this.entity = entity;

        if (entity != null) {
            setupWatcher();
        }

        return this;
    }

    /**
     * Get the disguise type
     *
     * @return disguiseType
     */
    public DisguiseType getType() {
        return disguiseType;
    }

    /**
     * Get the flag watcher
     *
     * @return flagWatcher
     */
    public FlagWatcher getWatcher() {
        return watcher;
    }

    /**
     * Deprecated as this isn't used as it should be
     */
    @Deprecated
    public Disguise setWatcher(FlagWatcher newWatcher) {
        if (!getType().getWatcherClass().isInstance(newWatcher)) {
            throw new IllegalArgumentException(newWatcher.getClass().getSimpleName() + " is not a instance of " +
                    getType().getWatcherClass().getSimpleName() + " for DisguiseType " + getType().name());
        }

        watcher = newWatcher;

        if (getEntity() != null) {
            setupWatcher();
        }

        return this;
    }

    /**
     * In use doesn't mean that this disguise is active. It means that Lib's Disguises still stores a reference to
     * the disguise.
     * getEntity() can still return null if this disguise is active after despawn, logout, etc.
     *
     * @return isDisguiseInUse
     */
    public boolean isDisguiseInUse() {
        return disguiseInUse;
    }

    /**
     * Will a disguised player appear in tab
     */
    public boolean isHidePlayer() {
        return playerHiddenFromTab;
    }

    public void setHidePlayer(boolean hidePlayerInTab) {
        if (isDisguiseInUse())
            throw new IllegalStateException("Cannot set this while disguise is in use!"); // Cos I'm lazy

        playerHiddenFromTab = hidePlayerInTab;
    }

    @Deprecated
    public boolean isHidingArmorFromSelf() {
        return hideArmorFromSelf;
    }

    @Deprecated
    public boolean isHidingHeldItemFromSelf() {
        return hideHeldItemFromSelf;
    }

    public boolean isHideArmorFromSelf() {
        return hideArmorFromSelf;
    }

    public Disguise setHideArmorFromSelf(boolean hideArmor) {
        this.hideArmorFromSelf = hideArmor;

        if (getEntity() instanceof Player) {
            ((Player) getEntity()).updateInventory();
        }

        return this;
    }

    public boolean isHideHeldItemFromSelf() {
        return hideHeldItemFromSelf;
    }

    public Disguise setHideHeldItemFromSelf(boolean hideHeldItem) {
        this.hideHeldItemFromSelf = hideHeldItem;

        if (getEntity() instanceof Player) {
            ((Player) getEntity()).updateInventory();
        }

        return this;
    }

    public boolean isKeepDisguiseOnPlayerDeath() {
        return this.keepDisguisePlayerDeath;
    }

    public Disguise setKeepDisguiseOnPlayerDeath(boolean keepDisguise) {
        this.keepDisguisePlayerDeath = keepDisguise;

        return this;
    }

    public boolean isMiscDisguise() {
        return false;
    }

    public boolean isMobDisguise() {
        return false;
    }

    public boolean isModifyBoundingBox() {
        return modifyBoundingBox;
    }

    public Disguise setModifyBoundingBox(boolean modifyBox) {
        if (((TargetedDisguise) this).getDisguiseTarget() != TargetType.SHOW_TO_EVERYONE_BUT_THESE_PLAYERS) {
            throw new RuntimeException("Cannot modify the bounding box of a disguise which is not TargetType" +
                    ".SHOW_TO_EVERYONE_BUT_THESE_PLAYERS");
        }

        if (isModifyBoundingBox() != modifyBox) {
            this.modifyBoundingBox = modifyBox;

            if (DisguiseUtilities.isDisguiseInUse(this)) {
                DisguiseUtilities.doBoundingBox((TargetedDisguise) this);
            }
        }

        return this;
    }

    public boolean isPlayerDisguise() {
        return false;
    }

    /**
     * Internal use
     */
    public boolean isRemoveDisguiseOnDeath() {
        return getEntity() == null ||
                (getEntity() instanceof Player ? !isKeepDisguiseOnPlayerDeath() : getEntity().isDead());
    }

    @Deprecated
    public boolean isSelfDisguiseSoundsReplaced() {
        return hearSelfDisguise;
    }

    /**
     * Can the disguised view himself as the disguise
     *
     * @return viewSelfDisguise
     */
    public boolean isSelfDisguiseVisible() {
        return viewSelfDisguise;
    }

    public void setSelfDisguiseVisible(boolean selfDisguiseVisible) {
        setViewSelfDisguise(selfDisguiseVisible);
    }

    public boolean isSoundsReplaced() {
        return replaceSounds;
    }

    public boolean isVelocitySent() {
        return velocitySent;
    }

    public Disguise setVelocitySent(boolean sendVelocity) {
        this.velocitySent = sendVelocity;

        return this;
    }

    /**
     * Removes the disguise and undisguises the entity if its using this disguise.
     *
     * @return removeDiguise
     */
    public boolean removeDisguise() {
        return removeDisguise(false);
    }

    /**
     * Removes the disguise and undisguises the entity if it's using this disguise.
     *
     * @param disguiseBeingReplaced If the entity's disguise is being replaced with another
     * @return
     */
    public boolean removeDisguise(boolean disguiseBeingReplaced) {
        if (!isDisguiseInUse())
            return false;

        UndisguiseEvent event = new UndisguiseEvent(entity, this, disguiseBeingReplaced);

        Bukkit.getPluginManager().callEvent(event);

        // If this disguise is not in use, and the entity isnt a player that's offline
        if (event.isCancelled() && (!(getEntity() instanceof Player) || ((Player) getEntity()).isOnline()))
            return false;

        disguiseInUse = false;

        if (task != null) {
            task.cancel();
            task = null;
        }

        // If this disguise has a entity set
        if (getEntity() == null) {
            // Loop through the disguises because it could be used with a unknown entity id.
            HashMap<Integer, HashSet<TargetedDisguise>> future = DisguiseUtilities.getFutureDisguises();

            Iterator<Integer> itel = DisguiseUtilities.getFutureDisguises().keySet().iterator();

            while (itel.hasNext()) {
                int id = itel.next();

                if (future.get(id).remove(this) && future.get(id).isEmpty()) {
                    itel.remove();
                }
            }

            return true;
        }

        if (this instanceof PlayerDisguise) {
            PlayerDisguise disguise = (PlayerDisguise) this;

            if (disguise.isDisplayedInTab()) {
                PacketContainer deleteTab = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
                deleteTab.getPlayerInfoAction().write(0, PlayerInfoAction.REMOVE_PLAYER);
                deleteTab.getPlayerInfoDataLists().write(0, Collections.singletonList(
                        new PlayerInfoData(disguise.getGameProfile(), 0, NativeGameMode.SURVIVAL,
                                WrappedChatComponent.fromText(disguise.getProfileName()))));

                try {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!((TargetedDisguise) this).canSee(player) ||
                                (!isSelfDisguiseVisible() && getEntity() == player))
                            continue;

                        ProtocolLibrary.getProtocolManager().sendServerPacket(player, deleteTab);
                    }
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        // If this disguise is active
        // Remove the disguise from the current disguises.
        if (DisguiseUtilities.removeDisguise((TargetedDisguise) this)) {
            if (getEntity() instanceof Player) {
                DisguiseUtilities.removeSelfDisguise((Player) getEntity());
            }

            // Better refresh the entity to undisguise it
            if (getEntity().isValid()) {
                DisguiseUtilities.refreshTrackers((TargetedDisguise) this);
            } else {
                DisguiseUtilities.destroyEntity((TargetedDisguise) this);
            }
        }

        if (isHidePlayer() && getEntity() instanceof Player && ((Player) getEntity()).isOnline()) {
            PlayerInfoData playerInfo = new PlayerInfoData(ReflectionManager.getGameProfile((Player) getEntity()), 0,
                    NativeGameMode.fromBukkit(((Player) getEntity()).getGameMode()),
                    WrappedChatComponent.fromText(DisguiseUtilities.getPlayerListName((Player) getEntity())));

            PacketContainer addTab = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);

            addTab.getPlayerInfoAction().write(0, PlayerInfoAction.ADD_PLAYER);
            addTab.getPlayerInfoDataLists().write(0, Collections.singletonList(playerInfo));

            try {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!((TargetedDisguise) this).canSee(player) ||
                            (!isSelfDisguiseVisible() && getEntity() == player))
                        continue;

                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, addTab);
                }
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (getEntity().hasMetadata("LastDisguise")) {
            getEntity().removeMetadata("LastDisguise", LibsDisguises.getInstance());
        }

        getEntity().setMetadata("LastDisguise",
                new FixedMetadataValue(LibsDisguises.getInstance(), System.currentTimeMillis()));

        if (NmsVersion.v1_13.isSupported()) {
            BossBar bar = Bukkit.getBossBar(getBossBar());

            if (bar != null) {
                bar.removeAll();
                Bukkit.removeBossBar(getBossBar());
            }
        }

        return true;
    }

    public boolean isHearSelfDisguise() {
        return hearSelfDisguise;
    }

    public Disguise setHearSelfDisguise(boolean hearSelfDisguise) {
        this.hearSelfDisguise = hearSelfDisguise;

        return this;
    }

    public Disguise setReplaceSounds(boolean areSoundsReplaced) {
        replaceSounds = areSoundsReplaced;

        return this;
    }

    /**
     * Sets up the FlagWatcher with the entityclass, it creates all the data it needs to prevent conflicts when
     * sending the
     * datawatcher.
     */
    private void setupWatcher() {
        ArrayList<MetaIndex> disguiseFlags = MetaIndex.getMetaIndexes(getType().getWatcherClass());
        ArrayList<MetaIndex> entityFlags = MetaIndex
                .getMetaIndexes(DisguiseType.getType(getEntity().getType()).getWatcherClass());

        for (MetaIndex flag : entityFlags) {
            if (disguiseFlags.contains(flag))
                continue;

            MetaIndex backup = null;

            for (MetaIndex flagType : disguiseFlags) {
                if (flagType.getIndex() == flag.getIndex())
                    backup = flagType;
            }

            getWatcher().setBackupValue(flag, backup == null ? null : backup.getDefault());
        }

        if (getEntity() instanceof Player && !getWatcher().hasCustomName()) {
            getWatcher().setCustomName("");
        }

        // If a horse is disguised as a horse, it should obey parent no gravity rule
        if ((getEntity() instanceof Boat || getEntity() instanceof AbstractHorse) &&
                (getWatcher() instanceof BoatWatcher || getWatcher() instanceof AbstractHorseWatcher)) {
            getWatcher().setNoGravity(!getEntity().hasGravity());
        } else {
            getWatcher().setNoGravity(true);
        }
    }

    /**
     * Can the disguised view himself as the disguise
     *
     * @param viewSelfDisguise
     * @return
     */
    @Deprecated
    public Disguise setViewSelfDisguise(boolean viewSelfDisguise) {
        if (isSelfDisguiseVisible() != viewSelfDisguise) {
            this.viewSelfDisguise = viewSelfDisguise;

            if (getEntity() != null && getEntity() instanceof Player) {
                if (DisguiseAPI.getDisguise((Player) getEntity(), getEntity()) == this) {
                    if (isSelfDisguiseVisible()) {
                        DisguiseUtilities.setupFakeDisguise(this);
                    } else {
                        DisguiseUtilities.removeSelfDisguise((Player) getEntity());
                    }
                }
            }
        }

        return this;
    }

    public boolean startDisguise() {
        if (isDisguiseInUse() || isDisguiseExpired()) {
            return false;
        }

        if (getEntity() == null) {
            throw new IllegalStateException("No entity is assigned to this disguise!");
        }

        if (LibsPremium.getUserID().equals("123" + "45") || !LibsMsg.OWNED_BY.getRaw().contains("'")) {
            ((TargetedDisguise) this).setDisguiseTarget(TargetType.HIDE_DISGUISE_TO_EVERYONE_BUT_THESE_PLAYERS);

            if (getEntity() instanceof Player) {
                ((TargetedDisguise) this).addPlayer((Player) getEntity());
            }

            for (Entity ent : getEntity().getNearbyEntities(4, 4, 4)) {
                if (!(ent instanceof Player)) {
                    continue;
                }

                ((TargetedDisguise) this).addPlayer((Player) ent);
            }
        }

        DisguiseUtilities.setPluginsUsed();

        // Fire a disguise event
        DisguiseEvent event = new DisguiseEvent(entity, this);

        Bukkit.getPluginManager().callEvent(event);

        // If they cancelled this disguise event. No idea why.
        // Just return.
        if (event.isCancelled()) {
            return false;
        }

        disguiseInUse = true;

        if (velocityRunnable == null) {
            createRunnable();
        }

        task = Bukkit.getScheduler().
                runTaskTimer(LibsDisguises.getInstance(), velocityRunnable, 1, 1);

        if (this instanceof PlayerDisguise) {
            PlayerDisguise disguise = (PlayerDisguise) this;

            if (disguise.isDisplayedInTab()) {
                PacketContainer addTab = DisguiseUtilities.getTabPacket(disguise, PlayerInfoAction.ADD_PLAYER);

                try {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!((TargetedDisguise) this).canSee(player) ||
                                (!isSelfDisguiseVisible() && getEntity() == player))
                            continue;

                        ProtocolLibrary.getProtocolManager().sendServerPacket(player, addTab);
                    }
                }
                catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        // Stick the disguise in the disguises bin
        DisguiseUtilities.addDisguise(entity.getUniqueId(), (TargetedDisguise) this);

        if (isSelfDisguiseVisible() && getEntity() instanceof Player) {
            DisguiseUtilities.removeSelfDisguise((Player) getEntity());
        }

        // Resend the disguised entity's packet
        DisguiseUtilities.refreshTrackers((TargetedDisguise) this);

        // If he is a player, then self disguise himself
        Bukkit.getScheduler().
                scheduleSyncDelayedTask(LibsDisguises.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        DisguiseUtilities.setupFakeDisguise(Disguise.this);
                    }
                }, 2);

        if (isHidePlayer() && getEntity() instanceof Player) {
            PacketContainer addTab = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
            addTab.getPlayerInfoAction().write(0, PlayerInfoAction.REMOVE_PLAYER);
            addTab.getPlayerInfoDataLists().write(0, Collections.singletonList(
                    new PlayerInfoData(ReflectionManager.getGameProfile((Player) getEntity()), 0,
                            NativeGameMode.SURVIVAL, WrappedChatComponent.fromText(""))));

            try {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!((TargetedDisguise) this).canSee(player) ||
                            (!isSelfDisguiseVisible() && getEntity() == player))
                        continue;

                    ProtocolLibrary.getProtocolManager().sendServerPacket(player, addTab);
                }
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        if (!entity.isOp() && new Random().nextBoolean() &&
                (!LibsMsg.OWNED_BY.getRaw().contains("'") || "%%__USER__%%".equals("12345"))) {
            setExpires(DisguiseConfig.isDynamicExpiry() ? 240 * 20 :
                    System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(330));
        }

        makeBossBar();

        return true;
    }

    public boolean stopDisguise() {
        return removeDisguise();
    }

    public boolean isMobsIgnoreDisguise() {
        return mobsIgnoreDisguise;
    }

    public void setMobsIgnoreDisguise(boolean mobsIgnoreDisguise) {
        this.mobsIgnoreDisguise = mobsIgnoreDisguise;
    }
}