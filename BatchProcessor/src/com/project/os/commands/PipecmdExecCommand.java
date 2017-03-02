package com.project.os.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

import com.project.os.exceptionhandler.ProcessException;

public class PipecmdExecCommand extends Command {
	
	public void parse(Element element) throws Exception {
		
		String cmdName = element.getNodeName();
		if (cmdName == null) {
			throw new ProcessException("unable to parse command from " + element.getTextContent());
		} else if ("exec".equalsIgnoreCase(cmdName)) {
			
			System.out.println("Parsing pipecmdexec command");		
			id = element.getAttribute("id");
			if (id == null || id.isEmpty()) {
				throw new ProcessException("Missing ID in pipecmdexec command");
			}
			System.out.println("pipecmdexec command ID: " + id);

			path = element.getAttribute("path");
			if (path == null || path.isEmpty()) {
				throw new ProcessException("Missing PATH in pipecmdexec command");
			}
			System.out.println("pipecmdexec command path: " + path);

			String arg = element.getAttribute("args");
			if (!(arg == null || arg.isEmpty())) {
				System.out.println("pipecmdexec command args: " + arg);
				StringTokenizer st = new StringTokenizer(arg);
				while (st.hasMoreTokens()) {
					String tok = st.nextToken();
					cmdArgs.add(tok);
				}
			}

			input = element.getAttribute("in");
			if (!(input == null || input.isEmpty())) {
				System.out.println("pipecmdexec command inID: " + input);
			}

			output = element.getAttribute("out");
			if (!(output == null || output.isEmpty())) {
				System.out.println("pipecmdexec command outID: " + output);
			}
		} else {
			throw new ProcessException("unable to parse command from " + element.getTextContent());		}
	}

	@Override
	public void execute(Map<String, Command> map) throws Exception {
		// Empty Function
	}
	
	public void execute(Map<String, Command> map, String pipeid) throws Exception {
		PipecmdCommand pipeCmdCommand = (PipecmdCommand) map.get(pipeid);
		List<Command> pipeExecCommands = pipeCmdCommand.pipeExecCmds;
		
		PipecmdExecCommand commandOne = (PipecmdExecCommand) pipeExecCommands.get(0);
		PipecmdExecCommand commandTwo = (PipecmdExecCommand) pipeExecCommands.get(1);
		
		ProcessBuilder builderOne = new ProcessBuilder();
		ProcessBuilder builderTwo = new ProcessBuilder();
		
		System.out.print("Before Checking Map: ");
		System.out.println("Input: " + commandOne.input + " Output: " + commandTwo.output);
		
		if((commandOne.input == null || commandOne.input.isEmpty())) {
			throw new ProcessException("input file not found for " + commandOne.path );
		}
		
		for(Map.Entry<String, Command> entry : map.entrySet())
		{ 
			//System.out.println("Map Key: " + entry.getKey());
			if(entry.getKey().equals(commandOne.input)) {
				Command temp = entry.getValue();
				commandOne.input = temp.path;
			}
			if(entry.getKey().equals(commandTwo.output)) {
				Command temp = entry.getValue();
				commandTwo.output = temp.path;
			}			
		}
		
		System.out.print("After Checking Map: ");
		System.out.println("Input: " + commandOne.input + " Output: " + commandTwo.output);
		
		//Populating list for process one
		List<String> listOne = new ArrayList<String>();
		listOne.add(commandOne.path);
		for(String argi: commandOne.cmdArgs) {
			listOne.add(argi);
		}
		/*System.out.println("List for pipecmdexec one after Build: ");
		for(String l: listOne) {
			System.out.println(l);
		}*/
		builderOne.command(listOne);
		File wd = builderOne.directory();		
		
		//Populating list for process two
		List<String> listTwo = new ArrayList<String>();
		listTwo.add(commandTwo.path);
		for(String argi: commandTwo.cmdArgs) {
			listTwo.add(argi);
		}
		/*System.out.println("List for pipecmdexec two after Build: ");
		for(String l: listTwo) {
			System.out.println(l);
		}*/
		builderTwo.command(listTwo);
		File wd2 = builderTwo.directory();
		
		//Starting process one and process two
		Process processOne = builderOne.start();
		Process processTwo = builderTwo.start();
		
		System.out.println(commandOne.describe());
		System.out.println(commandTwo.describe());
		
		if(commandOne.input != null) {
			OutputStream os_old = processOne.getOutputStream();
			FileInputStream fis_old = new FileInputStream(new File(wd, commandOne.input));
			int achar;
			while ((achar = fis_old.read()) != -1) {
				os_old.write(achar);
			}
			os_old.close();
		} 
		
		InputStream is_old = processOne.getInputStream();
		OutputStream os_new = processTwo.getOutputStream();
		
		int achar;
		while ((achar = is_old.read()) != -1) {
			os_new.write(achar);
		}
		os_new.close();
		
		if(commandTwo.output != null) {	
			
			InputStream is_new = processTwo.getInputStream();
			
			File outfile = new File(wd2, commandTwo.output);
			FileOutputStream fos_new = new FileOutputStream(outfile);
			
			int c;
			while ((c = is_new.read()) != -1) {
				fos_new.write(c);
			}
			fos_new.close();
		}
	}

	@Override
	public String describe() {
		return ("Executing pipecmdexec command");
	}

}
