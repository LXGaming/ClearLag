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

package io.github.lxgaming.clearlag.util;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Optional;

public class Toolbox {
    
    public static Text getTextPrefix() {
        Text.Builder textBuilder = Text.builder();
        textBuilder.onHover(TextActions.showText(getPluginInformation()));
        textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, "[", Reference.NAME, "]"));
        return Text.of(textBuilder.build(), TextStyles.RESET, " ");
    }
    
    public static Text getPluginInformation() {
        Text.Builder textBuilder = Text.builder();
        textBuilder.append(Text.of(TextColors.BLUE, TextStyles.BOLD, Reference.NAME, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Version: ", TextColors.WHITE, Reference.VERSION, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Authors: ", TextColors.WHITE, Reference.AUTHORS, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Source: ", TextColors.BLUE, getURLTextAction(Reference.SOURCE), Reference.SOURCE, Text.NEW_LINE));
        textBuilder.append(Text.of("    ", TextColors.DARK_GRAY, "Website: ", TextColors.BLUE, getURLTextAction(Reference.WEBSITE), Reference.WEBSITE));
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
        return TextSerializers.FORMATTING_CODE.deserialize(string);
    }
    
    /**
     * Removes non-printable characters (excluding new line and carriage return) in the provided {@link java.lang.String String}.
     *
     * @param string The {@link java.lang.String String} to filter.
     * @return The filtered {@link java.lang.String String}.
     */
    public static String filter(String string) {
        return StringUtils.replaceAll(string, "[^\\x20-\\x7E\\x0A\\x0D]", "");
    }
    
    public static Optional<Integer> parseInteger(String string) {
        try {
            return Optional.of(Integer.parseInt(string));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
    
    public static String getTimeString(long time) {
        time = Math.abs(time);
        long second = time / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        long day = hour / 24;
        
        StringBuilder stringBuilder = new StringBuilder();
        appendUnit(stringBuilder, day, "day", "days");
        appendUnit(stringBuilder, hour % 24, "hour", "hours");
        appendUnit(stringBuilder, minute % 60, "minute", "minutes");
        appendUnit(stringBuilder, second % 60, "second", "seconds");
        
        if (stringBuilder.length() == 0) {
            stringBuilder.append("a moment");
        }
        
        return stringBuilder.toString();
    }
    
    public static void appendUnit(StringBuilder stringBuilder, long unit, String singular, String plural) {
        if (unit > 0) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            
            stringBuilder.append(unit).append(" ");
            if (unit == 1) {
                stringBuilder.append(singular);
            } else {
                stringBuilder.append(plural);
            }
        }
    }
    
    public static boolean containsIgnoreCase(Collection<String> list, String targetString) {
        if (list == null || list.isEmpty()) {
            return false;
        }
        
        for (String string : list) {
            if (StringUtils.equalsIgnoreCase(string, targetString)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static <T> Optional<T> newInstance(Class<? extends T> typeOfT) {
        try {
            return Optional.of(typeOfT.newInstance());
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}