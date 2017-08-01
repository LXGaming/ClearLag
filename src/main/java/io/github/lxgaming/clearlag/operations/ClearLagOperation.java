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

package io.github.lxgaming.clearlag.operations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Sponge;

import io.github.lxgaming.clearlag.ClearLag;
import io.github.lxgaming.clearlag.util.SpongeHelper;

public class ClearLagOperation extends Operation {
	
	private final Map<String, Long> lastRunTimes;
	
	public ClearLagOperation() {
		lastRunTimes = new HashMap<String, Long>();
	}
	
	@Override
	public void run() {
		if (ClearLag.getInstance().getConfig() == null || Sponge.getGame().getState() == null || !Sponge.getGame().getState().equals(GameState.SERVER_STARTED)) {
			return;
		}
		
		if (getLastRunTimes().getOrDefault("Item", 0L) <= 0L && getLastRunTimes().getOrDefault("Mob", 0L) <= 0L) {
			getLastRunTimes().put("Item", System.currentTimeMillis());
			getLastRunTimes().put("Mob", System.currentTimeMillis());
		}
		
		if (ClearLag.getInstance().getConfig().getItemInterval() > 0) {
			broadcastMessage("Item",
					ClearLag.getInstance().getConfig().getItemWarningMessage(),
					ClearLag.getInstance().getConfig().getItemWarningIntervals(),
					ClearLag.getInstance().getConfig().getItemInterval(),
					ClearLag.getInstance().getConfig().isItemWarnings());
			
			if (getLastRunTimes().getOrDefault("Item", 0L) < (System.currentTimeMillis() - ClearLag.getInstance().getConfig().getItemInterval())) {
				ClearLag.getInstance().getEntityManager().removeItems();
				getLastRunTimes().put("Item", System.currentTimeMillis());
				getLastRunTimes().put("Item-Warning", 0L);
			}
		}
		
		if (ClearLag.getInstance().getConfig().getMobInterval() > 0) {
			broadcastMessage("Mob",
					ClearLag.getInstance().getConfig().getMobWarningMessage(),
					ClearLag.getInstance().getConfig().getMobWarningIntervals(),
					ClearLag.getInstance().getConfig().getMobInterval(),
					ClearLag.getInstance().getConfig().isMobWarnings());
			
			if (getLastRunTimes().getOrDefault("Mob", 0L) < (System.currentTimeMillis() - ClearLag.getInstance().getConfig().getMobInterval())) {
				ClearLag.getInstance().getEntityManager().removeMobs();
				getLastRunTimes().put("Mob", System.currentTimeMillis());
				getLastRunTimes().put("Mob-Warning", 0L);
			}
		}
	}
	
	private void broadcastMessage(String type, String message, List<Integer> warningIntervals, long interval, boolean warnings) {
		if (StringUtils.isBlank(type) || StringUtils.isBlank(message) || warningIntervals == null || interval < 0 || !warnings) {
			return;
		}
		
		int lastWarning = getLastRunTimes().getOrDefault(type + "-Warning", 0L).intValue();
		while (warningIntervals.size() > lastWarning && warningIntervals.get(lastWarning) > interval) {
			lastWarning++;
		}
		
		if (warningIntervals.size() <= lastWarning) {
			return;
		}
		
		int warningInterval = warningIntervals.get(lastWarning);
		if ((interval - warningInterval) <= System.currentTimeMillis() - getLastRunTimes().getOrDefault(type, 0L)) {
			SpongeHelper.broadcastMessage(message, (warningInterval / 1000));
			getLastRunTimes().put(type + "-Warning", lastWarning + 1L);
		}
	}
	
	@Override
	public String getName() {
		return "ClearLagOperation";
	}
	
	@Override
	public boolean isAsync() {
		return true;
	}
	
	@Override
	public long getInterval() {
		return 1000;
	}
	
	public Map<String, Long> getLastRunTimes() {
		return lastRunTimes;
	}
}