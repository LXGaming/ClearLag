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

package io.github.lxgaming.clearlag.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.lxgaming.clearlag.data.ClearData;
import io.github.lxgaming.clearlag.manager.ClearManager;
import io.github.lxgaming.clearlag.util.Toolbox;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ResultCommand extends AbstractCommand {
    
    public ResultCommand() {
        addAlias("result");
        addAlias("results");
        setPermission("clearlag.command.result");
        setUsage("<Type>");
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        if (arguments.isEmpty()) {
            if (commandSource instanceof Player) {
                List<Text> texts = Lists.newArrayList();
                ClearManager.getAllClearData().forEach(clearData -> {
                    Text.Builder textBuilder = Text.builder();
                    textBuilder.append(Text.of(clearData.getName()));
                    textBuilder.onClick(TextActions.executeCallback(callback -> resultCallback(clearData, callback)));
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
        
        resultCallback(clearData, commandSource);
        return CommandResult.success();
    }
    
    private void resultCallback(ClearData clearData, CommandSource commandSource) {
        if (clearData.getRemoved().isEmpty()) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "No results for ", clearData.getName()));
            return;
        }
        
        Map<CatalogType, Integer> results = Maps.newHashMap();
        for (CatalogType catalogType : clearData.getRemoved()) {
            results.put(catalogType, results.getOrDefault(catalogType, 0) + 1);
        }
        
        List<Text> texts = Lists.newArrayList();
        results.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach((entry) -> {
            Text.Builder textBuilder = Text.builder();
            textBuilder.append(Text.of(TextColors.WHITE, TextStyles.BOLD, "x", entry.getValue()));
            textBuilder.append(Text.of(TextColors.BLUE, " ", entry.getKey().getId()));
            texts.add(textBuilder.build());
        });
        
        if (commandSource instanceof Player) {
            PaginationList.Builder paginationBuilder = PaginationList.builder();
            paginationBuilder.title(Text.of(TextColors.WHITE, "Results: ", clearData.getName()));
            paginationBuilder.padding(Text.of(TextColors.DARK_GRAY, "="));
            paginationBuilder.footer(Text.of(TextColors.BLUE, "Last remove: ", TextColors.WHITE, Toolbox.getTimeString(System.currentTimeMillis() - clearData.getLastRemoveTime())));
            paginationBuilder.contents(texts);
            paginationBuilder.build().sendTo(commandSource);
        } else {
            texts.forEach(commandSource::sendMessage);
        }
    }
}