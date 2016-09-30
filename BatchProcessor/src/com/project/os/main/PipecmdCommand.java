package com.project.os.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PipecmdCommand extends Command {
	
	List<Command> pipeExecCmds = new ArrayList<Command>(); 
	@Override
	public void parse(Element element) throws Exception {
		id = element.getAttribute("id");
		if (id == null || id.isEmpty()) {
			throw new ProcessException("Missing ID in CMD Command");
		}
		System.out.println("ID: " + id);
		
		NodeList nodes = element.getChildNodes();
		for (int idx = 0; idx < nodes.getLength(); idx++) {
			Node node = nodes.item(idx);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element pipeExecElement = (Element) node;
				System.out.println("Element name is : " + pipeExecElement.getNodeName());
				Command pipeExecCommand = new PipecmdExecCommand();
				pipeExecCommand.parse(pipeExecElement);
				pipeExecCmds.add(pipeExecCommand);
			}
		}	
	}

	@Override
	public void execute(Map<String, Command> map)throws Exception {
		for(Command command : pipeExecCmds) {
			System.out.println(command.describe());
			command.execute(map);
		}		
	}

	@Override
	public String describe() {
		// TODO Auto-generated method stub
		return null;
	}

}
