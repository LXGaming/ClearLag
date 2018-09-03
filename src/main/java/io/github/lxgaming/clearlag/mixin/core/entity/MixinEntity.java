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

package io.github.lxgaming.clearlag.mixin.core.entity;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.interfaces.entity.IMixinEntity_ClearLag;
import io.github.lxgaming.clearlag.managers.ClearManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImpl;

@Mixin(value = Entity.class, priority = 1337)
@Implements(@Interface(iface = IMixinEntity_ClearLag.class, prefix = "clearlag$"))
public abstract class MixinEntity implements IMixinEntity_ClearLag {
    
    private int lastTick;
    
    @Inject(method = "onUpdate", at = @At(value = "HEAD"), cancellable = true)
    private void onTickEntity(CallbackInfo callbackInfo) {
        if (getLastTick() == SpongeImpl.getServer().getTickCounter()) {
            // ClearLag.getInstance().debugMessage("{} already ticked", getType().getId());
            return;
        }
        
        setLastTick(SpongeImpl.getServer().getTickCounter());
        removeEntity();
        
        // Don't perform update logic on a dead entity
        if (isRemoved()) {
            callbackInfo.cancel();
        }
    }
    
    public void clearlag$removeEntity() {
        if (!ClearManager.getEntityClearData().getRemoving().get()) {
            return;
        }
        
        if (isInConstructPhase()) {
            ClearLag.getInstance().debugMessage("{} is in construction phase", getType().getId());
            return;
        }
        
        if (isRemoved()) {
            ClearLag.getInstance().debugMessage("{} is already dead", getType().getId());
            return;
        }
        
        if (ClearManager.getTristate(ClearManager.getEntityClearData(), getType(), this).asBoolean()) {
            remove();
            ClearManager.getEntityClearData().getRemoved().add(getType());
            ClearLag.getInstance().debugMessage("{} removed", getType().getId());
        }
    }
    
    public int clearlag$getLastTick() {
        return this.lastTick;
    }
    
    public void clearlag$setLastTick(int lastTick) {
        this.lastTick = lastTick;
    }
}