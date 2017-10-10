/*
 * Copyright 2017 Alex Thomson
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

package io.github.lxgaming.clearlag.commands.mob;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.commands.Command;
import io.github.lxgaming.clearlag.util.Reference;
import io.github.lxgaming.clearlag.util.SpongeHelper;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;

public class MobValidateCommand extends Command {
    
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (ClearLag.getInstance().getConfig().getMobList().isEmpty()) {
            src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.RED, "Mob list is empty!"));
            return CommandResult.success();
        }
        
        String command = "/" + Reference.PLUGIN_NAME + " Mob Remove [ID]";
        List<Text> texts = new ArrayList<Text>();
        for (String string : ClearLag.getInstance().getConfig().getMobList()) {
            if (SpongeHelper.isCatalogTypePresent(EntityType.class, string)) {
                continue;
            }
            
            Text.Builder textBuilder = Text.builder();
            textBuilder.onClick(TextActions.suggestCommand(command.replace("[ID]", string)));
            textBuilder.onHover(TextActions.showText(Text.of(command.replace("[ID]", string))));
            textBuilder.append(Text.of(TextColors.RED, string));
            texts.add(textBuilder.build());
        }
        
        if (!SpongeHelper.buildPagination(src, Text.of(TextColors.DARK_GREEN, "Failed to validate:"), texts)) {
            src.sendMessage(Text.of(TextColors.GREEN, "All mobs successfully validated."));
            return CommandResult.success();
        }
        
        return CommandResult.success();
    }
    
    @Override
    public String getName() {
        return "Validate";
    }
}