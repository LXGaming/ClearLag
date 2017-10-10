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

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.commands.Command;
import io.github.lxgaming.clearlag.entries.EntityData;
import io.github.lxgaming.clearlag.util.SpongeHelper;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ItemRemoveCommand extends Command {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String item = args.<String>getOne("item").orElse("");
        boolean ignoreVariant = args.<Boolean>getOne("ignoreVariant").orElse(false);

        if (item.equalsIgnoreCase("hand") && src instanceof Player) {
            Player player = (Player) src;
            ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(null);
            if (itemStack == null) {
                src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.RED, "You are not holding anything!"));
                return CommandResult.success();
            }

            EntityData entityData = new EntityData();
            DataContainer dataContainer = itemStack.toContainer();
            entityData.populate(dataContainer.getString(DataQuery.of("ItemType")).orElse("") + ":" + dataContainer.getInt(DataQuery.of("UnsafeDamage")).orElse(0));
            if (ignoreVariant) {
                item = String.join(":", entityData.getModId(), entityData.getEntityId());
            } else {
                item = String.join(":", entityData.getModId(), entityData.getEntityId(), "" + entityData.getVariant());
            }
        }

        if (StringUtils.isBlank(item)) {
            src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.RED, "Item Id is not present!"));
            return CommandResult.success();
        }

        if (!ClearLag.getInstance().getConfig().getItemList().contains(item)) {
            src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.AQUA, item, TextColors.RED, " doesn't exist!"));
            return CommandResult.success();
        }

        for (Iterator<String> iterator = ClearLag.getInstance().getConfig().getItemList().iterator(); iterator.hasNext(); ) {
            String string = iterator.next();
            if (StringUtils.isBlank(string) || string.equals(item)) {
                iterator.remove();
            }
        }

        ClearLag.getInstance().getConfiguration().saveConfiguration();
        src.sendMessage(Text.of(SpongeHelper.getTextPrefix(), TextColors.GREEN, "Successfully removed ", TextColors.AQUA, item));
        return CommandResult.success();
    }

    @Override
    public String getName() {
        return "Remove";
    }

    @Override
    public String getUsage() {
        return "<Item Id | Hand> [Ignore Variant]";
    }

    @Override
    public List<CommandElement> getArguments() {
        return Arrays.asList(
                GenericArguments.string(Text.of("item")),
                GenericArguments.optional(GenericArguments.bool(Text.of("ignoreVariant"))));
    }
}