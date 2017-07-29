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
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;

import com.google.gson.Gson;

public class EntityData {
	
	private String modId;
	private String id;
	private int variant;
	
	public void populate(DataContainer dataContainer) {
		if (dataContainer == null) {
			return;
		}
		
		populate(dataContainer.getString(DataQuery.of("ItemType")).orElse(""), dataContainer.getInt(DataQuery.of("UnsafeDamage")).orElse(0));
	}
	
	public void populate(String entityId, int variant) {
		if (entityId == null) {
			return;
		}
		
		String[] data = entityId.split(":", 2);
		if (data.length == 2) {
			setModId(data[0]);
			setId(data[1]);
			setVariant(variant);
		}
	}
	
	public boolean isListed(List<String> list) {
		if (list == null || list.isEmpty() || !isValid()) {
			return false;
		}
		
		if (list.contains(getModId())) {
			return true;
		}
		
		if (list.contains(String.join(":", getModId(), getId()))) {
			return true;
		}
		
		if (list.contains(String.join(":", getModId(), getId(), "" + getVariant()))) {
			return true;
		}
		
		return false;
	}
	
	public boolean isValid() {
		if (StringUtils.isNotBlank(getModId()) && StringUtils.isNotBlank(getId())) {
			return true;
		}
		
		return false;
	}
	
	public String getModId() {
		return modId;
	}
	
	public void setModId(String modId) {
		this.modId = modId;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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