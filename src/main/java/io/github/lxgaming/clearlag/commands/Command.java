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

package io.github.lxgaming.clearlag.commands;

import java.util.List;

import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import io.github.lxgaming.clearlag.util.Reference;

public abstract class Command implements CommandExecutor {
	
	public abstract String getName();
	
	public Text getDescription() {
		return Text.of("No description provided");
	}
	
	public String getUsage() {
		return getName();
	}
	
	public List<String> getAliases() {
		return null;
	}
	
	public String getPermission() {
		return Reference.PLUGIN_NAME + ".Command." + getName();
	}
	
	public List<CommandElement> getArguments() {
		return null;
	}
	
	public List<Command> getSubCommands() {
		return null;
	}
}