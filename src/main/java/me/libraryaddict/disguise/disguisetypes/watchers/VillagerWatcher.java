package me.libraryaddict.disguise.disguisetypes.watchers;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.MetaIndex;
import me.libraryaddict.disguise.disguisetypes.VillagerData;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;
import me.libraryaddict.disguise.utilities.parser.RandomDefaultValue;
import me.libraryaddict.disguise.utilities.reflection.NmsAddedIn;
import me.libraryaddict.disguise.utilities.reflection.NmsVersion;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

public class VillagerWatcher extends AbstractVillagerWatcher {

    public VillagerWatcher(Disguise disguise) {
        super(disguise);

        setProfession(Profession.values()[DisguiseUtilities.random.nextInt(Profession.values().length)]);
    }

    @NmsAddedIn(val = NmsVersion.v1_14)
    public VillagerData getVillagerData() {
        return getData(MetaIndex.VILLAGER_DATA);
    }

    @NmsAddedIn(val = NmsVersion.v1_14)
    public void setVillagerData(VillagerData villagerData) {
        setData(MetaIndex.VILLAGER_DATA, villagerData);
        sendData(MetaIndex.VILLAGER_DATA);
    }

    public Profession getProfession() {
        if (NmsVersion.v1_14.isSupported()) {
            return getVillagerData().getProfession();
        }

        return Profession.values()[getData(MetaIndex.VILLAGER_PROFESSION) + 1];
    }

    @RandomDefaultValue
    public void setProfession(Profession profession) {
        if (NmsVersion.v1_14.isSupported()) {
            setVillagerData(new VillagerData(getType(), profession, getLevel()));
        } else {
            setData(MetaIndex.VILLAGER_PROFESSION, profession.ordinal() - 1);
            sendData(MetaIndex.VILLAGER_PROFESSION);
        }
    }

    @Deprecated
    @NmsAddedIn(val = NmsVersion.v1_14)
    public Villager.Type getType() {
        return getVillagerData().getType();
    }

    @Deprecated
    @NmsAddedIn(val = NmsVersion.v1_14)
    public void setType(Villager.Type type) {
        setVillagerData(new VillagerData(type, getProfession(), getLevel()));
    }

    @NmsAddedIn(val = NmsVersion.v1_14)
    public Villager.Type getBiome() {
        return getType();
    }

    @NmsAddedIn(val = NmsVersion.v1_14)
    public void setBiome(Villager.Type type) {
        setType(type);
    }

    @NmsAddedIn(val = NmsVersion.v1_14)
    public int getLevel() {
        return getVillagerData().getLevel();
    }

    @NmsAddedIn(val = NmsVersion.v1_14)
    public void setLevel(int level) {
        setVillagerData(new VillagerData(getType(), getProfession(), getLevel()));
    }
}
