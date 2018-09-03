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

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.services.AbstractService;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public final class ServiceManager {
    
    public static void schedule(AbstractService abstractService) {
        try {
            if (!abstractService.prepareService()) {
                throw new IllegalStateException("Service preparation failed");
            }
            
            schedule(abstractService, abstractService.isAsynchronous(), abstractService.getDelay(), abstractService.getInterval()).ifPresent(abstractService::setTask);
        } catch (RuntimeException ex) {
            ClearLag.getInstance().getLogger().error("Encountered an error processing {}::schedule", "ServiceManager", ex);
        }
    }
    
    public static Optional<Task> schedule(Runnable runnable, boolean asynchronous, long delay, long interval) {
        try {
            Task.Builder taskBuilder = Task.builder();
            taskBuilder.execute(runnable);
            taskBuilder.delay(Math.max(delay, 0L), TimeUnit.MILLISECONDS);
            taskBuilder.interval(Math.max(interval, 0L), TimeUnit.MILLISECONDS);
            if (asynchronous) {
                taskBuilder.async();
            }
            
            return Optional.of(taskBuilder.submit(ClearLag.getInstance().getPluginContainer()));
        } catch (RuntimeException ex) {
            ClearLag.getInstance().getLogger().error("Encountered an error processing {}::schedule", "ServiceManager", ex);
            return Optional.empty();
        }
    }
}