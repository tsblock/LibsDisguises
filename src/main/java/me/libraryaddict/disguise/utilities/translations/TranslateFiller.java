package me.libraryaddict.disguise.utilities.translations;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;
import me.libraryaddict.disguise.utilities.params.ParamInfo;
import me.libraryaddict.disguise.utilities.params.ParamInfoManager;
import me.libraryaddict.disguise.utilities.reflection.ClassGetter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by libraryaddict on 10/06/2017.
 */
public class TranslateFiller {
    public static void fillConfigs() {
        // Fill the configs

        for (ParamInfo info : ParamInfoManager.getParamInfos()) {
            TranslateType.DISGUISE_OPTIONS_PARAMETERS
                    .save(info.getRawName(), "A disguise option name, has description " + info.getDescription());

            if (!info.getRawName().equals(info.getRawDescriptiveName())) {
                TranslateType.DISGUISE_OPTIONS_PARAMETERS
                        .save(info.getRawDescriptiveName(), "A disguise option descriptive name");
            }

            TranslateType.DISGUISE_OPTIONS_PARAMETERS
                    .save(info.getRawDescription(), "Description for the disguise option " + info.getRawName());

            if (info.canTranslateValues()) {
                for (String e : info.getValues().keySet()) {
                    TranslateType.DISGUISE_OPTIONS_PARAMETERS
                            .save(e, "Used for the disguise option " + info.getRawName());
                }
            }

            if (info.getOtherValues() != null) {
                for (String e : info.getOtherValues()) {
                    TranslateType.DISGUISE_OPTIONS_PARAMETERS
                            .save(e, "Used for the disguise option " + info.getRawName());
                }
            }
        }

        for (DisguiseType type : DisguiseType.values()) {
            String[] split = type.name().split("_");

            for (int i = 0; i < split.length; i++) {
                split[i] = split[i].substring(0, 1) + split[i].substring(1).toLowerCase();
            }

            TranslateType.DISGUISES.save(StringUtils.join(split, " "), "Name for the " + type.name() + " disguise");

            if (type.getEntityType() == null) {
                continue;
            }

            for (Method method : ParamInfoManager.getDisguiseWatcherMethods(type.getWatcherClass())) {
                Class para = method.getParameterTypes()[0];
                String className = method.getDeclaringClass().getSimpleName().replace("Watcher", "");

                if (className.equals("Flag") || className.equals("Disguise"))
                    className = "Entity";
                else if (className.equals("Living"))
                    className = "Living Entity";
                else if (className.equals("AbstractHorse"))
                    className = "Horse";
                else if (className.equals("DroppedItem"))
                    className = "Item";
                else if (className.equals("IllagerWizard"))
                    className = "Illager";

                TranslateType.DISGUISE_OPTIONS.save(method.getName(),
                        "Found in the disguise options for " + className + " and uses " +
                                (para.isArray() ? "multiple" + " " : "a ") + para.getSimpleName().replace("[]", "s"));
            }
        }

        TranslateType.DISGUISE_OPTIONS.save("baby", "Used as a shortcut for setBaby when disguising an entity");
        TranslateType.DISGUISE_OPTIONS.save("adult", "Used as a shortcut for setBaby(false) when disguising an entity");

        ArrayList<Class> validClasses = new ArrayList<>();

        for (EntityType type : EntityType.values()) {
            Class c = type.getEntityClass();

            while (c != null && Entity.class.isAssignableFrom(c) && !validClasses.contains(c)) {
                validClasses.add(c);

                c = c.getSuperclass();
            }
        }

        for (Class c : validClasses) {
            if (c != Entity.class && Entity.class.isAssignableFrom(c) && c.getAnnotation(Deprecated.class) == null) {
                TranslateType.DISGUISES.save(c.getSimpleName(),
                        "Name for the " + c.getSimpleName() + " EntityType, " + "this is used in radius commands");
            }
        }

        TranslateType.DISGUISES.save("EntityType", "Used for the disgiuse radius command to list all entitytypes");
        TranslateType.DISGUISES
                .save("DisgiseType", "Used for the disgiuse modify radius command to list all " + "disguisetypes");

        for (LibsMsg msg : LibsMsg.values()) {
            TranslateType.MESSAGES.save(msg.getRaw(), "Reference: " + msg.name());
        }

        for (TranslateType type : TranslateType.values()) {
            type.saveTranslations();
        }
    }
}
