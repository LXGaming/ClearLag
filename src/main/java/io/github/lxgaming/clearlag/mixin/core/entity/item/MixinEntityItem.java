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

package io.github.lxgaming.clearlag.mixin.core.entity.item;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.managers.ClearManager;
import io.github.lxgaming.clearlag.mixin.core.entity.MixinEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityItem.class, priority = 1337)
public abstract class MixinEntityItem extends MixinEntity implements Item {
    
    @Shadow
    public abstract ItemStack getItem();
    
    @Override
    public void clearlag$removeEntity() {
        if (!ClearManager.getItemClearData().getRemoving().get()) {
            return;
        }
        
        /*
        // EntityItem is never updated apparently :(
        // https://github.com/SpongePowered/SpongeCommon/issues/1899
        
        if (isInConstructPhase()) {
            ClearLag.getInstance().debugMessage("{} is in construction phase", getType().getId());
            return;
        }
        */
        
        if (isRemoved()) {
            ClearLag.getInstance().debugMessage("{} is already dead", getItemType().getId());
            return;
        }
        
        if (ClearManager.getTristate(ClearManager.getItemClearData(), getItemType(), get(Keys.REPRESENTED_ITEM).orElse(ItemStackSnapshot.NONE)).asBoolean()) {
            remove();
            ClearManager.getItemClearData().getRemoved().add(getItemType());
            ClearLag.getInstance().debugMessage("{} removed", getItemType().getId());
        }
    }
}