package mini.java.fa.legacy;
/**
 * 
 * This class represents a typical DFA Node.
 *
 */
public class DFANode {
	private int nodeId;
	private static DFANode START_NODE =
		new DFANode(0);
	
	public DFANode(int id) {
		nodeId = id;
	}
	
	public int getNodeId() {
		return nodeId;
	}
	
	public static DFANode getStartNode() {
		return START_NODE;
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof DFANode)) {
			return false;
		} else {
			DFANode node = (DFANode)obj;
			return this.nodeId == node.nodeId;
		}
	}
}
