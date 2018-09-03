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

package io.github.lxgaming.clearlag.commands;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.configuration.categories.TypeCategory;
import io.github.lxgaming.clearlag.data.CatalogData;
import io.github.lxgaming.clearlag.data.ClearData;
import io.github.lxgaming.clearlag.managers.ClearManager;
import io.github.lxgaming.clearlag.util.Reference;
import io.github.lxgaming.clearlag.util.Toolbox;
import org.apache.commons.lang3.BooleanUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Tristate;

import java.util.List;

public class AddCommand extends AbstractCommand {
    
    public AddCommand() {
        addAlias("add");
        setPermission("clearlag.command.add");
        setUsage("<Type> <Id> [False/True]");
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        if (arguments.isEmpty()) {
            if (commandSource instanceof Player) {
                List<Text> texts = Toolbox.newArrayList();
                ClearManager.getAllClearData().forEach(clearData -> {
                    Text.Builder textBuilder = Text.builder();
                    textBuilder.append(Text.of(clearData.getName()));
                    textBuilder.onClick(TextActions.suggestCommand("/" + Reference.PLUGIN_ID + " " + getPrimaryAlias().orElse("Unknown") + " " + clearData.getId()));
                    textBuilder.onHover(TextActions.showText(Text.of(clearData.getId())));
                    texts.add(textBuilder.build());
                });
                
                PaginationList.Builder paginationBuilder = PaginationList.builder();
                paginationBuilder.title(Text.of(TextColors.WHITE, "Types"));
                paginationBuilder.padding(Text.of(TextColors.DARK_GRAY, "="));
                paginationBuilder.contents(texts);
                paginationBuilder.build().sendTo(commandSource);
            } else {
                commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.WHITE, "Types:"));
                ClearManager.getAllClearData().forEach(clearData -> {
                    commandSource.sendMessage(Text.of("- ", TextColors.BLUE, clearData.getId()));
                });
            }
            
            return CommandResult.empty();
        }
        
        if (arguments.size() < 2) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Invalid arguments: ", getUsage()));
            return CommandResult.empty();
        }
        
        ClearData clearData = ClearManager.getClearData(arguments.remove(0)).orElse(null);
        if (clearData == null) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "ClearData not present"));
            return CommandResult.empty();
        }
        
        TypeCategory typeCategory = ClearLag.getInstance().getConfig().map(clearData.getConfigFunction()).orElse(null);
        if (typeCategory == null) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Config unavailable"));
            return CommandResult.empty();
        }
        
        CatalogData catalogData = CatalogData.of(arguments.remove(0));
        if (!catalogData.isValid()) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Failed to parse id"));
            return CommandResult.empty();
        }
        
        if (!Sponge.getRegistry().getType(clearData.getCatalogTypeClass(), catalogData.getUniqueId()).isPresent()) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Id does not exist in registry"));
            return CommandResult.empty();
        }
        
        Tristate tristate = typeCategory.getDefaultValue();
        if (!arguments.isEmpty()) {
            tristate = Tristate.fromBoolean(BooleanUtils.toBoolean(arguments.remove(0)));
        }
        
        typeCategory.getTypes().put(catalogData.toString(), tristate);
        commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.GREEN, "Added ", catalogData.toString(), " to ", clearData.getName()));
        return CommandResult.success();
    }
}