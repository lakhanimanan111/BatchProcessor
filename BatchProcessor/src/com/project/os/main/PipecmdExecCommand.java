package com.project.os.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

public class PipecmdExecCommand extends Command {

	private static OutputStream prev_os;
	
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
		
		String arg = element.getAttribute("args");
		if (!(arg == null || arg.isEmpty())) {
			StringTokenizer st = new StringTokenizer(arg);
			while (st.hasMoreTokens()) {
				String tok = st.nextToken();
				cmdArgs.add(tok);
			}
		}

		input = element.getAttribute("in");
		if (!(input == null || input.isEmpty())) {
			System.out.println("inID: " + input);		
		}

		output = element.getAttribute("out");
		if (!(output == null || output.isEmpty())) {
			System.out.println("outID: " + output);
		}		
	}

	@Override
	public void execute(Map<String, Command> map) throws Exception {
		ProcessBuilder builder = new ProcessBuilder();		
		System.out.println("Before Checking Map Input: " + input + " Before Output: " + output);
		
		for(Map.Entry<String, Command> entry : map.entrySet())
		{ 
			System.out.println("Map Key: " + entry.getKey());
			if(entry.getKey().equals(input)) {
				Command temp = entry.getValue();
				input = temp.path;
			}
			if(entry.getKey().equals(output)) {
				Command temp = entry.getValue();
				output = temp.path;
			}			
		}
		System.out.println("After Checking Map Input: " + input + " Output: " + output);
		
		List<String> list = new ArrayList<String>();
		list.add(path);
		for(String argi: cmdArgs) {
			list.add(argi);
		}
		System.out.println("List after Build: ");
		for(String l: list) {
			System.out.println(l);
		}
		builder.command(list);
		
		builder.directory(new File("C:/Users/manan/workspace/BatchProcessor/"));
		File wd = builder.directory();
		System.out.println("Current working directory is " + wd);
		
//		if(input != null)
//			builder.redirectInput(new File(wd, input));
//			
//		if(output != null)
//			builder.redirectOutput(new File(wd, output));
		//builder.redirectError(new File(wd, "error.txt"));

		Process process = builder.start();
		
		if(input != null){
			OutputStream os = process.getOutputStream();
			FileInputStream fis = new FileInputStream(new File(wd, input));
			int achar;
			while ((achar = fis.read()) != -1) {
				os.write(achar);
			}
			os.close();
		} else {
			File outfile = new File(wd, output);
			FileOutputStream fos = new FileOutputStream(outfile);
			InputStream is = process.getInputStream();
			int achar;
			while ((achar = is.read()) != -1) {
				fos.write(achar);
			}
			fos.close();
			
			System.out.println("Program terminated!");
		}

	}

	@Override
	public String describe() {
		System.out.println("Executing PipeExec Command ID: " + id);
		return null;
	}

}
