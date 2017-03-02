package com.project.os.commands;

import java.io.IOException;
import java.util.Map;

import org.w3c.dom.Element;

import com.project.os.exceptionhandler.ProcessException;

public class FileNameCommand extends Command {

	@Override
	public void parse(Element element) throws Exception {
		id = element.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in filename command");
		}
		System.out.println("filename command ID: " + id);
		
		
		path = element.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in filename command");
		}
		System.out.println("filename command PATH: " + path);
		
	}

	@Override
	public void execute(Map<String, Command> map) throws IOException, InterruptedException {
		// Nothing to DO
	}

	@Override
	public String describe() {
		return "Executing filename command";
	}

}
