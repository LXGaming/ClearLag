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

package io.github.lxgaming.clearlag.configuration;

import java.io.IOException;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.entries.Config;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

public class Configuration {
	
	private final ConfigurationLoader<CommentedConfigurationNode> configurationLoader;
	private final ConfigurationOptions configurationOptions;
	private CommentedConfigurationNode configurationNode;
	private Config config;
	
	public Configuration() {
		configurationLoader = HoconConfigurationLoader.builder().setPath(ClearLag.getInstance().getPath()).build();
		configurationOptions = ConfigurationOptions.defaults().setShouldCopyDefaults(true);
	}
	
	public void loadConfiguration() {
		try {
			setConfigurationNode(getConfigurationLoader().load(getConfigurationOptions()));
			setConfig(new Config());
			
			//General configurations
			getConfig().setDebug(getConfigurationNode().getNode("general", "debug").getBoolean(false));
			
			//Item configurations
			getConfig().setItemInterval(getConfigurationNode().getNode("items", "interval").getLong(600000));
			getConfig().setItemLimit(getConfigurationNode().getNode("items", "limit").getInt(0));
			getConfig().setItemList(getConfigurationNode().getNode("items", "list").getList(TypeToken.of(String.class), Lists.newArrayList()));
			getConfig().setItemListType(getConfigurationNode().getNode("items", "listType").getString("WHITELIST"));
			getConfig().setItemWarningIntervals(getConfigurationNode().getNode("items", "warningIntervals").getList(TypeToken.of(Integer.class), Lists.newArrayList(60000, 30000, 15000, 5000)));
			getConfig().setItemClearedMessage(getConfigurationNode().getNode("items", "clearedMessage").getString("&f&l[COUNT] &8items have been cleared!"));
			getConfig().setItemLimitedMessage(getConfigurationNode().getNode("items", "limitedMessage").getString("&8Skipping item removal."));
			getConfig().setItemWarningMessage(getConfigurationNode().getNode("items", "warningMessage").getString("&8Items will be cleared in &f&l[COUNT] &8seconds."));
			
			//Mob configurations
			getConfig().setMobInterval(getConfigurationNode().getNode("mobs", "interval").getLong(300000));
			getConfig().setMobLimit(getConfigurationNode().getNode("mobs", "limit").getInt(500));
			getConfig().setMobLimitPerChunk(getConfigurationNode().getNode("mobs", "limitPerChunk").getInt(25));
			getConfig().setMobList(getConfigurationNode().getNode("mobs", "list").getList(TypeToken.of(String.class), Lists.newArrayList()));
			getConfig().setMobListType(getConfigurationNode().getNode("mobs", "listType").getString("WHITELIST"));
			getConfig().setMobWarningIntervals(getConfigurationNode().getNode("mobs", "warningIntervals").getList(TypeToken.of(Integer.class), Lists.newArrayList(60000, 30000, 15000, 5000)));
			getConfig().setMobClearedMessage(getConfigurationNode().getNode("mobs", "clearedMessage").getString("&f&l[COUNT] &8mobs have been cleared!"));
			getConfig().setMobLimitedMessage(getConfigurationNode().getNode("mobs", "limitedMessage").getString("&8Skipping mob removal."));
			getConfig().setMobWarningMessage(getConfigurationNode().getNode("mobs", "warningMessage").getString("&8Mobs will be cleared in &f&l[COUNT] &8seconds."));
			
			ClearLag.getInstance().getLogger().info("Successfully loaded configuration file.");
		} catch (IOException | RuntimeException | ObjectMappingException ex) {
			ClearLag.getInstance().getLogger().error("Encountered an error processing {}::loadConfiguration", getClass().getSimpleName(), ex);
			ex.printStackTrace();
		}
	}
	
	public void saveConfiguration() {
		try {
			//General configurations
			getConfigurationNode().getNode("general", "debug").setValue(getConfig().isDebug());
			
			//Item configurations
			getConfigurationNode().getNode("items", "interval").setValue(getConfig().getItemInterval());
			getConfigurationNode().getNode("items", "limit").setValue(getConfig().getItemLimit());
			getConfigurationNode().getNode("items", "list").setValue(getConfig().getItemList().stream().sorted().collect(Collectors.toList()));
			getConfigurationNode().getNode("items", "listType").setValue(getConfig().getItemListType());
			getConfigurationNode().getNode("items", "warningIntervals").setValue(getConfig().getItemWarningIntervals());
			getConfigurationNode().getNode("items", "clearedMessage").setValue(getConfig().getItemClearedMessage());
			getConfigurationNode().getNode("items", "warningMessage").setValue(getConfig().getItemWarningMessage());
			
			//Mob configurations
			getConfigurationNode().getNode("mobs", "interval").setValue(getConfig().getMobInterval());
			getConfigurationNode().getNode("mobs", "limit").setValue(getConfig().getMobLimit());
			getConfigurationNode().getNode("mobs", "limitPerChunk").setValue(getConfig().getMobLimitPerChunk());
			getConfigurationNode().getNode("mobs", "list").setValue(getConfig().getMobList().stream().sorted().collect(Collectors.toList()));
			getConfigurationNode().getNode("mobs", "listType").setValue(getConfig().getMobListType());
			getConfigurationNode().getNode("mobs", "warningIntervals").setValue(getConfig().getMobWarningIntervals());
			getConfigurationNode().getNode("mobs", "clearedMessage").setValue(getConfig().getMobClearedMessage());
			getConfigurationNode().getNode("mobs", "warningMessage").setValue(getConfig().getMobWarningMessage());
			
			getConfigurationLoader().save(getConfigurationNode());
			ClearLag.getInstance().getLogger().info("Successfully saved configuration file.");
		} catch (IOException | RuntimeException ex) {
			ClearLag.getInstance().getLogger().error("Encountered an error processing {}::saveConfiguration", getClass().getSimpleName(), ex);
			ex.printStackTrace();
		}
	}
	
	private ConfigurationLoader<CommentedConfigurationNode> getConfigurationLoader() {
		return configurationLoader;
	}
	
	private ConfigurationOptions getConfigurationOptions() {
		return configurationOptions;
	}
	
	private CommentedConfigurationNode getConfigurationNode() {
		return configurationNode;
	}
	
	private void setConfigurationNode(CommentedConfigurationNode configurationNode) {
		this.configurationNode = configurationNode;
	}
	
	public Config getConfig() {
		return config;
	}
	
	private void setConfig(Config config) {
		this.config = config;
	}
}