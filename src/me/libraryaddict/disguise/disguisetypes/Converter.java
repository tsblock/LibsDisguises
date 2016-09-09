package me.libraryaddict.disguise.disguisetypes;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.google.common.base.Optional;

public abstract class Converter<Y, G>
{
    public abstract G convertSend(Y obj);

    public abstract Y convertReceive(G obj);

    public static Converter<Boolean, Byte> BOOLEAN_TO_BYTE = new Converter<Boolean, Byte>()
    {
        @Override
        public Byte convertSend(Boolean obj)
        {
            return (byte) (obj ? 1 : 0);
        }

        @Override
        public Boolean convertReceive(Byte obj)
        {
            return obj == 1;
        }
    };

    public static Converter<Boolean, Integer> BOOLEAN_TO_INT = new Converter<Boolean, Integer>()
    {
        @Override
        public Integer convertSend(Boolean obj)
        {
            return (obj ? 1 : 0);
        }

        @Override
        public Boolean convertReceive(Integer obj)
        {
            return obj != 0;
        }
    };

    public static Converter<Integer, Byte> INT_TO_BYTE = new Converter<Integer, Byte>()
    {
        @Override
        public Byte convertSend(Integer obj)
        {
            return obj.byteValue();
        }

        @Override
        public Integer convertReceive(Byte obj)
        {
            return obj.intValue();
        }
    };
    public static Converter<Byte, Integer> BYTE_TO_INT = new Converter<Byte, Integer>()
    {
        @Override
        public Byte convertReceive(Integer obj)
        {
            return obj.byteValue();
        }

        @Override
        public Integer convertSend(Byte obj)
        {
            return obj.intValue();
        }
    };

    public static Converter<Optional<ItemStack>, ItemStack> OPT_ITEM_TO_ITEM = new Converter<Optional<ItemStack>, ItemStack>()
    {
        @Override
        public ItemStack convertSend(Optional<ItemStack> obj)
        {
            if (!obj.isPresent())
                return new ItemStack(Material.AIR);

            return obj.get();
        }

        @Override
        public Optional<ItemStack> convertReceive(ItemStack obj)
        {
            return Optional.fromNullable(obj);
        }
    };

    public static Converter<Optional<UUID>, String> OPT_UUID_TO_STRING = new Converter<Optional<UUID>, String>()
    {
        @Override
        public String convertSend(Optional<UUID> obj)
        {
            return "";
        }

        @Override
        public Optional<UUID> convertReceive(String obj)
        {
            return Optional.absent();
        }
    };

    public static Converter<Optional<WrappedBlockData>, ItemStack> BLOCKDATA_TO_ITEM = new Converter<Optional<WrappedBlockData>, ItemStack>()
    {
        @Override
        public ItemStack convertSend(Optional<WrappedBlockData> obj)
        {
            if (!obj.isPresent())
                return null;

            return new ItemStack(obj.get().getType(), 1, (short) obj.get().getData());
        }

        @Override
        public Optional<WrappedBlockData> convertReceive(ItemStack obj)
        {
            if (obj == null)
                return Optional.absent();

            return Optional.of(WrappedBlockData.createData(obj.getType(), obj.getDurability()));
        }
    };
}
