/*
 * Copyright 2018 Alex Thomson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lxgaming.clearlag.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.command.AbstractCommand;
import io.github.lxgaming.clearlag.command.ClearLagCommand;
import io.github.lxgaming.clearlag.util.Toolbox;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class CommandManager {
    
    public static final Set<AbstractCommand> COMMANDS = Sets.newLinkedHashSet();
    private static final Set<Class<? extends AbstractCommand>> COMMAND_CLASSES = Sets.newHashSet();
    
    public static void prepare() {
        registerCommand(ClearLagCommand.class);
        
        for (AbstractCommand command : COMMANDS) {
            String[] aliases = command.getAliases().toArray(new String[0]);
            Sponge.getCommandManager().register(ClearLag.getInstance().getPluginContainer(), command, aliases);
        }
    }
    
    public static CommandResult execute(AbstractCommand parentCommand, CommandSource commandSource, String message) {
        List<String> arguments = getArguments(message.split(" "));
        AbstractCommand command = getCommand(parentCommand, arguments).orElse(null);
        if (command == null) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Unknown command"));
            return CommandResult.empty();
        }
        
        if (!command.testPermission(commandSource)) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "You do not have permission to execute this command"));
            return CommandResult.empty();
        }
        
        ClearLag.getInstance().getLogger().debug("Processing {} for {}", command.getPrimaryAlias().orElse("Unknown"), commandSource.getName());
        
        try {
            return command.execute(commandSource, arguments);
        } catch (Exception ex) {
            ClearLag.getInstance().getLogger().error("Encountered an error while executing {}", command.getClass().getSimpleName(), ex);
            commandSource.sendMessage(Text.of(TextColors.RED, "An error has occurred. Details are available in console."));
            return CommandResult.empty();
        }
    }
    
    public static boolean registerAlias(AbstractCommand command, String alias) {
        if (Toolbox.containsIgnoreCase(command.getAliases(), alias)) {
            ClearLag.getInstance().getLogger().warn("{} is already registered for {}", alias, command.getClass().getSimpleName());
            return false;
        }
        
        command.getAliases().add(alias);
        ClearLag.getInstance().getLogger().debug("{} registered for {}", alias, command.getClass().getSimpleName());
        return true;
    }
    
    public static boolean registerCommand(Class<? extends AbstractCommand> commandClass) {
        if (registerCommand(COMMANDS, commandClass)) {
            ClearLag.getInstance().getLogger().debug("{} registered", commandClass.getSimpleName());
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean registerCommand(AbstractCommand parentCommand, Class<? extends AbstractCommand> commandClass) {
        if (parentCommand.getClass() == commandClass) {
            ClearLag.getInstance().getLogger().warn("{} attempted to register itself", parentCommand.getClass().getSimpleName());
            return false;
        }
        
        if (registerCommand(parentCommand.getChildren(), commandClass)) {
            ClearLag.getInstance().getLogger().debug("{} registered for {}", commandClass.getSimpleName(), parentCommand.getClass().getSimpleName());
            return true;
        } else {
            return false;
        }
    }
    
    private static boolean registerCommand(Set<AbstractCommand> commands, Class<? extends AbstractCommand> commandClass) {
        if (COMMAND_CLASSES.contains(commandClass)) {
            ClearLag.getInstance().getLogger().warn("{} is already registered", commandClass.getSimpleName());
            return false;
        }
        
        COMMAND_CLASSES.add(commandClass);
        AbstractCommand command = Toolbox.newInstance(commandClass).orElse(null);
        if (command == null) {
            ClearLag.getInstance().getLogger().error("{} failed to initialize", commandClass.getSimpleName());
            return false;
        }
        
        return commands.add(command);
    }
    
    public static Optional<AbstractCommand> getCommand(Class<? extends AbstractCommand> commandClass) {
        return getCommand(null, commandClass);
    }
    
    public static Optional<AbstractCommand> getCommand(AbstractCommand parentCommand, Class<? extends AbstractCommand> commandClass) {
        Set<AbstractCommand> commands = Sets.newLinkedHashSet();
        if (parentCommand != null) {
            commands.addAll(parentCommand.getChildren());
        } else {
            commands.addAll(COMMANDS);
        }
        
        for (AbstractCommand command : commands) {
            if (command.getClass() == commandClass) {
                return Optional.of(command);
            }
            
            Optional<AbstractCommand> childCommand = getCommand(command, commandClass);
            if (childCommand.isPresent()) {
                return childCommand;
            }
        }
        
        return Optional.empty();
    }
    
    public static Optional<AbstractCommand> getCommand(List<String> arguments) {
        return getCommand(null, arguments);
    }
    
    private static Optional<AbstractCommand> getCommand(AbstractCommand parentCommand, List<String> arguments) {
        if (arguments.isEmpty()) {
            return Optional.ofNullable(parentCommand);
        }
        
        Set<AbstractCommand> commands = Sets.newLinkedHashSet();
        if (parentCommand != null) {
            commands.addAll(parentCommand.getChildren());
        } else {
            commands.addAll(COMMANDS);
        }
        
        for (AbstractCommand command : commands) {
            if (Toolbox.containsIgnoreCase(command.getAliases(), arguments.get(0))) {
                arguments.remove(0);
                return getCommand(command, arguments);
            }
        }
        
        return Optional.ofNullable(parentCommand);
    }
    
    private static List<String> getArguments(String[] strings) {
        List<String> arguments = Lists.newArrayList();
        for (String argument : strings) {
            if (StringUtils.isNotBlank(argument)) {
                arguments.add(argument);
            }
        }
        
        return arguments;
    }
}