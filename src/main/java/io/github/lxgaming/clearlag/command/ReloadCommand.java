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

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.util.Toolbox;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class ReloadCommand extends AbstractCommand {
    
    public ReloadCommand() {
        addAlias("reload");
        setPermission("clearlag.command.reload");
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        if (ClearLag.getInstance().reload()) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.GREEN, "Configuration reloaded"));
        } else {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "An error occurred. Please check the console."));
        }
        
        return CommandResult.success();
    }
}