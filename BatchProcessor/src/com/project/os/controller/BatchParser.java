package com.project.os.controller;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.os.commands.Command;
import com.project.os.commands.ExecCommand;
import com.project.os.commands.FileNameCommand;
import com.project.os.commands.PipecmdCommand;
import com.project.os.dataobjects.Batch;
import com.project.os.exceptionhandler.ProcessException;

public class BatchParser {

	public Batch buildBatch(File batchFile) throws Exception {
		Batch batch = new Batch();
		
		FileInputStream fis = new FileInputStream(batchFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fis);

		Element pnode = doc.getDocumentElement();
		NodeList nodes = pnode.getChildNodes();
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			Node node = nodes.item(idx);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element elem = (Element) node;
				System.out.println("Element name is : " + elem.getNodeName());
				Command addCmd = buildCommand(elem);
				batch.addCommand(addCmd);
			}
		}
		return batch;
	}
	
	private Command buildCommand(Element element) throws Exception {
		
		String cmdName = element.getNodeName();
		Command commandObj;
		
		if (cmdName == null) {
			throw new ProcessException("unable to parse command from " + element.getTextContent());
		}
		else if ("filename".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing filename");
			commandObj = new FileNameCommand();
			commandObj.parse(element);
		}
		else if ("exec".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing exec");
			commandObj = new ExecCommand();
			commandObj.parse(element);
		}
		else if ("pipecmd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing pipecmd");
			commandObj = new PipecmdCommand();
			commandObj.parse(element);
		}
		else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + element.getBaseURI());
		}
		return commandObj;
	}
}
