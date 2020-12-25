package me.libraryaddict.disguise.utilities;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.mojang.authlib.GameProfile;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.utilities.mineskin.MineSkinResponse;
import me.libraryaddict.disguise.utilities.reflection.LibsProfileLookup;
import me.libraryaddict.disguise.utilities.reflection.ReflectionManager;
import me.libraryaddict.disguise.utilities.translations.LibsMsg;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by libraryaddict on 1/01/2020.
 */
public class SkinUtils {
    public interface SkinCallback {
        void onError(LibsMsg msg, Object... args);

        void onInfo(LibsMsg msg, Object... args);

        void onSuccess(WrappedGameProfile profile);
    }

    public enum ModelType {
        SLIM,
        NORMAL
    }

    public static void handleFile(File file, ModelType modelType, SkinCallback callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    MineSkinResponse response = DisguiseUtilities.getMineSkinAPI()
                            .generateFromFile(callback, file, modelType);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (response == null) {
                                return;
                            } else if (response.getGameProfile() == null) {
                                callback.onError(LibsMsg.SKIN_API_FAIL);
                                return;
                            }

                            handleProfile(response.getGameProfile(), modelType, callback);
                        }
                    }.runTask(LibsDisguises.getInstance());
                }
                catch (IllegalArgumentException e) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            callback.onError(LibsMsg.SKIN_API_BAD_FILE);
                        }
                    }.runTask(LibsDisguises.getInstance());
                }
            }
        }.runTaskAsynchronously(LibsDisguises.getInstance());
    }

    public static void handleUrl(String url, ModelType modelType, SkinCallback callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                MineSkinResponse response = DisguiseUtilities.getMineSkinAPI()
                        .generateFromUrl(callback, url, modelType);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (response == null) {
                            return;
                        } else if (response.getGameProfile() == null) {
                            callback.onError(LibsMsg.SKIN_API_FAIL);
                        }

                        handleProfile(response.getGameProfile(), modelType, callback);
                    }
                }.runTask(LibsDisguises.getInstance());
            }
        }.runTaskAsynchronously(LibsDisguises.getInstance());
    }

    public static void handleName(String playerName, ModelType modelType, SkinCallback callback) {
        WrappedGameProfile gameProfile = DisguiseUtilities.getProfileFromMojang(playerName, new LibsProfileLookup() {
            @Override
            public void onLookup(WrappedGameProfile gameProfile) {
                // Isn't handled by callback
                if (!Pattern.matches("([A-Za-z0-9_]){1,16}", playerName)) {
                    return;
                }

                if (gameProfile == null || gameProfile.getProperties().isEmpty()) {
                    callback.onError(LibsMsg.CANNOT_FIND_PLAYER_NAME, playerName);
                    return;
                }

                handleProfile(gameProfile, modelType, callback);
            }
        });

        // Is handled in callback
        if (gameProfile == null) {
            return;
        }

        if (gameProfile.getProperties().isEmpty()) {
            callback.onError(LibsMsg.CANNOT_FIND_PLAYER_NAME, playerName);
            return;
        }

        handleProfile(gameProfile, modelType, callback);
    }

    public static void handleProfile(GameProfile profile, ModelType modelType, SkinCallback callback) {
        handleProfile(WrappedGameProfile.fromHandle(profile), modelType, callback);
    }

    public static void handleProfile(WrappedGameProfile profile, ModelType modelType, SkinCallback callback) {
        callback.onSuccess(profile);
    }

    public static void handleUUID(UUID uuid, ModelType modelType, SkinCallback callback) {
        new BukkitRunnable() {
            @Override
            public void run() {
                WrappedGameProfile profile = ReflectionManager
                        .getSkullBlob(new WrappedGameProfile(uuid, "AutoGenerated"));

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (profile == null || profile.getProperties().isEmpty()) {
                            callback.onError(LibsMsg.CANNOT_FIND_PLAYER_UUID, uuid.toString());
                            return;
                        }

                        handleProfile(profile, modelType, callback);
                    }
                }.runTask(LibsDisguises.getInstance());
            }
        }.runTaskAsynchronously(LibsDisguises.getInstance());
    }

    public static boolean isUsable() {
        return getUsableStatus() == null;
    }

    public static String getUsableStatus() {
        if (DisguiseUtilities.getMineSkinAPI().isInUse()) {
            return LibsMsg.SKIN_API_IN_USE.get();
        }

        if (DisguiseUtilities.getMineSkinAPI().nextRequestIn() > 0) {
            return LibsMsg.SKIN_API_TIMER.get(DisguiseUtilities.getMineSkinAPI().nextRequestIn());
        }

        return null;
    }

    public static void grabSkin(String param, SkinCallback callback) {
        ModelType modelType = param.toLowerCase(Locale.ENGLISH).endsWith(":slim") ? ModelType.SLIM : ModelType.NORMAL;

        if (modelType == ModelType.SLIM) {
            param = param.substring(0, param.length() - ":slim".length());
        }

        if (param.matches("https?:\\/\\/.+")) {
            // Its an url
            callback.onInfo(LibsMsg.SKIN_API_USING_URL);

            handleUrl(param, modelType, callback);
        } else {
            // Check if it contains legal file characters
            if (!param.matches("[a-zA-Z0-9 -_]+(\\.png)?")) {
                callback.onError(LibsMsg.SKIN_API_INVALID_NAME);
                return;
            }

            File file = new File(LibsDisguises.getInstance().getDataFolder(),
                    "/Skins/" + param + (param.toLowerCase(Locale.ENGLISH).endsWith(".png") ? "" : ".png"));

            if (!file.exists()) {
                file = null;

                if (param.toLowerCase(Locale.ENGLISH).endsWith(".png")) {
                    callback.onError(LibsMsg.SKIN_API_BAD_FILE_NAME);
                    return;
                }
            }

            if (file != null) {
                callback.onInfo(LibsMsg.SKIN_API_USING_FILE);
                handleFile(file, modelType, callback);
                // We're using a file!
            } else {
                // We're using a player name or UUID!
                if (param.contains("-")) {
                    try {
                        UUID uuid = UUID.fromString(param);

                        callback.onInfo(LibsMsg.SKIN_API_USING_UUID);
                        handleUUID(uuid, modelType, callback);
                        return;
                    }
                    catch (Exception ignored) {
                    }
                }

                WrappedGameProfile profile = DisguiseUtilities.getGameProfile(param);

                if (profile != null) {
                    callback.onInfo(LibsMsg.SKIN_API_USING_EXISTING_NAME);
                    callback.onSuccess(profile);
                    return;
                }

                callback.onInfo(LibsMsg.SKIN_API_USING_NAME);

                handleName(param, modelType, callback);
            }
        }
    }
}
