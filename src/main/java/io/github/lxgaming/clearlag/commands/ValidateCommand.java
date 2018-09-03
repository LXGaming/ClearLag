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
import io.github.lxgaming.clearlag.util.Toolbox;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class ValidateCommand extends AbstractCommand {
    
    public ValidateCommand() {
        addAlias("validate");
        setPermission("clearlag.command.validate");
        setUsage("<Type>");
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        if (arguments.isEmpty()) {
            if (commandSource instanceof Player) {
                List<Text> texts = Toolbox.newArrayList();
                ClearManager.getAllClearData().forEach(clearData -> {
                    Text.Builder textBuilder = Text.builder();
                    textBuilder.append(Text.of(clearData.getName()));
                    textBuilder.onClick(TextActions.executeCallback(callback -> validateCallback(clearData, callback)));
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
        
        ClearData clearData = ClearManager.getClearData(arguments.remove(0)).orElse(null);
        if (clearData == null) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "ClearData not present"));
            return CommandResult.empty();
        }
        
        validateCallback(clearData, commandSource);
        return CommandResult.success();
    }
    
    private void validateCallback(ClearData clearData, CommandSource commandSource) {
        TypeCategory typeCategory = ClearLag.getInstance().getConfig().map(clearData.getConfigFunction()).orElse(null);
        if (typeCategory == null) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Config unavailable"));
            return;
        }
        
        List<Text> texts = Toolbox.newArrayList();
        for (String key : typeCategory.getTypes().keySet()) {
            CatalogData catalogData = CatalogData.of(key);
            if (!catalogData.isValid() || !Sponge.getRegistry().getType(clearData.getCatalogTypeClass(), catalogData.getUniqueId()).isPresent()) {
                texts.add(Text.of(TextColors.RED, key));
            }
        }
        
        if (texts.isEmpty()) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.GREEN, "All ", clearData.getName(), " validated"));
            return;
        }
        
        if (commandSource instanceof Player) {
            PaginationList.Builder paginationBuilder = PaginationList.builder();
            paginationBuilder.title(Text.of(TextColors.WHITE, "Validate: ", clearData.getName()));
            paginationBuilder.padding(Text.of(TextColors.DARK_GRAY, "="));
            paginationBuilder.contents(texts);
            paginationBuilder.build().sendTo(commandSource);
        } else {
            texts.forEach(commandSource::sendMessage);
        }
    }
}