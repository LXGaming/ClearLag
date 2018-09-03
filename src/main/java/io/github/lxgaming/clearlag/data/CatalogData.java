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

import io.github.lxgaming.clearlag.util.Toolbox;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public final class CatalogData {
    
    private final String mod;
    private final String id;
    private final Integer variant;
    
    private CatalogData(String mod, String id, Integer variant) {
        this.mod = mod;
        this.id = id;
        this.variant = variant;
    }
    
    public static CatalogData of(String string) {
        String prefix = StringUtils.substringBeforeLast(string, ":");
        String suffix = StringUtils.substringAfterLast(string, ":");
        if (StringUtils.isNotBlank(prefix) && StringUtils.isNumeric(suffix)) {
            return of(prefix, Toolbox.parseInteger(suffix).orElse(-1));
        }
        
        return of(prefix, suffix);
    }
    
    public static CatalogData of(String string, int variant) {
        String mod = StringUtils.substringBefore(string, ":");
        String id = StringUtils.substringAfter(string, ":");
        return of(mod, id, variant);
    }
    
    public static CatalogData of(String mod, String id) {
        return of(mod, id, null);
    }
    
    public static CatalogData of(String mod, String id, Integer variant) {
        return new CatalogData(mod, id, variant);
    }
    
    public boolean isValid() {
        return StringUtils.isNotBlank(getMod()) && StringUtils.isNotBlank(getId()) && (getVariant() == null || getVariant() >= 0);
    }
    
    public String getUniqueId() {
        return getMod() + ":" + getId();
    }
    
    public String getMod() {
        return mod;
    }
    
    public String getId() {
        return id;
    }
    
    public Integer getVariant() {
        return variant;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getMod(), getId(), getVariant());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        CatalogData catalogData = (CatalogData) obj;
        return StringUtils.equals(getMod(), catalogData.getMod())
                && StringUtils.equals(getId(), catalogData.getId())
                && Objects.equals(getVariant(), catalogData.getVariant());
    }
    
    @Override
    public String toString() {
        if (getVariant() == null) {
            return getMod() + ":" + getId();
        }
        
        return getMod() + ":" + getId() + ":" + getVariant();
    }
}