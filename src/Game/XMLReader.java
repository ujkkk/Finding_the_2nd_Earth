package Game;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import java.awt.Graphics;
import java.awt.Image;
import java.io.*; 
//W3C definition for DOM and DOM Exceptions
import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NodeList;

//identify errors
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import javax.swing.*;

public class XMLReader {

	//XML의 루트 노드
	private Document XMLDoc;
	
	// All element names except elements for components
	static public String E_SPACEGAME = "SpaceGame";
	static public String E_SCREEN = "Screen";
	static public String E_GAMEPANEL = "GamePanel";
	static public String E_MAINPANEL = "MainPanel";
	static public String E_ACTIVEMAP = "ActiveMap";
	static public String E_SELECTPANEL = "SelectPanel";
	static public String E_BG = "Bg";
	static public String E_RECTANGLE = "Rectangle";
	static public String E_ENEMY = "Enemy";
	static public String E_ITEM = "Item";
	static public String E_GOAL = "Goal";
	static public String E_CHARACTER = "Character";
	static public String E_SIZE = "Size";
	static public String E_TILE = "Tile";
	static public String E_CLOSEDAREA = "ClosedArea";
	static public String E_POINT = "Point";
	static public String E_OBJ = "Obj";
	
	private Node spaceGameElement=null;
	private Node activeMapElement=null;
	private Node closedAreaElement=null;
	private Node screenElement=null;
	private Node gamePanelElement=null;
	private Node mainPanelElement=null;
	private Node selectPanelElement = null;
	private Node bgElement=null;
	private Node rectangleElement=null;
	
	public Node getSpaceGameElement() {return spaceGameElement;}
	public Node getActiveMapElement() {return activeMapElement;}
	public Node getScreenElement() {return screenElement;}
	public Node getClosedAreaElement() {return closedAreaElement;}
	public Node getGamePanelElement() {return gamePanelElement;}
	public Node getMainPanelElement() {return mainPanelElement;}
	public Node getSelectPanelElement() {return selectPanelElement;}
	public Node getBgElement() {return bgElement;}
	public Node getRectangleElement() {return rectangleElement;}

	private PrintWriter out;

	public XMLReader(String XMLFile) {

		read(XMLFile); // XMLFile을 읽어 파싱하고 XMLDoc를 생성한다.
		process(XMLDoc);	
		
		
		// 이 이하 라인들을 모두 잘 읽었는지를 확인하기 위한 디버깅 코드로서 생략해도 됨
		ByteArrayOutputStream byteStream=null;
		try {
			byteStream = new ByteArrayOutputStream();
	    	OutputStreamWriter writer = new OutputStreamWriter(byteStream, "UTF-8");
			this.out = new PrintWriter(writer, true);
	    	//this.out = new PrintWriter(System.out, true);
		}
		catch(IOException ioe) {
			return;
		}
		
		new DEBUG_echo(XMLDoc, out);
		
		out.flush();
	}
	
	// XMLFile을 읽고 파싱하여 XMLDocument 객체 생성
	private void read(String XMLFile) {
		DocumentBuilderFactory      factory=null;
	    DocumentBuilder             builder=null;

		factory = DocumentBuilderFactory.newInstance();

		// set  configuration options
//		factory.setValidating(true);
		factory.setIgnoringComments(false);
		factory.setIgnoringElementContentWhitespace(false);
		try {
		    builder = factory.newDocumentBuilder();

			// set error handler before parse() is called
			// if validation signal is set on, error handler should be attached
			OutputStreamWriter errStreamWriter = new OutputStreamWriter(System.err, "UTF-8");
	    	builder.setErrorHandler(new XMLBuilderErrorHandler(new PrintWriter(errStreamWriter, true)));

			File f = new File(XMLFile);
		    XMLDoc = builder.parse(f);
		}
		catch (SAXException sxe) {
		    // Error generated during parsing
		    Exception  x = sxe;
		    if (sxe.getException() != null)
				x = sxe.getException();
		    x.printStackTrace();
		}
		catch (ParserConfigurationException pce) {
			// Parser with specified options can't be built
			pce.printStackTrace();
		}
		catch (IOException ioe) {
			// I/O error
		    ioe.printStackTrace();
		} // end of try-catch block
		
	}
	

	public void process(Node parentNode) {
      for (Node node = parentNode.getFirstChild(); node != null;
           node = node.getNextSibling()) { // parentNode의 일차 자식들 검색
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue; // we search for element nodes
		    if(node.getNodeName().equals(E_SPACEGAME))
				spaceGameElement = node;
		    else if(node.getNodeName().equals(E_SCREEN)) {
		    	screenElement = node;
		    }
		    else if(node.getNodeName().equals(E_BG)) {
				bgElement = node;
		    }
		    else if(node.getNodeName().equals(E_GAMEPANEL)) {
		    	gamePanelElement = node;
		    }
		    else if(node.getNodeName().equals(E_ACTIVEMAP)) {
		    	activeMapElement = node;
		    }
		    else if(node.getNodeName().equals(E_CLOSEDAREA)) {
		    	closedAreaElement = node;
		    }
		    else if(node.getNodeName().equals(E_SELECTPANEL)) {
		    	selectPanelElement = node;
		    }
			else if(node.getNodeName().equals(E_TILE)) {
				
			}
			else if(node.getNodeName().equals(E_OBJ)) {
			}
			printNode(node);
			process(node); // recursion
      }
	} // end of method

	void printNode(Node element) {
		// print node name
		System.out.print(element.getNodeName()+ " ");
		
		// print all attrs
		
		// get a list of Atribute Nodes
		NamedNodeMap attrs = element.getAttributes();
		
		for(int i=0; i<attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			String value = attr.getNodeValue();
			System.out.print(name + "=" + value +" ");			
		}// end of for	
		
		//String text = element.getTextContent();
		//System.out.println(text);
		System.out.println();
	}

	static public Node getNode(Node parentNode, String nodeName) {
		Node node = null;
		for (node = parentNode.getFirstChild(); node != null;
         node = node.getNextSibling()) {
			if(node.getNodeType() != Node.ELEMENT_NODE)
				continue; // we search for element nodes
		    if(node.getNodeName().equals(nodeName))
				return node;
		    else {
		    	Node n = getNode(node, nodeName);
		    	if(n != null)
		    		return n;
		    }
		}
		return node;	
	}
	static public String getAttr(Node element, String attrName)
	{
		// get a list of Atribute Nodes
		NamedNodeMap attrs = element.getAttributes();
		for(int i=0; i<attrs.getLength(); i++) {
			Node attr = attrs.item(i);
			String name = attr.getNodeName();
			if(name.equals(attrName)) {
				return attr.getNodeValue();
			}
		}// end of for

		return null; // error or default
	}
	
	// Error handler to report errors and warnings
	class XMLBuilderErrorHandler implements ErrorHandler
	{
		/** Error handler output goes here */
		private PrintWriter out;

		XMLBuilderErrorHandler(PrintWriter out) {
	            this.out = out;
		}

		private String getParseExceptionInfo(SAXParseException spe) {
			String systemId = spe.getSystemId();
		    if (systemId == null) {
			    systemId = "null";
			}
			String info = "URI=" + systemId + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();
			return info;
		}

		// The following methods are three standard SAX ErrorHandler methods.
		public void warning(SAXParseException spe) throws SAXException {
			out.println("Warning: " + getParseExceptionInfo(spe));
		}

		public void error(SAXParseException spe) throws SAXException {
			String message = "Error: " + getParseExceptionInfo(spe);
		    throw new SAXException(message);
		}

		public void fatalError(SAXParseException spe) throws SAXException {
			String message = "Fatal Error: " + getParseExceptionInfo(spe);
			throw new SAXException(message);
		}
	} // end of class
}

class DEBUG_echo
{
	private PrintWriter out;
	private int indent;

	DEBUG_echo(Document d, PrintWriter w)
	{
		out = w;
		echo(d); // XMLDoc is a kind of Node
	}

  private void outputIndentation() {
      for (int i = 0; i < indent; i++) {
          out.print("   "); // print indent
      }
  }

  private void printlnCommon(Node n) {
      out.print(" nodeName=\"" + n.getNodeName() + "\"");

      String val = n.getNamespaceURI();
      if (val != null) {
          out.print(" uri=\"" + val + "\"");
      }

      val = n.getPrefix();
      if (val != null) {
          out.print(" pre=\"" + val + "\"");
      }

      val = n.getLocalName();
      if (val != null) {
          out.print(" local=\"" + val + "\"");
      }

      val = n.getNodeValue();
      if (val != null) {
          out.print(" nodeValue=");
          if (val.trim().equals("")) {
              // Whitespace
              out.print("[WS]");
          } else {
              out.print("\"" + n.getNodeValue() + "\"");
          }
      }
      out.println();
  }

	protected void echo(Node n)
	{
      // Indent to the current level before printing anything
      outputIndentation();

      int type = n.getNodeType();
      switch (type) {
      case Node.ATTRIBUTE_NODE:
          out.print("ATTR:");
          printlnCommon(n);
          break;
      case Node.CDATA_SECTION_NODE:
          out.print("CDATA:");
          printlnCommon(n);
          break;
      case Node.COMMENT_NODE:
          out.print("COMM:");
          printlnCommon(n);
          break;
      case Node.DOCUMENT_FRAGMENT_NODE:
          out.print("DOC_FRAG:");
          printlnCommon(n);
          break;
      case Node.DOCUMENT_NODE:
          out.print("DOC:");
          printlnCommon(n);
          break;
      case Node.DOCUMENT_TYPE_NODE:
          out.print("DOC_TYPE:");
          printlnCommon(n);

          // Print entities if any
          NamedNodeMap nodeMap = ((DocumentType)n).getEntities();
          indent += 2;
          for (int i = 0; i < nodeMap.getLength(); i++) {
              Entity entity = (Entity)nodeMap.item(i);
              echo(entity);
          }
          indent -= 2;
          break;
      case Node.ELEMENT_NODE:
          out.print("ELEM:");
          printlnCommon(n);

          // Print attributes if any.  Note: element attributes are not
          // children of ELEMENT_NODEs but are properties of their
          // associated ELEMENT_NODE.  For this reason, they are printed
          // with 2x the indent level to indicate this.
          NamedNodeMap atts = n.getAttributes();
          indent += 2;
          for (int i = 0; i < atts.getLength(); i++) {
              Node att = atts.item(i);
              echo(att);
          }
          indent -= 2;
          break;
      case Node.ENTITY_NODE:
          out.print("ENT:");
          printlnCommon(n);
          break;
      case Node.ENTITY_REFERENCE_NODE:
          out.print("ENT_REF:");
          printlnCommon(n);
          break;
      case Node.NOTATION_NODE:
          out.print("NOTATION:");
          printlnCommon(n);
          break;
      case Node.PROCESSING_INSTRUCTION_NODE:
          out.print("PROC_INST:");
          printlnCommon(n);
          break;
      case Node.TEXT_NODE:
          out.print("TEXT:");
          printlnCommon(n);
          break;
      default:
          out.print("UNSUPPORTED NODE: " + type);
          printlnCommon(n);
          break;
      }

      // Print children if any
      indent++;
      for (Node child = n.getFirstChild(); child != null;
           child = child.getNextSibling()) {
          echo(child);
      }
      indent--;
  } // end of method

}


