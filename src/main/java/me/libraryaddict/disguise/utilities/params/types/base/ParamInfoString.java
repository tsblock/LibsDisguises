package me.libraryaddict.disguise.utilities.params.types.base;

import me.libraryaddict.disguise.utilities.params.ParamInfo;
import org.bukkit.ChatColor;

/**
 * Created by libraryaddict on 7/09/2018.
 */
public class ParamInfoString extends ParamInfo {
    public ParamInfoString(Class paramClass, String name, String description) {
        super(paramClass, name, description);

        setOtherValues("%user-name%", "%target-name%");
    }

    @Override
    protected Object fromString(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public String toString(Object object) {
        return ((String) object).replace(ChatColor.COLOR_CHAR + "", "&");
    }
}
