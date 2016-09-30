package com.project.os.main;

import java.io.File;

public class BatchProcessor {

	public static void main(String[] args) {
		BatchParser parser = new BatchParser();
		try {
			String filename = null;
			if(args.length > 0) {
				filename = args[0];
			}
			else {
				filename = "C:/Users/manan/workspace/BatchProcessor/batch1.xml";
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
		
		for(Command command : batch.commandList) {
			System.out.println(command.describe());
			command.execute(batch.getCommands());
		}
		System.out.println("Program terminated!");
	}

}
