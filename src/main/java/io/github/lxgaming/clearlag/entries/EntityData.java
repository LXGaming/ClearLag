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

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;

import io.github.lxgaming.clearlag.util.SpongeHelper;

public class EntityData {
	
	private boolean excluded;
	private String modId;
	private String entityId;
	private int variant;
	
	public boolean populate(String string) {
		if (StringUtils.isBlank(string)) {
			return false;
		}
		
		String data = StringUtils.substringBeforeLast(string, ":");
		String variant = StringUtils.substringAfterLast(string, ":");
		if (StringUtils.isNotBlank(data) && StringUtils.isNumeric(variant)) {
			return populate(data, SpongeHelper.parseInt(variant));
		}
		
		return populate(string, 0);
	}
	
	public boolean populate(String string, int variant) {
		if (StringUtils.isBlank(string) || variant < 0) {
			return false;
		}
		
		String modId = StringUtils.substringBefore(string, ":");
		if (StringUtils.isNotBlank(modId) && modId.startsWith("!")) {
			modId = modId.substring(1);
			setExcluded(true);
		}
		
		String entityId = StringUtils.substringAfter(string, ":");
		if (StringUtils.isNotBlank(modId) && StringUtils.isNotBlank(entityId)) {
			setModId(modId);
			setEntityId(entityId);
			setVariant(variant);
			return true;
		}
		
		return false;
	}
	
	public boolean isListed(List<String> list) {
		if (list == null || list.isEmpty() || !isValid()) {
			return false;
		}
		
		if (list.contains("!" + getModId() + ":" + getEntityId() + ":" + getVariant())) {
			return false;
		}
		
		if (list.contains("!" + getModId() + ":" + getEntityId())) {
			return false;
		}
		
		if (list.contains(getModId() + ":any")) {
			return true;
		}
		
		if (list.contains(getModId() + ":" + getEntityId())) {
			return true;
		}
		
		if (list.contains(getModId() + ":" + getEntityId() + ":" + getVariant())) {
			return true;
		}
		
		return false;
	}
	
	public boolean isUniversal() {
		if (isValid() && getEntityId().equalsIgnoreCase("any")) {
			return true;
		}
		
		return false;
	}
	
	public boolean isValid() {
		if (StringUtils.isNotBlank(getModId()) && StringUtils.isNotBlank(getEntityId()) && getVariant() >= 0) {
			return true;
		}
		
		return false;
	}
	
	public boolean isExcluded() {
		return excluded;
	}
	
	public void setExcluded(boolean excluded) {
		this.excluded = excluded;
	}
	
	public String getModId() {
		return modId;
	}
	
	public void setModId(String modId) {
		this.modId = modId;
	}
	
	public String getEntityId() {
		return entityId;
	}
	
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}
	
	public int getVariant() {
		return variant;
	}
	
	public void setVariant(int variant) {
		this.variant = variant;
	}
	
	@Override
	public String toString() {
		return new Gson().toJson(this, getClass());
	}
}