package com.project.os.commands;

import java.io.File;
import java.io.IOException;
import java.util.*;

import org.w3c.dom.Element;

import com.project.os.exceptionhandler.ProcessException;

public class ExecCommand extends Command {

	@Override
	public void parse(Element element) throws Exception {
		
		id = element.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in exec command");
		}
		System.out.println("exec command ID: " + id);
		
		path = element.getAttribute("path");
		if (path == null || path.isEmpty()) {
			throw new ProcessException("Missing PATH in exec command");
		}
		System.out.println("exec command path: " + path);
		
		String arg = element.getAttribute("args");
		if (!(arg == null || arg.isEmpty())) {
			System.out.println("exec command args: " + arg);
			StringTokenizer st = new StringTokenizer(arg);
			while (st.hasMoreTokens()) {
				String tok = st.nextToken();
				cmdArgs.add(tok);
			}
		} 

		input = element.getAttribute("in");
		if (!(input == null || input.isEmpty())) {
			System.out.println("exec command inID: " + input);
		}
		
		if( !path.equalsIgnoreCase("cmd") && (input == null || input.isEmpty()) ) {
			throw new ProcessException("Input file not found for " + path );
		}

		output = element.getAttribute("out");
		if (!(output == null || output.isEmpty())) {
			System.out.println("exec command outID: " + output);
		}
	}

	@Override
	public void execute(Map<String, Command> map) throws IOException, InterruptedException {
		
		ProcessBuilder builder = new ProcessBuilder();		
		System.out.print("Before Checking Map: ");
		System.out.println("Input: " + input + " Output: " + output);
		
		for(Map.Entry<String, Command> entry : map.entrySet())
		{ 
			//System.out.println("Map Key: " + entry.getKey());
			if(entry.getKey().equals(input)) {
				Command temp = entry.getValue();
				input = temp.path;
			}
			if(entry.getKey().equals(output)) {
				Command temp = entry.getValue();
				output = temp.path;
			}			
		}
		System.out.print("After Checking Map: ");
		System.out.println("Input: " + input + " Output: " + output);
		
		List<String> list = new ArrayList<String>();
		list.add(path);
		for(String argi: cmdArgs) {
			list.add(argi);
		}
		/*System.out.println("List for exec command:");
		for(String l: list) {
			System.out.println(l);
		}*/
		builder.command(list);
		
		File wd = builder.directory();
		
		if(input != null && !input.isEmpty())
			builder.redirectInput(new File(wd, input));
			
		if(output != null && !output.isEmpty())
			builder.redirectOutput(new File(wd, output));

		Process process = builder.start();
		process.waitFor();
	}

	@Override
	public String describe() {
		return "Executing exec command";
	}

}
