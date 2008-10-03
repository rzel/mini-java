package mini.java.lex.legacy;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import mini.java.regex.legacy.RegularExpression;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * 
 * A helper class reading the spec file.
 *
 */
public class JDomParser {
	private String fileName;

	private Vector<RegularExpression> regExps;

	private Vector<Modifier> mods;

	public JDomParser(String fn) {
		regExps = new Vector<RegularExpression>();
		mods = new Vector<Modifier>();
		fileName = fn;
		System.out.println("** File Name: " + fileName);
	}

	public Iterator<Modifier> getModifiers() {
		return mods.iterator();
	}

	public Iterator<RegularExpression> getRegularExpressions() {
		return regExps.iterator();
	}

	@SuppressWarnings("unchecked")
	public void jDomParser() throws Exception {
	    // FIXME new implementation
//		SAXBuilder builder = new SAXBuilder(false);
//		try {
//			Document doc = builder.build(fileName);
//			Element rootEle = doc.getRootElement();
//			Element tokensEle = rootEle.getChild("tokens");
//			List<Element> linklist = tokensEle.getChildren("token");
//			for (Iterator<Element> it = linklist.iterator(); it.hasNext();) {
//				Element link = (Element) it.next();
//
//				RegularExpression re = new RegularExpression();
//				re.setType(link.getChildText("Type"));
//				re.setRegExpress(link.getChildText("RegularExpression"));
//				regExps.add(re);
//			}
//			Element modifiersEle = rootEle.getChild("modifiers");
//			if (modifiersEle != null) {
//				List<Element> modifierList = modifiersEle
//						.getChildren("modifier");
//
//				for (Iterator<Element> iter = modifierList.iterator(); iter
//						.hasNext();) {
//					Element mod = iter.next();
//					String modName = mod.getAttributeValue("name");
//					mods.add((Modifier) Class.forName(modName).newInstance());
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	// public void showLink(){
	// Iterator<RegularExpress> it=vec.iterator();
	// int index=0;
	// System.out.println("*******************Parsing of
	// JDOM*******************");
	// while(it.hasNext()){
	// System.out.println("------------------------------------------------");
	// System.out.println("The Link NO "+(++index));
	// it.next().showLink();
	// System.out.println("");
	// }
	// }
	// // public static void main(String[] args) throws Exception{
	// // // TODO Auto-generated method stub
	// // JDomParser parser=new JDomParser();
	// // parser.jDomParser();
	// // parser.showLink();
	// //
	// // }
}
