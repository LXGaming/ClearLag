/*
 * Copyright 2017 Alex Thomson
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

package io.github.lxgaming.clearlag;

import com.google.inject.Inject;
import io.github.lxgaming.clearlag.configuration.Configuration;
import io.github.lxgaming.clearlag.entries.Config;
import io.github.lxgaming.clearlag.managers.EntityManager;
import io.github.lxgaming.clearlag.managers.RegistryManager;
import io.github.lxgaming.clearlag.util.Reference;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

@Plugin(
        id = Reference.PLUGIN_ID,
        name = Reference.PLUGIN_NAME,
        version = Reference.PLUGIN_VERSION,
        description = Reference.DESCRIPTION,
        authors = {Reference.AUTHORS},
        url = Reference.WEBSITE
)
public class ClearLag {
    
    private static ClearLag instance;
    
    @Inject
    private Logger logger;
    
    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path path;
    
    private Configuration configuration;
    private EntityManager entityManager;
    private RegistryManager registryManager;
    
    @Listener
    public void onGamePreInit(GamePreInitializationEvent event) {
        instance = this;
        configuration = new Configuration();
        entityManager = new EntityManager();
        registryManager = new RegistryManager();
    }
    
    @Listener
    public void onGameInit(GameInitializationEvent event) {
        getConfiguration().loadConfiguration();
        getConfiguration().saveConfiguration();
        getRegistryManager().register();
    }
    
    public void debugMessage(String message, Object... objects) {
        if (getConfig() != null && getConfig().isDebug()) {
            getLogger().info(message, objects);
        }
    }
    
    public static ClearLag getInstance() {
        return instance;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public Path getPath() {
        return path;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Config getConfig() {
        if (getConfiguration() != null) {
            return getConfiguration().getConfig();
        }
        
        return null;
    }
    
    public EntityManager getEntityManager() {
        return entityManager;
    }
    
    public RegistryManager getRegistryManager() {
        return registryManager;
    }
}