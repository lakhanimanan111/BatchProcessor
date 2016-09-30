package com.project.os.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

public abstract class Command {
	
	String id;
	String path;
	List<String> cmdArgs = new ArrayList<String>();
	String input;
	String output;
	
	public abstract void parse(Element element)throws Exception;
	public abstract void execute(Map<String, Command> map) throws Exception;
	public abstract String describe() throws Exception;
}
