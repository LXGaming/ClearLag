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

package io.github.lxgaming.clearlag.commands.item;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.commands.Command;
import io.github.lxgaming.clearlag.entries.EntityData;
import io.github.lxgaming.clearlag.util.SpongeHelper;

public class ItemAddCommand extends Command {
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		String item = args.<String>getOne("item").orElse("");
		boolean ignoreVariant = args.<Boolean>getOne("ignoreVariant").orElse(false);
		
		if ((StringUtils.isBlank(item) || item.equalsIgnoreCase("hand")) && src instanceof Player) {
			Player player = (Player) src;
			ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
			if (itemStack == null) {
				src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.RED, "You are not holding anything!"));
				return CommandResult.success();
			}
			
			EntityData entityData = new EntityData();
			entityData.populate(itemStack.toContainer());
			if (ignoreVariant) {
				item = String.join(":", entityData.getModId(), entityData.getId());
			} else {
				item = String.join(":", entityData.getModId(), entityData.getId(), "" + entityData.getVariant());
			}
		}
		
		if (StringUtils.isBlank(item)) {
			src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.RED, "Item Id not provided!"));
			return CommandResult.success();
		}
		
		if (ClearLag.getInstance().getConfig().getItemList().contains(item)) {
			src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.AQUA, item, TextColors.RED, " already exists!"));
			return CommandResult.success();
		}
		
		ClearLag.getInstance().getConfig().getItemList().add(item);
		ClearLag.getInstance().getConfiguration().saveConfiguration();
		src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.GREEN, "Successfully added ", TextColors.AQUA, item));
		return CommandResult.success();
	}
	
	@Override
	public String getName() {
		return "Add";
	}
	
	@Override
	public String getUsage() {
		return getName() + " [Item Id | Hand] [Ignore Variant]";
	}
	
	@Override
	public List<CommandElement> getArguments() {
		return Arrays.asList(
				GenericArguments.optional(GenericArguments.string(Text.of("item"))),
				GenericArguments.optional(GenericArguments.bool(Text.of("ignoreVariant"))));
	}
}