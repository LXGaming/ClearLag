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

package io.github.lxgaming.clearlag.managers;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.entries.EntityData;
import io.github.lxgaming.clearlag.entries.ListType;
import io.github.lxgaming.clearlag.util.SpongeHelper;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.Hostile;
import org.spongepowered.api.entity.living.monster.Boss;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManager {
    
    public void removeItems() {
        if (ClearLag.getInstance().getConfig() == null) {
            ClearLag.getInstance().getLogger().warn("Failed to remove items as config is null!");
            return;
        }
        
        ListType listType = ListType.getListType(ClearLag.getInstance().getConfig().getItemListType());
        if (listType == null || listType.equals(ListType.UNKNOWN)) {
            ClearLag.getInstance().getLogger().warn("{} is not a valid Item List Type!", listType);
            return;
        }
        
        List<Entity> entities = filterEntities(listType, ClearLag.getInstance().getConfig().getItemList(), getEntities(Item.class));
        if (entities != null && ClearLag.getInstance().getConfig().getItemLimit() <= entities.size()) {
            removeEntities(entities);
            SpongeHelper.broadcastMessage(ClearLag.getInstance().getConfig().getItemClearedMessage(), entities.size());
        } else {
            SpongeHelper.broadcastMessage(ClearLag.getInstance().getConfig().getItemLimitedMessage(), ClearLag.getInstance().getConfig().getItemLimit());
        }
    }
    
    public void removeMobs() {
        if (ClearLag.getInstance().getConfig() == null) {
            ClearLag.getInstance().getLogger().warn("Failed to remove mobs as config is null!");
            return;
        }
        
        ListType listType = ListType.getListType(ClearLag.getInstance().getConfig().getMobListType());
        if (listType == null || listType.equals(ListType.UNKNOWN)) {
            ClearLag.getInstance().getLogger().warn("{} is not a valid Mob List Type!", listType);
            return;
        }
        
        List<Entity> entities = filterEntities(listType, ClearLag.getInstance().getConfig().getMobList(), getEntities(Hostile.class));
        if (entities != null && ClearLag.getInstance().getConfig().getMobLimit() <= entities.size()) {
            removeEntities(entities);
            SpongeHelper.broadcastMessage(ClearLag.getInstance().getConfig().getMobClearedMessage(), entities.size());
        } else {
            SpongeHelper.broadcastMessage(ClearLag.getInstance().getConfig().getMobLimitedMessage(), ClearLag.getInstance().getConfig().getMobLimit());
        }
    }
    
    public List<Entity> filterEntities(ListType listType, List<String> list, List<Entity> entities) {
        if (listType == null || list == null || entities == null) {
            return null;
        }
        
        for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext(); ) {
            Entity entity = iterator.next();
            if (entity == null) {
                iterator.remove();
                continue;
            }
            
            EntityData entityData = new EntityData();
            entityData.populate(entity.getType().getId(), 0);
            if (Item.class.isAssignableFrom(entity.getClass())) {
                DataContainer dataContainer = ((Item) entity).item().get().toContainer();
                entityData.populate(dataContainer.getString(DataQuery.of("ItemType")).orElse(""), dataContainer.getInt(DataQuery.of("UnsafeDamage")).orElse(0));
            }
            
            if (!entityData.isValid()) {
                ClearLag.getInstance().getLogger().warn("Invalid EntityData!");
            }
            
            if (listType.equals(ListType.BLACKLIST) && entityData.isListed(list)) {
                ClearLag.getInstance().debugMessage("Entity {} will be removed,", entityData.toString());
                continue;
            }
            
            if (listType.equals(ListType.WHITELIST) && !entityData.isListed(list)) {
                ClearLag.getInstance().debugMessage("Entity {} will be removed,", entityData.toString());
                continue;
            }
            
            iterator.remove();
        }
        
        return entities;
    }
    
    public List<Entity> getEntities(Class<? extends Entity> entityClass) {
        List<Entity> entities = new ArrayList<Entity>();
        for (World world : Sponge.getServer().getWorlds()) {
            if (entityClass == null) {
                entities.addAll(world.getEntities().stream()
                                        .filter(entity -> !Player.class.isAssignableFrom(entity.getClass()))
                                        .collect(Collectors.toCollection(ArrayList::new)));
                
                continue;
            }
            
            entities.addAll(world.getEntities().stream()
                                    .filter(entity -> !Player.class.isAssignableFrom(entity.getClass()))
                                    .filter(entity -> !Boss.class.isAssignableFrom(entity.getClass()))
                                    .filter(entity -> entityClass.isAssignableFrom(entity.getClass()))
                                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        
        return entities;
    }
    
    public int removeEntities(List<Entity> entities) {
        int removedEntities = 0;
        if (entities == null) {
            return removedEntities;
        }
        
        for (Entity entity : entities) {
            if (entity == null) {
                continue;
            }
            
            entity.remove();
            removedEntities++;
        }
        
        return removedEntities;
    }
}