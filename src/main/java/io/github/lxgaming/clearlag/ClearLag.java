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

package io.github.lxgaming.clearlag;

import com.google.inject.Inject;
import io.github.lxgaming.clearlag.configuration.Config;
import io.github.lxgaming.clearlag.configuration.Configuration;
import io.github.lxgaming.clearlag.configuration.category.GeneralCategory;
import io.github.lxgaming.clearlag.manager.ClearManager;
import io.github.lxgaming.clearlag.manager.CommandManager;
import io.github.lxgaming.clearlag.service.ClearService;
import io.github.lxgaming.clearlag.util.Reference;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Plugin(
        id = Reference.ID,
        name = Reference.NAME,
        version = Reference.VERSION,
        description = Reference.DESCRIPTION,
        authors = {Reference.AUTHORS},
        url = Reference.WEBSITE
)
public class ClearLag {
    
    private static ClearLag instance;
    
    @Inject
    private PluginContainer pluginContainer;
    
    @Inject
    private Logger logger;
    
    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path path;
    
    private Configuration configuration;
    
    @Listener
    public void onConstruction(GameConstructionEvent event) {
        instance = this;
        configuration = new Configuration(this.path);
    }
    
    @Listener
    public void onInitialization(GameInitializationEvent event) {
        CommandManager.prepare();
    }
    
    @Listener
    public void onLoadComplete(GameLoadCompleteEvent event) {
        if (!reload()) {
            getLogger().error("Failed to load");
            return;
        }
        
        Task.builder()
                .async()
                .name(Reference.NAME)
                .interval(1000L, TimeUnit.MILLISECONDS)
                .execute(new ClearService())
                .submit(getPluginContainer());
    }
    
    @Listener
    public void onStartingServer(GameStartingServerEvent event) {
        getLogger().info("{} v{} has started.", Reference.NAME, Reference.VERSION);
    }
    
    @Listener
    public void onStopping(GameStoppingEvent event) {
        getLogger().info("{} v{} has stopped.", Reference.NAME, Reference.VERSION);
    }
    
    public boolean reload() {
        getConfiguration().loadConfiguration();
        if (!getConfig().isPresent()) {
            return false;
        }
        
        ClearManager.getAllClearData().forEach(ClearManager::processTypeCategory);
        getConfiguration().saveConfiguration();
        return true;
    }
    
    public void debug(String format, Object... arguments) {
        if (getConfig().map(Config::getGeneralCategory).map(GeneralCategory::isDebug).orElse(false)) {
            getLogger().info(format, arguments);
        }
    }
    
    public static ClearLag getInstance() {
        return instance;
    }
    
    public PluginContainer getPluginContainer() {
        return pluginContainer;
    }
    
    public Logger getLogger() {
        return logger;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }
    
    public Optional<Config> getConfig() {
        if (getConfiguration() != null) {
            return Optional.ofNullable(getConfiguration().getConfig());
        }
        
        return Optional.empty();
    }
}