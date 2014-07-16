package sigcom.routing.model;

import java.util.LinkedList;

public class Node 
{
	public String name;							//�ڵ������Ҳ������id��node_id��
	public int indexInValidNodes;				//�ýڵ�����ѡ�ڵ㼯���е�index
//    public int contactDuration;
//    public int contactCount;
    public LinkedList<Message> msgQueue = null;		//ÿ��Node��һ��msg���У�����ֻ��addLast��removeFirst����
//  public TreeSet<Integer> communities;
//	public TreeMap<Integer,int[]> communityContactDurationMap;
    
	public Node(String nodeName)
	{
    	name = nodeName;
    	indexInValidNodes = -1;//��ʼ��ʱ����������index��-1ֻ����ʱ��
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
