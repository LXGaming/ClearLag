/*
 * Copyright 2019 Alex Thomson
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

package io.github.lxgaming.clearlag.mixin.core.world;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.bridge.entity.EntityBridge;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.SpongeImpl;

@Mixin(value = World.class, priority = 137)
public abstract class WorldMixin {
    
    @Inject(method = "updateEntityWithOptionalForce", at = @At(value = "HEAD"), cancellable = true)
    private void onUpdateEntityWithOptionalForce(Entity entity, boolean forceUpdate, CallbackInfo callbackInfo) {
        EntityBridge entityBridge = (EntityBridge) entity;
        if (entityBridge.bridge$getLastTick() == SpongeImpl.getServer().getTickCounter()) {
            ClearLag.getInstance().debug("{} already ticked", entityBridge.getType().getId());
            return;
        }
        
        entityBridge.bridge$setLastTick(SpongeImpl.getServer().getTickCounter());
        entityBridge.bridge$removeEntity();
        
        // Don't perform update logic on a dead entity
        if (entityBridge.isRemoved()) {
            callbackInfo.cancel();
        }
    }
}