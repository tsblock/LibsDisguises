package me.libraryaddict.disguise.disguisetypes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers.Direction;
import com.comphenix.protocol.wrappers.Vector3F;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.google.common.base.Optional;

import me.libraryaddict.disguise.disguisetypes.watchers.AgeableWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.AreaEffectCloudWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ArmorStandWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ArrowWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.BatWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.BlazeWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.BoatWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.CreeperWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.DroppedItemWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.EnderCrystalWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.EnderDragonWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.EndermanWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.FallingBlockWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.FireworkWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.FishingHookWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.GhastWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.GuardianWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.HorseWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.InsentientWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.IronGolemWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ItemFrameWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.LivingWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.MinecartWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.OcelotWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PigWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PlayerWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.PolarBearWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.RabbitWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SheepWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ShulkerWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SkeletonWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SlimeWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SnowmanWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SpiderWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.SplashPotionWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.TNTWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.TameableWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.VillagerWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WitchWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WitherSkullWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WitherWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.WolfWatcher;
import me.libraryaddict.disguise.disguisetypes.watchers.ZombieWatcher;
import me.libraryaddict.disguise.utilities.ReflectionManager;

public class FlagType<Y>
{

    private static final int _v1_10 = 0;
    private static final int _v1_8 = 2;
    private static final int _v1_9 = 1;
    private static final int _v1_7 = 3;
    private static FlagType[] _values = new FlagType[0];

    public static FlagType<Boolean> AGEABLE_BABY = new FlagType<Boolean>(AgeableWatcher.class, 0, false)
            .set1_7(new Converter<Boolean, Integer>()
            {
                public Integer convertSend(Boolean isAdult)
                {
                    return isAdult ? 0 : -24000;
                }

                public Boolean convertReceive(Integer isAdult)
                {
                    return isAdult >= 0;
                }
            }).set1_8(12).set1_7(12);

    public static FlagType<Integer> AREA_EFFECT_COLOR = new FlagType<Integer>(AreaEffectCloudWatcher.class, 1,
            Color.BLACK.asRGB()).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Boolean> AREA_EFFECT_IGNORE_RADIUS = new FlagType<Boolean>(AreaEffectCloudWatcher.class, 2, false)
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Integer> AREA_EFFECT_PARTICLE = new FlagType<Integer>(AreaEffectCloudWatcher.class, 3, 0)
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Integer> AREA_EFFECT_PARTICLE_PARAM_1 = new FlagType<Integer>(AreaEffectCloudWatcher.class, 4, 0)
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Integer> AREA_EFFECT_PARTICLE_PARAM_2 = new FlagType<Integer>(AreaEffectCloudWatcher.class, 5, 0)
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Float> AREA_EFFECT_RADIUS = new FlagType<Float>(AreaEffectCloudWatcher.class, 0, 0F)
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Vector3F> ARMORSTAND_BODY = new FlagType<Vector3F>(ArmorStandWatcher.class, 2, new Vector3F(0, 0, 0))
            .set1_8(12);

    public static FlagType<Vector3F> ARMORSTAND_HEAD = new FlagType<Vector3F>(ArmorStandWatcher.class, 1, new Vector3F(0, 0, 0))
            .set1_8(11);

    public static FlagType<Vector3F> ARMORSTAND_LEFT_ARM = new FlagType<Vector3F>(ArmorStandWatcher.class, 3,
            new Vector3F(0, 0, 0)).set1_8(13);

    public static FlagType<Vector3F> ARMORSTAND_LEFT_LEG = new FlagType<Vector3F>(ArmorStandWatcher.class, 5,
            new Vector3F(0, 0, 0)).set1_8(15);

    public static FlagType<Byte> ARMORSTAND_META = new FlagType<Byte>(ArmorStandWatcher.class, 0, (byte) 0).set1_8(10);

    public static FlagType<Vector3F> ARMORSTAND_RIGHT_ARM = new FlagType<Vector3F>(ArmorStandWatcher.class, 4,
            new Vector3F(0, 0, 0)).set1_8(14);

    public static FlagType<Vector3F> ARMORSTAND_RIGHT_LEG = new FlagType<Vector3F>(ArmorStandWatcher.class, 6,
            new Vector3F(0, 0, 0)).set1_8(16);

    public static FlagType<Byte> ARROW_CRITICAL = new FlagType<Byte>(ArrowWatcher.class, 0, (byte) 0).set1_8(16).set1_7(16);

    public static FlagType<Byte> BAT_HANGING = new FlagType<Byte>(BatWatcher.class, 0, (byte) 1).set1_8(16).set1_7(16);

    public static FlagType<Byte> BLAZE_BLAZING = new FlagType<Byte>(BlazeWatcher.class, 0, (byte) 0).set1_8(16).set1_7(16);

    public static FlagType<Float> BOAT_DAMAGE = new FlagType<Float>(BoatWatcher.class, 2, 40F).set1_8(19).set1_7(19);

    public static FlagType<Integer> BOAT_DIRECTION = new FlagType<Integer>(BoatWatcher.class, 1, 0).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Integer> BOAT_LAST_HIT = new FlagType<Integer>(BoatWatcher.class, 0, 0).set1_8(17).set1_7(17);

    public static FlagType<Integer> BOAT_SHAKE = new FlagType<Integer>(BoatWatcher.class, 18, 0).setSupported(_v1_8, _v1_7);

    public static FlagType<Boolean> BOAT_LEFT_PADDLING = new FlagType<Boolean>(BoatWatcher.class, 5, false).setUnsupported(_v1_8,
            _v1_7);

    public static FlagType<Boolean> BOAT_RIGHT_PADDLING = new FlagType<Boolean>(BoatWatcher.class, 4, false).setUnsupported(_v1_8,
            _v1_7);

    public static FlagType<Integer> BOAT_TYPE = new FlagType<Integer>(BoatWatcher.class, 3, 0).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Boolean> CREEPER_IGNITED = new FlagType<Boolean>(CreeperWatcher.class, 2, false)
            .set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(18).set1_8(18);

    public static FlagType<Boolean> CREEPER_POWERED = new FlagType<Boolean>(CreeperWatcher.class, 1, false)
            .set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(17).set1_8(17);

    public static FlagType<Integer> CREEPER_STATE = new FlagType<Integer>(CreeperWatcher.class, 0, -1).set1_7(16).set1_8(16);

    public static FlagType<Optional<ItemStack>> DROPPED_ITEM = new FlagType<Optional<ItemStack>>(DroppedItemWatcher.class, 0,
            Optional.<ItemStack> of(new ItemStack(Material.STONE))).set1_8(Converter.OPT_ITEM_TO_ITEM)
                    .set1_7(Converter.OPT_ITEM_TO_ITEM).set1_8(10).set1_7(10);

    public static FlagType<Optional<BlockPosition>> ENDER_CRYSTAL_BEAM = new FlagType<Optional<BlockPosition>>(
            EnderCrystalWatcher.class, 0, Optional.<BlockPosition> absent()).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Boolean> ENDER_CRYSTAL_PLATE = new FlagType<Boolean>(EnderCrystalWatcher.class, 1, false)
            .set1_8(Converter.BOOLEAN_TO_INT).set1_7(Converter.BOOLEAN_TO_INT).set1_8(8).set1_7(8);

    public static FlagType<Integer> ENDERDRAGON_PHASE = new FlagType<Integer>(EnderDragonWatcher.class, 0, 0);

    public static FlagType<Boolean> ENDERMAN_AGRESSIVE = new FlagType<Boolean>(EndermanWatcher.class, 1, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(18).set1_7(18);

    public static FlagType<Optional<WrappedBlockData>> ENDERMAN_ITEM = new FlagType<Optional<WrappedBlockData>>(
            EndermanWatcher.class, 0, Optional.<WrappedBlockData> absent()).setUnsupported(_v1_7, _v1_8);

    public static FlagType<Short> ENDERMAN_OLD_ITEM_TYPE = new FlagType<Short>(EndermanWatcher.class, 16, (short) 0)
            .setSupported(_v1_8, _v1_7);

    public static FlagType<Byte> ENDERMAN_OLD_ITEM_DATA = new FlagType<Byte>(EndermanWatcher.class, 17, (byte) 0)
            .setSupported(_v1_8, _v1_7);

    public static FlagType<Integer> ENTITY_AIR_TICKS = new FlagType<Integer>(FlagWatcher.class, 1, 0);

    public static FlagType<String> ENTITY_CUSTOM_NAME = new FlagType<String>(FlagWatcher.class, 2, "");

    public static FlagType<Boolean> ENTITY_CUSTOM_NAME_VISIBLE = new FlagType<Boolean>(FlagWatcher.class, 3, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE);

    public static FlagType<Byte> ENTITY_META = new FlagType<Byte>(FlagWatcher.class, 0, (byte) 0);

    public static FlagType<Boolean> ENTITY_NO_GRAVITY = new FlagType<Boolean>(FlagWatcher.class, 5, false).setUnsupported(_v1_8,
            _v1_9);

    public static FlagType<Boolean> ENTITY_SILENT = new FlagType<Boolean>(FlagWatcher.class, 4, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).setUnsupported(_v1_7);

    public static FlagType<BlockPosition> FALLING_BLOCK_POSITION = new FlagType<BlockPosition>(FallingBlockWatcher.class, 0,
            BlockPosition.ORIGIN).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Optional<ItemStack>> FIREWORK_ITEM = new FlagType<Optional<ItemStack>>(FireworkWatcher.class, 0,
            Optional.<ItemStack> absent()).set1_8(Converter.OPT_ITEM_TO_ITEM).set1_7(Converter.OPT_ITEM_TO_ITEM).set1_8(8)
                    .set1_7(8);

    public static FlagType<Integer> FISHING_HOOK = new FlagType<Integer>(FishingHookWatcher.class, 0, 0);

    public static FlagType<Boolean> GHAST_AGRESSIVE = new FlagType<Boolean>(GhastWatcher.class, 0, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(16).set1_7(16);

    public static FlagType<Byte> GUARDIAN_FLAG = new FlagType<Byte>(GuardianWatcher.class, 0, (byte) 0)
            .set1_8(Converter.INT_TO_BYTE).set1_7(Converter.INT_TO_BYTE).set1_8(16).set1_7(16);

    public static FlagType<Integer> GUARDIAN_TARGET = new FlagType<Integer>(GuardianWatcher.class, 1, 0).set1_8(17).set1_7(17);

    public static FlagType<Integer> HORSE_ARMOR = new FlagType<Integer>(HorseWatcher.class, 4, 0).set1_8(22).set1_7(22);

    public static FlagType<Integer> HORSE_COLOR = new FlagType<Integer>(HorseWatcher.class, 2, 0).set1_8(20).set1_7(20);

    public static FlagType<Byte> HORSE_META = new FlagType<Byte>(HorseWatcher.class, 0, (byte) 0).set1_8(16).set1_7(16);

    public static FlagType<Optional<UUID>> HORSE_OWNER = new FlagType<Optional<UUID>>(HorseWatcher.class, 3,
            Optional.<UUID> absent()).set1_8(Converter.OPT_UUID_TO_STRING).set1_7(Converter.OPT_UUID_TO_STRING).set1_8(21)
                    .set1_7(21);

    public static FlagType<Integer> HORSE_VARIANT = new FlagType<Integer>(HorseWatcher.class, 1, 0).set1_8(19).set1_7(19);

    public static FlagType<Byte> INSENTIENT_META = new FlagType<Byte>(InsentientWatcher.class, 0, (byte) 0).set1_8(15)
            .setUnsupported(_v1_7);

    public static FlagType<Byte> IRON_GOLEM_PLAYER_CREATED = new FlagType<Byte>(IronGolemWatcher.class, 0, (byte) 0).set1_8(16)
            .set1_7(16);

    public static FlagType<Optional<ItemStack>> ITEMFRAME_ITEM = new FlagType<Optional<ItemStack>>(ItemFrameWatcher.class, 0,
            Optional.<ItemStack> absent()).set1_8(Converter.OPT_ITEM_TO_ITEM).set1_7(Converter.OPT_ITEM_TO_ITEM).set1_8(8)
                    .set1_7(8);

    public static FlagType<Integer> ITEMFRAME_ROTATION = new FlagType<Integer>(ItemFrameWatcher.class, 1, 0)
            .set1_8(Converter.INT_TO_BYTE).set1_7(Converter.INT_TO_BYTE).set1_8(9).set1_7(9);

    public static FlagType<Integer> LIVING_ARROWS = new FlagType<Integer>(LivingWatcher.class, 4, 0).set1_8(Converter.INT_TO_BYTE)
            .set1_7(Converter.INT_TO_BYTE).set1_8(9).set1_7(9);

    public static FlagType<Byte> LIVING_HAND = new FlagType<Byte>(LivingWatcher.class, 0, (byte) 0).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Float> LIVING_HEALTH = new FlagType<Float>(LivingWatcher.class, 1, 1F).set1_8(6).set1_7(6);

    public static FlagType<Boolean> LIVING_POTION_AMBIENT = new FlagType<Boolean>(LivingWatcher.class, 3, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(8).set1_7(8);

    public static FlagType<Integer> LIVING_POTIONS = new FlagType<Integer>(LivingWatcher.class, 2, 0).set1_8(7).set1_7(7);

    public static FlagType<Integer> MINECART_BLOCK = new FlagType<Integer>(MinecartWatcher.class, 3, 0).set1_8(20).set1_7(20);

    public static FlagType<Boolean> MINECART_BLOCK_VISIBLE = new FlagType<Boolean>(MinecartWatcher.class, 5, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(22).set1_7(22);

    public static FlagType<Integer> MINECART_BLOCK_Y = new FlagType<Integer>(MinecartWatcher.class, 4, 0).set1_8(21).set1_7(21);

    public static FlagType<Integer> MINECART_SHAKING_DIRECTION = new FlagType<Integer>(MinecartWatcher.class, 1, 1).set1_8(18)
            .set1_7(18);

    public static FlagType<Float> MINECART_SHAKING_MULITPLIER = new FlagType<Float>(MinecartWatcher.class, 2, 0F).set1_8(19)
            .set1_7(19);

    public static FlagType<Integer> MINECART_SHAKING_POWER = new FlagType<Integer>(MinecartWatcher.class, 0, 0).set1_8(17)
            .set1_7(17);

    public static FlagType<Integer> OCELOT_TYPE = new FlagType<Integer>(OcelotWatcher.class, 0, 0).set1_8(Converter.INT_TO_BYTE)
            .set1_7(Converter.INT_TO_BYTE).set1_8(18).set1_7(18);

    public static FlagType<Boolean> PIG_SADDLED = new FlagType<Boolean>(PigWatcher.class, 0, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(16).set1_7(16);

    public static FlagType<Float> PLAYER_ABSORPTION = new FlagType<Float>(PlayerWatcher.class, 0, 0F).set1_8(17).set1_7(17);

    public static FlagType<Byte> PLAYER_HAND = new FlagType<Byte>(PlayerWatcher.class, 3, (byte) 0).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Integer> PLAYER_SCORE = new FlagType<Integer>(PlayerWatcher.class, 1, 0).set1_8(18).set1_7(18);

    public static FlagType<Byte> PLAYER_SKIN = new FlagType<Byte>(PlayerWatcher.class, 2, (byte) 127).set1_8(10).set1_7(10);

    public static FlagType<Byte> PLAYER_SOMETHING = new FlagType<Byte>(PlayerWatcher.class, 16, (byte) 0).setSupported(_v1_7,
            _v1_8);

    public static FlagType<Boolean> POLAR_BEAR_STANDING = new FlagType<Boolean>(PolarBearWatcher.class, 0, false);

    public static FlagType<Integer> RABBIT_TYPE = new FlagType<Integer>(RabbitWatcher.class, 0, 0).set1_8(Converter.INT_TO_BYTE)
            .set1_7(Converter.INT_TO_BYTE).set1_8(18).set1_7(18);

    public static FlagType<Byte> SHEEP_WOOL = new FlagType<Byte>(SheepWatcher.class, 0, (byte) 0).set1_8(16).set1_7(16);

    public static FlagType<Optional<BlockPosition>> SHULKER_ATTACHED = new FlagType<Optional<BlockPosition>>(ShulkerWatcher.class,
            1, Optional.<BlockPosition> absent()).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Direction> SHULKER_FACING = new FlagType<Direction>(ShulkerWatcher.class, 0, Direction.DOWN)
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Byte> SHULKER_PEEKING = new FlagType<Byte>(ShulkerWatcher.class, 2, (byte) 0).setUnsupported(_v1_8,
            _v1_7);

    public static FlagType<Boolean> SKELETON_SWING_ARMS = new FlagType<Boolean>(SkeletonWatcher.class, 1, false)
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Integer> SKELETON_TYPE = new FlagType<Integer>(SkeletonWatcher.class, 0, 0)
            .set1_8(Converter.INT_TO_BYTE).set1_7(Converter.INT_TO_BYTE).set1_8(13).set1_7(13);

    public static FlagType<Integer> SLIME_SIZE = new FlagType<Integer>(SlimeWatcher.class, 0, 0).set1_8(Converter.INT_TO_BYTE)
            .set1_7(Converter.INT_TO_BYTE).set1_8(16).set1_7(16);

    public static FlagType<Byte> SNOWMAN_HAT = new FlagType<Byte>(SnowmanWatcher.class, 0, (byte) 0).setUnsupported(_v1_8, _v1_7);

    public static FlagType<Byte> SPIDER_CLIMB = new FlagType<Byte>(SpiderWatcher.class, 0, (byte) 0).set1_8(16).set1_7(16);

    public static FlagType<Optional<ItemStack>> SPLASH_POTION_ITEM = new FlagType<Optional<ItemStack>>(SplashPotionWatcher.class,
            1, Optional.fromNullable(ReflectionManager.getItemWithMaterial("SPLASH_POTION"))).setUnsupported(_v1_8, _v1_7);
    // Yeah, the '1' isn't a bug. No idea why but MC thinks there's a '0' already.

    public static FlagType<Optional<ItemStack>> SPLASH_POTION_ITEM_BAD = new FlagType<Optional<ItemStack>>(
            SplashPotionWatcher.class, 0, Optional.fromNullable(ReflectionManager.getItemWithMaterial("SPLASH_POTION")))
                    .setUnsupported(_v1_8, _v1_7);
    // Yeah, the '1' isn't a bug. No idea why but MC thinks there's a '0' already.

    public static FlagType<Byte> TAMEABLE_META = new FlagType<Byte>(TameableWatcher.class, 0, (byte) 0).set1_8(16).set1_7(16);

    public static FlagType<Optional<UUID>> TAMEABLE_OWNER = new FlagType<Optional<UUID>>(TameableWatcher.class, 1,
            Optional.<UUID> absent()).set1_8(Converter.OPT_UUID_TO_STRING).set1_7(Converter.OPT_UUID_TO_STRING).set1_8(17)
                    .set1_7(17);

    public static FlagType<Integer> TIPPED_ARROW_COLOR = new FlagType<Integer>(ArrowWatcher.class, 1, Color.WHITE.asRGB())
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Integer> TNT_FUSE_TICKS = new FlagType<Integer>(TNTWatcher.class, 0, Integer.MAX_VALUE)
            .setUnsupported(_v1_8, _v1_7);

    public static FlagType<Integer> VILLAGER_PROFESSION = new FlagType<Integer>(VillagerWatcher.class, 0, 0).set1_8(16)
            .set1_7(16);

    public static FlagType<Boolean> WITCH_AGGRESSIVE = new FlagType<Boolean>(WitchWatcher.class, 0, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(21).set1_7(21);

    public static FlagType<Integer> WITHER_INVUL = new FlagType<Integer>(WitherWatcher.class, 3, 0).set1_8(20).set1_7(20);

    public static FlagType<Integer> WITHER_TARGET_1 = new FlagType<Integer>(WitherWatcher.class, 0, 0).set1_8(17).set1_7(17);

    public static FlagType<Integer> WITHER_TARGET_2 = new FlagType<Integer>(WitherWatcher.class, 1, 0).set1_8(18).set1_7(18);

    public static FlagType<Integer> WITHER_TARGET_3 = new FlagType<Integer>(WitherWatcher.class, 2, 0).set1_8(19).set1_7(19);

    public static FlagType<Boolean> WITHERSKULL_BLUE = new FlagType<Boolean>(WitherSkullWatcher.class, 0, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(10).set1_7(10);

    public static FlagType<Boolean> WOLF_BEGGING = new FlagType<Boolean>(WolfWatcher.class, 1, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(19).set1_7(19);

    public static FlagType<Integer> WOLF_COLLAR = new FlagType<Integer>(WolfWatcher.class, 2, 14).set1_8(Converter.INT_TO_BYTE)
            .set1_7(Converter.INT_TO_BYTE).set1_8(20).set1_7(20);

    public static FlagType<Float> WOLF_DAMAGE = new FlagType<Float>(WolfWatcher.class, 0, 0F).set1_8(18).set1_7(18);

    public static FlagType<Boolean> ZOMBIE_AGGRESSIVE = new FlagType<Boolean>(ZombieWatcher.class, 3, false).setUnsupported(_v1_8,
            _v1_7);

    public static FlagType<Boolean> ZOMBIE_BABY = new FlagType<Boolean>(ZombieWatcher.class, 0, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(12).set1_7(12);

    public static FlagType<Integer> ZOMBIE_PROFESSION = new FlagType<Integer>(ZombieWatcher.class, 1, 0)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(13).set1_7(13);

    public static FlagType<Boolean> ZOMBIE_SHAKING = new FlagType<Boolean>(ZombieWatcher.class, 2, false)
            .set1_8(Converter.BOOLEAN_TO_BYTE).set1_7(Converter.BOOLEAN_TO_BYTE).set1_8(14).set1_7(14);

    static
    {
        try
        {
            for (Field field : FlagType.class.getFields())
            {
                if (field.getDeclaringClass() != FlagType.class)
                    continue;

                FlagType flagType = (FlagType) field.get(null);

                if (flagType.isUnsupported())
                    continue;

                _values = Arrays.copyOf(_values, _values.length + 1);
                _values[_values.length - 1] = flagType;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        ArrayList<FlagType> flags = new ArrayList<FlagType>(Arrays.asList(values()));
        final HashMap<FlagType, Integer> map = new HashMap<FlagType, Integer>();

        for (FlagType flag : flags)
        {
            Class c1 = flag.getFlagWatcher();

            int score = 0;

            while (FlagWatcher.class.isAssignableFrom(c1))
            {
                c1 = c1.getSuperclass();
                score++;
            }

            map.put(flag, score);
        }

        Collections.sort(flags, new Comparator<FlagType>()
        {
            @Override
            public int compare(FlagType o1, FlagType o2)
            {
                Class c1 = o1.getFlagWatcher();
                Class c2 = o2.getFlagWatcher();

                boolean a1 = c1.isAssignableFrom(c2);
                boolean a2 = c2.isAssignableFrom(c1);

                if (a1 && a2)
                {
                    return Integer.compare(o1.getIndex(), o2.getIndex());
                }

                return Integer.compare(map.get(o1), map.get(o2));
            }
        });

        if (!ReflectionManager.is1_7() && !ReflectionManager.is1_8())
        {
            for (FlagType flagType : flags)
            {
                if (flagType.getFlagWatcher() == FlagWatcher.class)
                    continue;

                flagType._index += getNoIndexes(flagType.getFlagWatcher().getSuperclass());
            }
        }

        // Simple verification for the dev that he's setting up the FlagType's properly.
        // All flag types should be from 0 to <Max Number> with no empty numbers.
        // All flag types should never occur twice.

        HashMap<Class, Integer> maxValues = new HashMap<Class, Integer>();

        for (FlagType type : values())
        {
            if (maxValues.containsKey(type.getFlagWatcher()) && maxValues.get(type.getFlagWatcher()) > type.getIndex())
                continue;

            maxValues.put(type.getFlagWatcher(), type.getIndex());
        }

        for (Entry<Class, Integer> entry : maxValues.entrySet())
        {
            loop:

            for (int i = 0; i < entry.getValue(); i++)
            {
                FlagType found = null;

                for (FlagType type : values())
                {
                    if (type.getIndex() != i)
                        continue;

                    if (!type.getFlagWatcher().isAssignableFrom(entry.getKey()))
                        continue;

                    if (found != null)
                    {
                        System.err.println(entry.getKey().getSimpleName() + " has multiple FlagType's registered for the index "
                                + i + " (" + type.getFlagWatcher().getSimpleName() + ", " + found.getFlagWatcher().getSimpleName()
                                + ")");
                        continue loop;
                    }

                    found = type;
                }

                if (found != null || ReflectionManager.is1_7() || ReflectionManager.is1_8())
                    continue;

                System.err.println(entry.getKey().getSimpleName() + " has no FlagType registered for the index " + i);
            }
        }
    }

    public static FlagType getFlag(Class<? extends FlagWatcher> watcherClass, int flagNo)
    {
        for (FlagType type : values())
        {
            if (type.getIndex() != flagNo)
                continue;

            if (!type.getFlagWatcher().isAssignableFrom(watcherClass))
                continue;

            return type;
        }

        return null;
    }

    public static ArrayList<FlagType> getFlags(Class<? extends FlagWatcher> watcherClass)
    {
        ArrayList<FlagType> list = new ArrayList<FlagType>();

        for (FlagType type : values())
        {
            if (!type.getFlagWatcher().isAssignableFrom(watcherClass))
                continue;

            list.add(type);
        }

        return list;
    }

    private static int getNoIndexes(Class c)
    {
        int found = 0;

        for (FlagType type : values())
        {
            if (type.getFlagWatcher() != c)
                continue;

            found++;
        }

        if (c != FlagWatcher.class)
        {
            found += getNoIndexes(c.getSuperclass());
        }

        return found;
    }

    public static FlagType[] values()
    {
        return _values;
    }

    private Y _defaultValue;
    private int _index;
    private boolean[] _unsupported = new boolean[4];
    private Converter _converter;
    private Class<? extends FlagWatcher> _watcher;

    private FlagType(Class<? extends FlagWatcher> watcher, int index, Y defaultValue)
    {
        _index = index;
        _watcher = watcher;
        _defaultValue = defaultValue;
    }

    public Y getDefault()
    {
        return _defaultValue;
    }

    public Object convertToSend(Y value)
    {
        int version = getVersion();

        if (version == -1)
            return value;

        if (_converter == null)
            return value;

        return _converter.convertSend(value);
    }

    public Y convertToReceive(Object value)
    {
        int version = getVersion();

        if (version == -1)
            return (Y) value;

        if (_converter == null)
            return (Y) value;

        return (Y) _converter.convertReceive(value);
    }

    public Class<? extends FlagWatcher> getFlagWatcher()
    {
        return _watcher;
    }

    public int getIndex()
    {
        return _index;
    }

    private int getVersion()
    {
        if (ReflectionManager.is1_10())
            return _v1_10;
        else if (ReflectionManager.is1_9())
            return _v1_9;
        else if (ReflectionManager.is1_8())
            return _v1_8;
        else
            return -1;
    }

    public boolean isUnsupported()
    {
        int version = getVersion();

        if (version == -1)
            throw new IllegalArgumentException("This version of Minecraft is not supported!");

        return _unsupported[version];
    }

    private FlagType<Y> set1_7(int index)
    {
        if (!ReflectionManager.is1_7())
            return this;

        _index = index;

        return this;
    }

    private FlagType<Y> set1_7(Converter converter)
    {
        if (!ReflectionManager.is1_7())
            return this;
        
        _converter = converter;

        return this;
    }

    private FlagType<Y> set1_8(Converter converter)
    {
        if (!ReflectionManager.is1_8())
            return this;
        
        _converter = converter;

        return this;
    }

    private FlagType<Y> set1_8(int index)
    {
        if (!ReflectionManager.is1_8())
            return this;

        _index = index;

        return this;
    }

    private FlagType<Y> setUnsupported(int... versions)
    {
        for (int version : versions)
        {
            _unsupported[version] = true;
        }

        return this;
    }

    private FlagType<Y> setSupported(int... versions)
    {
        for (int i = 0; i < _unsupported.length; i++)
            _unsupported[i] = true;

        for (int version : versions)
        {
            _unsupported[version] = false;
        }

        return this;
    }
}
