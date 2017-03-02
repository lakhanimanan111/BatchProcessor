package com.project.os.main;

import java.io.File;

import com.project.os.commands.Command;
import com.project.os.controller.BatchParser;
import com.project.os.dataobjects.Batch;
import com.project.os.exceptionhandler.ProcessException;

public class BatchProcessor {

	public static void main(String[] args) {
		BatchParser parser = new BatchParser();
		try {
			String filename = null;
			if(args.length > 0) {
				filename = args[0];
			}
			else {
				throw new ProcessException("Filename Not Specified. Unable to Continue!");
			}
			System.out.println("Opening " + filename);
			File f = new File(filename);
			Batch batch = parser.buildBatch(f);
			executeBatch(batch);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
	
	public static void executeBatch(Batch batch) throws Exception {
		
		for(Command command : batch.getCommandList()) {
			System.out.println(command.describe());
			command.execute(batch.getCommands());
		}
		System.out.println("Batch file execution completed!");
	}
}
