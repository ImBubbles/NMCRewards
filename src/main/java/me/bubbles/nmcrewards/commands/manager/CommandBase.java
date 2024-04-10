package me.bubbles.nmcrewards.commands.manager;

import me.bubbles.nmcrewards.NMCRewards;
import me.bubbles.nmcrewards.util.UtilSender;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class CommandBase implements CommandExecutor, TabCompleter {

    public NMCRewards plugin;
    public String no_perms;
    private String command;
    private String permission;
    private ArrayList<Argument> arguments = new ArrayList<>();
    public UtilSender utilSender;
    public final int index=0;

    public CommandBase(NMCRewards plugin, String command) {
        this.command=command;
        this.plugin=plugin;
    }

    public void run(CommandSender sender, String[] args) {
        this.utilSender=new UtilSender(plugin, sender);
        if(getArguments().isEmpty()) {
            return;
        }
        if(!(args.length==0)) { // IF PLAYER SENDS ARGUMENTS
            for(Argument argument : getArguments()) { // ARGUMENTS
                if(argument.getArg().equalsIgnoreCase(args[index])) {
                    argument.run(sender, args,false);
                    return;
                }
            }
        }
        utilSender.sendMessage(getArgsMessage());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        this.utilSender=new UtilSender(plugin,sender);
        run(sender,args); // this is so I can use super statements for run
        return true;
    }

    public String getCommand() {
        return command;
    }

    public boolean permissionCheck() {
        if(permission==null)
            return true;
        if(!utilSender.isPlayer()) {
            return true;
        }
        Player player = utilSender.getPlayer();
        if(!player.hasPermission(permission)) {
            utilSender.sendMessage(no_perms);
            return false;
        }else{
            return true;
        }
    }

    public void setPermission(String permission) {
        String node = plugin.getName().toLowerCase() + "." + permission;
        this.permission=node;
        this.no_perms=plugin.getConfigManager().getConfig("config.yml").getFileConfiguration().getString("placeholders.no_perms").replace("%node%",node);
    }

    public String getArgsMessage() {

        StringBuilder stringBuilder = new StringBuilder();
        String topLine = "%prefix%" + "%primary%" + " Commands:";
        stringBuilder.append(topLine);

        for(Argument arg : arguments) {
            if(arg.getPermission()!=null) {
                if(!utilSender.hasPermission(arg.getPermission())) {
                    continue;
                }
            }
            String command = "\n" + "%primary%" + "/" + getCommand() + "%secondary%" + " " + arg.getDisplay() + "\n";
            stringBuilder.append(command);
        }

        return stringBuilder.toString();

    }

    public void addArguments(Argument... args) {
        arguments.addAll(Arrays.asList(args));
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase(getCommand())) {
            tabIndex=0;
            int depth = strings.length-1;
            return getArgsAtDepth(getArguments(),depth,strings);
        }
        return null;
    }

    private int tabIndex;

    public List<String> getArgsAtDepth(List<Argument> arguments, int depth, String[] args) {
        List<String> result = new ArrayList<>();
        List<Argument> oneDeep = new ArrayList<>();
        for(Argument argument : arguments) {
            if(depth==0) {
                result.add(argument.getArg());
            }
            if(tabIndex==depth) {
                if(args[depth].equalsIgnoreCase(argument.getArg())) {
                    for(Argument depthArg : argument.getArguments()) {
                        result.add(depthArg.getArg());
                    }
                }
            }
            oneDeep.add(argument);
        }
        if(tabIndex==depth) {
            return result;
        }
        if(tabIndex>depth) {
            Bukkit.getLogger().log(Level.SEVERE,"bad work");
            return result;
        }
        tabIndex++;
        return getArgsAtDepth(oneDeep,depth,args);
    }

}