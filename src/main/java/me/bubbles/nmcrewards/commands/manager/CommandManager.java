package me.bubbles.nmcrewards.commands.manager;

import me.bubbles.nmcrewards.NMCRewards;
import me.bubbles.nmcrewards.commands.base.BaseCommand;

import java.util.ArrayList;
import java.util.HashSet;

public class CommandManager {
    private NMCRewards plugin;

    private HashSet<CommandBase> commands;

    public CommandManager(NMCRewards plugin) {
        this.plugin=plugin;
        this.commands=new HashSet<>();
        registerCommands();
    }

    public void registerCommands() {
        addCommand(
                new BaseCommand(plugin)
        );
    }

    public void addCommand(CommandBase... commands) {
        for(CommandBase command : commands) {
            try {
                plugin.getCommand(command.getCommand()).setExecutor(command);
                plugin.getCommand(command.getCommand()).setTabCompleter(command);
                this.commands.add(command);
                if(!command.getArguments().isEmpty()) {
                    registerArguments(command.getArguments());
                }
            } catch (NullPointerException e) {
                plugin.getLogger().warning("Command /"+command.getCommand()+", could not be registered. Most likely due to improper plugin.yml");
            }
        }
    }

    public void registerArguments(ArrayList<Argument> arguments) {
        for(Argument argument : arguments) {
            if(argument.getAlias()!=null) {
                try {
                    plugin.getCommand(argument.getAlias()).setExecutor(argument);
                } catch (NullPointerException e) {
                    plugin.getLogger().warning("Command /"+argument.getAlias()+", could not be registered. Most likely due to improper plugin.yml");
                }
            }
            if(!argument.getArguments().isEmpty()) {
                registerArguments(argument.getArguments());
            }
        }
    }

    public HashSet<CommandBase> getCommands() {
        return commands;
    }

}