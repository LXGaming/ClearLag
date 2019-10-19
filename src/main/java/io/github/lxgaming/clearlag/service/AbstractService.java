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

package io.github.lxgaming.clearlag.service;

import io.github.lxgaming.clearlag.ClearLag;
import org.spongepowered.api.scheduler.Task;

import java.util.function.Consumer;

public abstract class AbstractService implements Consumer<Task> {
    
    @Override
    public void accept(Task task) {
        try {
            execute();
        } catch (Exception ex) {
            ClearLag.getInstance().getLogger().error("Encountered an error while executing {}", getClass().getSimpleName(), ex);
            task.cancel();
        }
    }
    
    public abstract void execute() throws Exception;
}