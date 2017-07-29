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

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.commands.Command;
import io.github.lxgaming.clearlag.util.SpongeHelper;

public class MobAddCommand extends Command {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String mob = args.<String>getOne("mob").orElse("");
		if (StringUtils.isBlank(mob)) {
			src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.RED, "Mob Id not provided!"));
			return CommandResult.success();
		}
		
		if (ClearLag.getInstance().getConfig().getMobList().contains(mob)) {
			src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.AQUA, mob, TextColors.RED, " already exists!"));
			return CommandResult.success();
		}
		
		ClearLag.getInstance().getConfig().getMobList().add(mob);
		ClearLag.getInstance().getConfiguration().saveConfiguration();
		src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.GREEN, "Successfully added ", TextColors.AQUA, mob));
		return CommandResult.success();
	}
	
	@Override
	public String getName() {
		return "Add";
	}
	
	@Override
	public String getUsage() {
		return getName() + " [MobId]";
	}
	
	@Override
	public List<CommandElement> getArguments() {
		return Arrays.asList(GenericArguments.optional(GenericArguments.string(Text.of("mob"))));
	}
}