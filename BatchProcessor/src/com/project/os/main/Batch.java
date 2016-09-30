package com.project.os.main;

import java.util.*;

public class Batch {

	private Map<String, Command> cmdLookup = new HashMap<String, Command>();
	List<Command> commandList = new ArrayList<Command>();
	
	public void addCommand(Command command) {
		commandList.add(command);
		cmdLookup.put(command.id, command);
	}
	
	public Map<String, Command> getCommands() {
		return cmdLookup;
	}
}
