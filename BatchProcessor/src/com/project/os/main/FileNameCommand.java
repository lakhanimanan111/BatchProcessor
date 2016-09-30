package com.project.os.main;

import java.io.IOException;
import java.util.Map;

import org.w3c.dom.Element;

public class FileNameCommand extends Command {

	@Override
	public void parse(Element element) throws Exception {
		id = element.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
		System.out.println("ID: " + id);
		
		
		path = element.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in CMD Command");
		}
		System.out.println("Path: " + path);
		
	}

	@Override
	public void execute(Map<String, Command> map) throws IOException, InterruptedException {
		// Nothing to DO
	}

	@Override
	public String describe() {
		return "Executing fileName command";
	}

}
