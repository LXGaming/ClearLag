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

package io.github.lxgaming.clearlag.listeners;

import java.util.Optional;

import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.ConstructEntityEvent;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.world.Chunk;

import io.github.lxgaming.clearlag.ClearLag;	

public class EntityListener {
	
	@Listener
	public void onConstructEntity(ConstructEntityEvent.Pre event, @Root Entity entity) {
		if (ClearLag.getInstance().getConfig() == null || ClearLag.getInstance().getConfig().getMobLimitPerChunk() <= 0 || !(entity instanceof Living)) {
			return;
		}
		
		Optional<Chunk> chunk = entity.getWorld().getChunk(entity.getLocation().getChunkPosition());
		if (chunk.isPresent() && chunk.get().getEntities().size() >= ClearLag.getInstance().getConfig().getMobLimitPerChunk()) {
			event.setCancelled(true);
			return;
		}
	}
}