package sigcom.routing.model;

import java.util.LinkedList;

public class Node 
{
	public String name;							//节点的名字也可以是id（node_id）
	public int indexInValidNodes;				//该节点在所选节点集合中的index
//    public int contactDuration;
//    public int contactCount;
    public LinkedList<Message> msgQueue = null;		//每个Node有一个msg队列，这里只用addLast和removeFirst方法
//  public TreeSet<Integer> communities;
//	public TreeMap<Integer,int[]> communityContactDurationMap;
    
	public Node(String nodeName)
	{
    	name = nodeName;
    	indexInValidNodes = -1;//初始化时会重新设置index，-1只是暂时的
//    	contactDuration = 0;
    	msgQueue = new LinkedList<Message>();
//    	communities = new TreeSet<Integer>();
//    	communityContactDurationMap = new TreeMap<Integer,int[]>();
    }
	
	public static Node findNodeByName(String name, Node[] nodes)
	{
		for(int i = 0; i<nodes.length; i++)
		{
			if(nodes[i].name.equals(name))
			{
				return nodes[i];
			}
		}
		
		return null;
	}
	
	public void fowardMessageToAnotherNode(Node otherNode, int msgNum)
	{
		for(int i = 0; i<msgNum; i++)
		{
			Message msg = this.msgQueue.removeFirst();
			otherNode.msgQueue.addLast(msg);
		}
	}
	
	public void copyMessageToAnotherNode(Node otherNode, int msgNum)
	{
		for(int i = 0; i<msgNum; i++)
		{
			Message msg = this.msgQueue.get(i);
			msg.TTL = Message.MAXTTL;
			otherNode.msgQueue.addLast(msg);
		}
	}

}
