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

package io.github.lxgaming.clearlag.managers;

import com.google.common.collect.ImmutableSet;
import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.configuration.Config;
import io.github.lxgaming.clearlag.configuration.categories.TypeCategory;
import io.github.lxgaming.clearlag.data.CatalogData;
import io.github.lxgaming.clearlag.data.ClearData;
import io.github.lxgaming.clearlag.util.Reference;
import io.github.lxgaming.clearlag.util.Toolbox;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.SpongeImplHooks;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public final class ClearManager {
    
    private static final ClearData ENTITY_CLEAR_DATA = ClearData.of(Reference.PLUGIN_ID + ":entity", "Entities", CatalogTypes.ENTITY_TYPE, Config::getEntityCategory);
    private static final ClearData ITEM_CLEAR_DATA = ClearData.of(Reference.PLUGIN_ID + ":item", "Items", CatalogTypes.ITEM_TYPE, Config::getItemCategory);
    private static final Collection<CatalogType> EXCLUDED_CATALOG_TYPES = ImmutableSet.of(EntityTypes.ITEM, EntityTypes.PLAYER);
    
    public static void updatePre(ClearData clearData) {
        if (!SpongeImplHooks.isMainThread()) {
            return;
        }
        
        if (clearData.getLastRemoveTick() > SpongeImpl.getServer().getTickCounter()) {
            clearData.setLastRemoveTick(0);
            ClearLag.getInstance().getLogger().warn("Tick synchronization error");
        }
        
        if (clearData.getRemoving().get()) {
            clearData.setLastRemoveTick(SpongeImpl.getServer().getTickCounter() + 1);
            clearData.setLastRemoveTime(System.currentTimeMillis());
        }
    }
    
    public static void updatePost(ClearData clearData) {
        if (!SpongeImplHooks.isMainThread()) {
            return;
        }
        
        if (clearData.getLastRemoveTick() == SpongeImpl.getServer().getTickCounter()) {
            if (!clearData.getRemoving().compareAndSet(true, false)) {
                clearData.setLastRemoveTick(0);
                ClearLag.getInstance().getLogger().warn("Clearing synchronization error");
            }
        }
    }
    
    public static void processTypeCategory(ClearData clearData) {
        TypeCategory typeCategory = ClearLag.getInstance().getConfig().map(clearData.getConfigFunction()).orElse(null);
        if (typeCategory == null) {
            return;
        }
        
        for (CatalogType catalogType : Sponge.getRegistry().getAllOf(clearData.getCatalogTypeClass())) {
            if (getExcludedCatalogTypes().contains(catalogType)) {
                typeCategory.getTypes().remove(catalogType.getId());
                continue;
            }
            
            if (typeCategory.isAutoPopulate()) {
                typeCategory.getTypes().putIfAbsent(catalogType.getId(), typeCategory.getDefaultValue());
            }
        }
        
        clearData.setInitializeTime(0);
        clearData.setInterval(typeCategory.getInterval());
        clearData.setOrder(Order.PRE);
        clearData.setWarningInterval(0);
    }
    
    public static Tristate getTristate(ClearData clearData, CatalogType catalogType, ValueContainer valueContainer) {
        TypeCategory typeCategory = ClearLag.getInstance().getConfig().map(clearData.getConfigFunction()).orElse(null);
        if (typeCategory == null || getExcludedCatalogTypes().contains(catalogType)) {
            return Tristate.UNDEFINED;
        }
        
        if (typeCategory.isIgnoreNamed() && valueContainer.get(Keys.DISPLAY_NAME).isPresent()) {
            ClearLag.getInstance().debugMessage("Ignoring named - {}", catalogType.getId());
            return Tristate.UNDEFINED;
        }
        
        CatalogData catalogData = CatalogData.of(catalogType.getId());
        if (!catalogData.isValid()) {
            return Tristate.UNDEFINED;
        }
        
        return getTristate(typeCategory, catalogData);
    }
    
    private static Tristate getTristate(TypeCategory typeCategory, CatalogData catalogData) {
        Map<String, Tristate> results = Toolbox.newHashMap();
        for (Map.Entry<String, Tristate> entry : typeCategory.getTypes().entrySet()) {
            if (entry.getValue() == Tristate.UNDEFINED) {
                continue;
            }
            
            if (StringUtils.equals(entry.getKey(), catalogData.getMod() + ":" + catalogData.getId() + ":" + catalogData.getVariant())) {
                results.put(entry.getKey(), entry.getValue());
                continue;
            }
            
            if (StringUtils.equals(entry.getKey(), catalogData.getMod() + ":" + catalogData.getId())) {
                results.put(entry.getKey(), entry.getValue());
                continue;
            }
            
            if (StringUtils.equals(entry.getKey(), catalogData.getMod())) {
                results.put(entry.getKey(), entry.getValue());
            }
        }
        
        if (results.isEmpty()) {
            return typeCategory.getDefaultValue();
        }
        
        return results.getOrDefault(catalogData.getMod() + ":" + catalogData.getId() + ":" + catalogData.getVariant(),
                results.getOrDefault(catalogData.getMod() + ":" + catalogData.getId(),
                        results.getOrDefault(catalogData.getMod(),
                                typeCategory.getDefaultValue()
                        )
                )
        );
    }
    
    public static Optional<ClearData> getClearData(String id) {
        for (ClearData clearData : getAllClearData()) {
            if (StringUtils.equals(clearData.getId(), id)) {
                return Optional.of(clearData);
            }
        }
        
        return Optional.empty();
    }
    
    public static Collection<ClearData> getAllClearData() {
        return ImmutableSet.of(getEntityClearData(), getItemClearData());
    }
    
    public static ClearData getEntityClearData() {
        return ENTITY_CLEAR_DATA;
    }
    
    public static ClearData getItemClearData() {
        return ITEM_CLEAR_DATA;
    }
    
    public static Collection<CatalogType> getExcludedCatalogTypes() {
        return EXCLUDED_CATALOG_TYPES;
    }
}