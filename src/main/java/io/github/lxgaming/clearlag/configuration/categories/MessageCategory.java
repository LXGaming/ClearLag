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

import io.github.lxgaming.clearlag.util.Broadcast;
import io.github.lxgaming.clearlag.util.Toolbox;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Set;

@ConfigSerializable
public class MessageCategory {
    
    @Setting(value = "pre-clear", comment = "Message when clearing has started")
    private String preClear = "&8Preparing to clear [NAME]...";
    
    @Setting(value = "pre-clear-type", comment = "Chat type [ACTION_BAR, CHAT, CONSOLE, SYSTEM]")
    private Broadcast.Type preClearType = Broadcast.Type.CHAT;
    
    @Setting(value = "post-clear", comment = "Message when clearing has finished")
    private String postClear = "&f&l[COUNT] &8[NAME] have been cleared";
    
    @Setting(value = "post-clear-type", comment = "Chat type [ACTION_BAR, CHAT, CONSOLE, SYSTEM]")
    private Broadcast.Type postClearType = Broadcast.Type.CHAT;
    
    @Setting(value = "warning", comment = "Warning message")
    private String warning = "&f[NAME] will be cleared in &b&l[TIME]";
    
    @Setting(value = "warning-intervals", comment = "Warning message intervals")
    private Set<Long> warningIntervals = Toolbox.newHashSet(60000L, 30000L, 15000L, 5000L);
    
    @Setting(value = "warning-type", comment = "Chat type [ACTION_BAR, CHAT, CONSOLE, SYSTEM]")
    private Broadcast.Type warningType = Broadcast.Type.ACTION_BAR;
    
    public String getPreClear() {
        return preClear;
    }
    
    public Broadcast.Type getPreClearType() {
        return preClearType;
    }
    
    public String getPostClear() {
        return postClear;
    }
    
    public Broadcast.Type getPostClearType() {
        return postClearType;
    }
    
    public String getWarning() {
        return warning;
    }
    
    public Set<Long> getWarningIntervals() {
        return warningIntervals;
    }
    
    public Broadcast.Type getWarningType() {
        return warningType;
    }
}