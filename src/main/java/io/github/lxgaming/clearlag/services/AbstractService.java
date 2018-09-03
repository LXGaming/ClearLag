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

package io.github.lxgaming.clearlag.services;

import io.github.lxgaming.clearlag.ClearLag;
import org.spongepowered.api.scheduler.Task;

public abstract class AbstractService implements Runnable {
    
    private boolean asynchronous;
    private long delay;
    private long interval;
    private Task task;
    
    @Override
    public final void run() {
        try {
            executeService();
        } catch (Exception ex) {
            ClearLag.getInstance().getLogger().error("Encountered an error processing {}::run", getClass().getSimpleName(), ex);
            getTask().cancel();
        }
    }
    
    public abstract boolean prepareService();
    
    public abstract void executeService();
    
    public final boolean isAsynchronous() {
        return asynchronous;
    }
    
    protected final void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }
    
    public final long getDelay() {
        return delay;
    }
    
    protected final void setDelay(long delay) {
        this.delay = delay;
    }
    
    public final long getInterval() {
        return interval;
    }
    
    protected final void setInterval(long interval) {
        this.interval = interval;
    }
    
    public Task getTask() {
        return task;
    }
    
    public void setTask(Task task) {
        this.task = task;
    }
}