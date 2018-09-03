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
import io.github.lxgaming.clearlag.configuration.categories.TypeCategory;
import io.github.lxgaming.clearlag.data.ClearData;
import io.github.lxgaming.clearlag.managers.ClearManager;
import io.github.lxgaming.clearlag.util.Broadcast;
import io.github.lxgaming.clearlag.util.Toolbox;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.text.Text;

public final class ClearService extends AbstractService {
    
    @Override
    public boolean prepareService() {
        setAsynchronous(true);
        setDelay(0L);
        setInterval(1000L);
        return true;
    }
    
    @Override
    public void executeService() {
        if (Sponge.getGame().getState() != GameState.SERVER_STARTED) {
            return;
        }
        
        ClearManager.getAllClearData().forEach(clearData -> {
            ClearLag.getInstance().getConfig().map(clearData.getConfigFunction()).ifPresent(typeCategory -> processTypeCategory(clearData, typeCategory));
        });
    }
    
    private void processTypeCategory(ClearData clearData, TypeCategory typeCategory) {
        if (clearData == null || typeCategory == null || clearData.getRemoving().get()) {
            return;
        }
        
        if (clearData.getOrder() == Order.POST) {
            clearData.setOrder(Order.PRE);
            Broadcast.builder()
                    .message(formatMessage(clearData, typeCategory.getMessageCategory().getPostClear()))
                    .permission("clearlag.messages.post")
                    .type(typeCategory.getMessageCategory().getPostClearType())
                    .build().sendMessage();
            return;
        }
        
        long interval = clearData.getInterval();
        if (clearData.getInitializeTime() <= 0L) {
            clearData.setInitializeTime(System.currentTimeMillis());
        } else {
            interval -= System.currentTimeMillis() - clearData.getInitializeTime();
        }
        
        if (interval <= 0L) {
            clearData.setInitializeTime(0);
            clearData.setInterval(typeCategory.getInterval());
            clearData.setOrder(Order.POST);
            clearData.setWarningInterval(0);
            clearData.getRemoved().clear();
            if (clearData.getRemoving().compareAndSet(false, true)) {
                Broadcast.builder()
                        .message(formatMessage(clearData, typeCategory.getMessageCategory().getPreClear()))
                        .permission("clearlag.messages.pre")
                        .type(typeCategory.getMessageCategory().getPreClearType())
                        .build().sendMessage();
            } else {
                Broadcast.builder()
                        .message(Text.of(clearData.getName(), " unexpected value"))
                        .permission("clearlag.messages.error")
                        .type(Broadcast.Type.CONSOLE)
                        .build().sendMessage();
            }
            
            return;
        }
        
        for (long warningInterval : typeCategory.getMessageCategory().getWarningIntervals()) {
            if (clearData.getInterval() < warningInterval || (clearData.getWarningInterval() <= warningInterval && clearData.getWarningInterval() > 0)) {
                continue;
            }
            
            if (interval <= warningInterval) {
                clearData.setWarningInterval(warningInterval);
                Broadcast.builder()
                        .message(formatMessage(clearData, typeCategory.getMessageCategory().getWarning()))
                        .permission("clearlag.messages.warning")
                        .type(typeCategory.getMessageCategory().getWarningType())
                        .build().sendMessage();
                return;
            }
        }
    }
    
    private static Text formatMessage(ClearData clearData, String format) {
        String message = format;
        message = StringUtils.replace(message, "[ID]", clearData.getId());
        message = StringUtils.replace(message, "[NAME]", clearData.getName());
        message = StringUtils.replace(message, "[COUNT]", "" + clearData.getRemoved().size());
        message = StringUtils.replace(message, "[TIME]", Toolbox.getTimeString(clearData.getWarningInterval()));
        if (StringUtils.isBlank(message)) {
            return Text.EMPTY;
        }
        
        return Toolbox.convertColor(message);
    }
}