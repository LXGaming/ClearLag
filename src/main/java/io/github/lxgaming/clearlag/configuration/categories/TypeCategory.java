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

package io.github.lxgaming.clearlag.configuration.categories;

import io.github.lxgaming.clearlag.util.Toolbox;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.util.Tristate;

import java.util.Map;

@ConfigSerializable
public class TypeCategory {
    
    @Setting(value = "auto-populate", comment = "If enabled, newly discovered types will be added to this config with the default-value.")
    private boolean autoPopulate = false;
    
    @Setting(value = "default-value", comment = "Default value [FALSE, TRUE, UNDEFINED]")
    private Tristate defaultValue = Tristate.UNDEFINED;
    
    @Setting(value = "interval", comment = "Clear interval in seconds")
    private long interval = 600000L;
    
    @Setting(value = "ignore-named", comment = "Ignores types with custom names")
    private boolean ignoreNamed = true;
    
    @Setting(value = "messages", comment = "Messages")
    private MessageCategory messageCategory = new MessageCategory();
    
    @Setting(value = "types", comment = "Types Map" +
            "\nFALSE - Type will not be removed" +
            "\nTRUE - Type will be removed" +
            "\nUNDEFINED - Fallback to default-value")
    private Map<String, Tristate> types = Toolbox.newHashMap();
    
    public boolean isAutoPopulate() {
        return autoPopulate;
    }
    
    public Tristate getDefaultValue() {
        return defaultValue;
    }
    
    public long getInterval() {
        return interval;
    }
    
    public boolean isIgnoreNamed() {
        return ignoreNamed;
    }
    
    public MessageCategory getMessageCategory() {
        return messageCategory;
    }
    
    public Map<String, Tristate> getTypes() {
        return types;
    }
}