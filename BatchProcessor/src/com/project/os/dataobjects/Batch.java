package com.project.os.dataobjects;

import java.util.*;

import com.project.os.commands.Command;

public class Batch {

	private Map<String, Command> cmdLookup = new HashMap<String, Command>();
	private List<Command> commandList = new ArrayList<Command>();
	
	public List<Command> getCommandList() {
		return commandList;
	}
	
	public void addCommand(Command command) {
		commandList.add(command);
		cmdLookup.put(command.id, command);
	}
	
	public Map<String, Command> getCommands() {
		return cmdLookup;
	}
}
