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

package io.github.lxgaming.clearlag.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

public class SpongeHelper {
	
	public static void broadcastMessage(String message, int count) {
		if (StringUtils.isBlank(message)) {
			return;
		}
		
		Sponge.getServer().getBroadcastChannel().send(Text.of(getTextPrefix(), convertColor(message.replace("[COUNT]", "" + count))));
	}
	
	public static Text getTextPrefix() {
		Text.Builder textBuilder = Text.builder();
		textBuilder.onHover(TextActions.showText(getPluginInformation()));
		textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, "[", Reference.PLUGIN_NAME, "]"));
		return Text.of(textBuilder.build(), TextStyles.RESET, " ");
	}
	
	public static Text getPluginInformation() {
		Text.Builder textBuilder = Text.builder();
		textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, Reference.PLUGIN_NAME, Text.NEW_LINE));
		textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Version: ", TextColors.WHITE, Reference.VERSION, Text.NEW_LINE));
		textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Authors: ", TextColors.WHITE, Reference.AUTHORS, Text.NEW_LINE));
		textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Website: ", TextColors.BLUE, getURLTextAction(Reference.WEBSITE), Reference.WEBSITE, Text.NEW_LINE));
		textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Source: ", TextColors.BLUE, getURLTextAction(Reference.SOURCE), Reference.SOURCE));
		return textBuilder.build();
	}
	
	public static TextAction<?> getURLTextAction(String url) {
		try {
			return TextActions.openUrl(new URL(url));
		} catch (MalformedURLException ex) {
			return TextActions.suggestCommand(url);
		}
	}
	
	public static Text convertColor(String string) {
		return TextSerializers.formattingCode('&').deserialize(string);
	}
	
	public static boolean isCatalogTypePresent(Class<? extends CatalogType> catalogType, String id) {
		if (catalogType == null || StringUtils.isBlank(id)) {
			return false;
		}
		
		if (Sponge.getRegistry().getType(catalogType, id).isPresent()) {
			return true;
		}
		
		return false;
	}
}