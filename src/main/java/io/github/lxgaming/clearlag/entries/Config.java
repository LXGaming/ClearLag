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

package io.github.lxgaming.clearlag.entries;

import java.util.List;

public class Config {
	
	// General Configurations
	private boolean debug;
	
	// Item Configurations
	private long itemInterval;
	private int itemLimit;
	private List<String> itemList;
	private String itemListType;
	private boolean itemWarnings;
	private List<Integer> itemWarningIntervals;
	private String itemClearedMessage;
	private String itemLimitedMessage;
	private String itemWarningMessage;
	
	// Mob Configurations
	private long mobInterval;
	private int mobLimit;
	private int mobLimitPerChunk;
	private List<String> mobList;
	private String mobListType;
	private boolean mobWarnings;
	private List<Integer> mobWarningIntervals;
	private String mobClearedMessage;
	private String mobLimitedMessage;
	private String mobWarningMessage;
	
	public boolean isDebug() {
		return debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	public long getItemInterval() {
		return itemInterval;
	}
	
	public void setItemInterval(long itemInterval) {
		this.itemInterval = itemInterval;
	}
	
	public int getItemLimit() {
		return itemLimit;
	}
	
	public void setItemLimit(int itemLimit) {
		this.itemLimit = itemLimit;
	}
	
	public List<String> getItemList() {
		return itemList;
	}
	
	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}
	
	public String getItemListType() {
		return itemListType;
	}
	
	public void setItemListType(String itemListType) {
		this.itemListType = itemListType;
	}
	
	public boolean isItemWarnings() {
		return itemWarnings;
	}
	
	public void setItemWarnings(boolean itemWarnings) {
		this.itemWarnings = itemWarnings;
	}
	
	public List<Integer> getItemWarningIntervals() {
		return itemWarningIntervals;
	}
	
	public void setItemWarningIntervals(List<Integer> itemWarningIntervals) {
		this.itemWarningIntervals = itemWarningIntervals;
	}
	
	public String getItemClearedMessage() {
		return itemClearedMessage;
	}
	
	public void setItemClearedMessage(String itemClearedMessage) {
		this.itemClearedMessage = itemClearedMessage;
	}
	
	public String getItemLimitedMessage() {
		return itemLimitedMessage;
	}
	
	public void setItemLimitedMessage(String itemLimitedMessage) {
		this.itemLimitedMessage = itemLimitedMessage;
	}
	
	public String getItemWarningMessage() {
		return itemWarningMessage;
	}
	
	public void setItemWarningMessage(String itemWarningMessage) {
		this.itemWarningMessage = itemWarningMessage;
	}
	
	public long getMobInterval() {
		return mobInterval;
	}
	
	public void setMobInterval(long mobInterval) {
		this.mobInterval = mobInterval;
	}
	
	public int getMobLimit() {
		return mobLimit;
	}
	
	public void setMobLimit(int mobLimit) {
		this.mobLimit = mobLimit;
	}
	
	public int getMobLimitPerChunk() {
		return mobLimitPerChunk;
	}
	
	public void setMobLimitPerChunk(int mobLimitPerChunk) {
		this.mobLimitPerChunk = mobLimitPerChunk;
	}
	
	public List<String> getMobList() {
		return mobList;
	}
	
	public void setMobList(List<String> mobList) {
		this.mobList = mobList;
	}
	
	public String getMobListType() {
		return mobListType;
	}
	
	public void setMobListType(String mobListType) {
		this.mobListType = mobListType;
	}
	
	public boolean isMobWarnings() {
		return mobWarnings;
	}
	
	public void setMobWarnings(boolean mobWarnings) {
		this.mobWarnings = mobWarnings;
	}
	
	public List<Integer> getMobWarningIntervals() {
		return mobWarningIntervals;
	}
	
	public void setMobWarningIntervals(List<Integer> mobWarningIntervals) {
		this.mobWarningIntervals = mobWarningIntervals;
	}
	
	public String getMobClearedMessage() {
		return mobClearedMessage;
	}
	
	public void setMobClearedMessage(String mobClearedMessage) {
		this.mobClearedMessage = mobClearedMessage;
	}
	
	public String getMobLimitedMessage() {
		return mobLimitedMessage;
	}
	
	public void setMobLimitedMessage(String mobLimitedMessage) {
		this.mobLimitedMessage = mobLimitedMessage;
	}
	
	public String getMobWarningMessage() {
		return mobWarningMessage;
	}
	
	public void setMobWarningMessage(String mobWarningMessage) {
		this.mobWarningMessage = mobWarningMessage;
	}
}