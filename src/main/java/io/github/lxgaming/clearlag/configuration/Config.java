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

package io.github.lxgaming.clearlag.configuration;

import io.github.lxgaming.clearlag.configuration.category.GeneralCategory;
import io.github.lxgaming.clearlag.configuration.category.TypeCategory;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Config {
    
    @Setting(value = "general")
    private GeneralCategory generalCategory = new GeneralCategory();
    
    @Setting(value = "entities")
    private TypeCategory entityCategory = new TypeCategory();
    
    @Setting(value = "items")
    private TypeCategory itemCategory = new TypeCategory();
    
    public GeneralCategory getGeneralCategory() {
        return generalCategory;
    }
    
    public TypeCategory getEntityCategory() {
        return entityCategory;
    }
    
    public TypeCategory getItemCategory() {
        return itemCategory;
    }
}