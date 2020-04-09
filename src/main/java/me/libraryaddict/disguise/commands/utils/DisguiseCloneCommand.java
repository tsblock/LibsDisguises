package me.libraryaddict.disguise.commands.utils;

import me.libraryaddict.disguise.DisguiseConfig;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.commands.DisguiseBaseCommand;
import me.libraryaddict.disguise.commands.interactions.DisguiseCloneInteraction;
import me.libraryaddict.disguise.utilities.DisguiseUtilities;
import me.libraryaddict.disguise.utilities.parser.DisguisePermissions;
import me.libraryaddict.disguise.utilities.translations.LibsMsg;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DisguiseCloneCommand extends DisguiseBaseCommand implements TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.getName().equals("CONSOLE")) {
            sender.sendMessage(LibsMsg.NO_CONSOLE.get());
            return true;
        }

        if (!sender.hasPermission("libsdisguises.disguise.disguiseclone")) {

            sender.sendMessage(LibsMsg.NO_PERM.get());
            return true;
        }

        boolean doEquipment = true;
        boolean doSneak = false;
        boolean doSprint = false;
        Player player = null;

        if (args.length > 0) {
            player = Bukkit.getPlayerExact(args[0]);
        }

        for (int i = player == null ? 0 : 1; i < args.length; i++) {
            String option = args[i];
            if (StringUtils.startsWithIgnoreCase(option, LibsMsg.DCLONE_EQUIP.get())) {
                doEquipment = false;
            } else if (option.equalsIgnoreCase(LibsMsg.DCLONE_SNEAKSPRINT.get())) {
                doSneak = true;
                doSprint = true;
            } else if (option.equalsIgnoreCase(LibsMsg.DCLONE_SNEAK.get())) {
                doSneak = true;
            } else if (option.equalsIgnoreCase(LibsMsg.DCLONE_SPRINT.get())) {
                doSprint = true;
            } else {
                sender.sendMessage(LibsMsg.INVALID_CLONE.get(option));
                return true;
            }
        }

        Boolean[] options = new Boolean[]{doEquipment, doSneak, doSprint};

        if (player != null) {
            DisguiseUtilities.createClonedDisguise((Player) sender, player, options);
        } else {
            LibsDisguises.getInstance().getListener()
                    .addInteraction(sender.getName(), new DisguiseCloneInteraction(options),
                            DisguiseConfig.getDisguiseCloneExpire());

            sender.sendMessage(LibsMsg.CLICK_TIMER.get(DisguiseConfig.getDisguiseCloneExpire()));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] origArgs) {
        ArrayList<String> tabs = new ArrayList<>();

        String[] args = getPreviousArgs(origArgs);

        if (args.length == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                // If command user cannot see player online, don't tab-complete name
                if (sender instanceof Player && !((Player) sender).canSee(player)) {
                    continue;
                }

                tabs.add(player.getName());
            }
        }

        tabs.add(LibsMsg.DCLONE_EQUIP.get());
        tabs.add(LibsMsg.DCLONE_SNEAKSPRINT.get());
        tabs.add(LibsMsg.DCLONE_SNEAK.get());
        tabs.add(LibsMsg.DCLONE_SPRINT.get());

        return filterTabs(tabs, origArgs);
    }

    /**
     * Send the player the information
     */
    @Override
    protected void sendCommandUsage(CommandSender sender, DisguisePermissions permissions) {
        sender.sendMessage(LibsMsg.CLONE_HELP1.get());
        sender.sendMessage(LibsMsg.CLONE_HELP2.get());
        sender.sendMessage(LibsMsg.CLONE_HELP3.get());
    }
}
