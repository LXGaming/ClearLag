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

package io.github.lxgaming.clearlag.commands;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.util.SpongeHelper;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ReloadCommand extends Command {
    
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ClearLag.getInstance().getConfiguration().loadConfiguration();
        src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.GREEN, "Configuration reloaded."));
        return CommandResult.success();
    }
    
    @Override
    public String getName() {
        return "Reload";
    }
}