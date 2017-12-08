package avicit.ui.word;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class QueryXML {
	private QueryXML(){
		
	}

	public NodeList query(NodeList nodes){

	    try {
	    	if(nodes !=null){
	    	}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return nodes;
	}

	public QueryXML getInstance(){
		return instance;
	}

	public static QueryXML instance = new QueryXML();
	
}
