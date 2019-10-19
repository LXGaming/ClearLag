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

package io.github.lxgaming.clearlag.data;

import com.google.common.collect.Queues;
import io.github.lxgaming.clearlag.configuration.Config;
import io.github.lxgaming.clearlag.configuration.category.TypeCategory;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.event.Order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public final class ClearData {
    
    private final String id;
    private final String name;
    private final Class<? extends CatalogType> catalogTypeClass;
    private final Function<Config, TypeCategory> configFunction;
    private final BlockingQueue<CatalogType> removed = Queues.newLinkedBlockingQueue();
    private final AtomicBoolean removing = new AtomicBoolean(false);
    private long initializeTime;
    private long interval;
    private int lastRemoveTick;
    private long lastRemoveTime;
    private Order order;
    private long warningInterval;
    
    private ClearData(String id, String name, Class<? extends CatalogType> catalogTypeClass, Function<Config, TypeCategory> configFunction) {
        this.id = id;
        this.name = name;
        this.catalogTypeClass = catalogTypeClass;
        this.configFunction = configFunction;
    }
    
    public static ClearData of(String id, String name, Class<? extends CatalogType> catalogTypeClass, Function<Config, TypeCategory> configFunction) {
        return new ClearData(id, name, catalogTypeClass, configFunction);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Class<? extends CatalogType> getCatalogTypeClass() {
        return catalogTypeClass;
    }
    
    public Function<Config, TypeCategory> getConfigFunction() {
        return configFunction;
    }
    
    public BlockingQueue<CatalogType> getRemoved() {
        return removed;
    }
    
    public AtomicBoolean getRemoving() {
        return removing;
    }
    
    public long getInitializeTime() {
        return initializeTime;
    }
    
    public void setInitializeTime(long initializeTime) {
        this.initializeTime = initializeTime;
    }
    
    public long getInterval() {
        return interval;
    }
    
    public void setInterval(long interval) {
        this.interval = interval;
    }
    
    public int getLastRemoveTick() {
        return lastRemoveTick;
    }
    
    public void setLastRemoveTick(int lastRemoveTick) {
        this.lastRemoveTick = lastRemoveTick;
    }
    
    public long getLastRemoveTime() {
        return lastRemoveTime;
    }
    
    public void setLastRemoveTime(long lastRemoveTime) {
        this.lastRemoveTime = lastRemoveTime;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public long getWarningInterval() {
        return warningInterval;
    }
    
    public void setWarningInterval(long warningInterval) {
        this.warningInterval = warningInterval;
    }
}