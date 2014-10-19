package sigcom.routing.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import sigcom.routing.model.Contact;
import sigcom.routing.model.Couple;
import sigcom.routing.model.DijkstraPath;
import sigcom.routing.model.Message;
import sigcom.routing.model.Node;

public class EpidemicCountTest 
{
	public int loopFlag = -1;
	public static int[] nodeMsgCount = new int[76];
	
	ArrayList<Couple> coupleList = new ArrayList<Couple>();
	ArrayList<Contact> contactList = new ArrayList<Contact>();

	public EpidemicCountTest(int test_startTime, int test_endTime, int flag)//初始化messageList和contactList
	{
		this.loopFlag = flag;
		
		for(int i = 0; i<nodeMsgCount.length; i++)
		{
			nodeMsgCount[i] = 0;
		}
		
		Node srcNode = Main.validNodes[flag];
		Node desNode = new Node("-1");
		Couple couple = new Couple(srcNode, desNode);
		coupleList.add(couple);
		
		File f = new File("Stage_2/proximities_sorted.arff");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while (scan.hasNextLine()) 
		{
			String temp = scan.nextLine();
			StringTokenizer tempTokenizer = new StringTokenizer(temp,",");
			
			int startTime = Integer.parseInt(tempTokenizer.nextToken());
			String firstNodeName = tempTokenizer.nextToken();
			Node firstNode = Node.findNodeByName(firstNodeName, Main.validNodes);
			String secondNodeName = tempTokenizer.nextToken();
			Node secondNode = Node.findNodeByName(secondNodeName, Main.validNodes);
			
			if(firstNode != null && secondNode != null &&
					startTime >= test_startTime && startTime <= test_endTime)
			{
				Contact contact = new Contact(firstNode, secondNode, startTime, startTime+10, 10);
				contactList.add(contact);
			}
		}
		scan.close();
	}
	
	public void execute()
	{
		for(int i = 0; i < coupleList.size(); i++)//开始使用SimBet算法发送每一个消息
		{
			Couple couple = coupleList.get(i);
			Node srcNode = couple.srcNode;
			Node desNode = couple.desNode;
			for(int w = 0; w<Main.validNodes.length; w++)
				Main.validNodes[w].msgQueue.clear();
			Message msg = new Message(srcNode, desNode);
			srcNode.msgQueue.addLast(msg);						//设定srcNode有一个message
			DijkstraPath path = new DijkstraPath(srcNode.name, desNode.name);
			
			for(int j = 0; j < contactList.size(); j++)
			{
				Node firstNodeInContact = contactList.get(j).firstNode;
				Node secondNodeInContact = contactList.get(j).secondNode;
				if(couple.startFowardTime == -1 && (firstNodeInContact.name.equals(srcNode.name) || secondNodeInContact.name.equals(srcNode.name)))
				{
					couple.startFowardTime = contactList.get(j).startTime;
				}
				
				Node preNode = null;	//目前持有消息的节点，即要转发消息的节点
				Node node = null;		//preNode要转发给的下一跳节点
				
				if(firstNodeInContact.msgQueue.size() > 0 && secondNodeInContact.msgQueue.size() == 0)
				{
					preNode = firstNodeInContact;
					node = secondNodeInContact;
				}
				else if(secondNodeInContact.msgQueue.size() > 0 && firstNodeInContact.msgQueue.size() == 0)
				{
					preNode = secondNodeInContact;
					node = firstNodeInContact;
				}
				else if(secondNodeInContact.msgQueue.size() > 0 && firstNodeInContact.msgQueue.size() > 0)
				{
					EpidemicCountTest.nodeMsgCount[firstNodeInContact.indexInValidNodes]++;
					EpidemicCountTest.nodeMsgCount[secondNodeInContact.indexInValidNodes]++;
//					System.out.println(contactList.get(j).startTime+","+contactList.get(j).firstNode.name+","+contactList.get(j).secondNode.name);
//					System.out.println("***给"+firstNodeInContact.name+"节点计数器加一");
//					System.out.println("***给"+secondNodeInContact.name+"节点计数器加一");
					continue;
				}
				if(preNode!=null && node!=null && node.msgQueue.size()==0)
				{
//					System.out.println(contactList.get(j).startTime+","+contactList.get(j).firstNode.name+","+contactList.get(j).secondNode.name);
//					System.out.println("给"+node.name+"节点计数器加一");
					if(node.name.equals(desNode.name))
					{//如果转发到了目的节点
						preNode.msgQueue.getFirst().TTL--;
						preNode.copyMessageToAnotherNode(node, 1);	//转发消息
						path.addSide(preNode.name, node.name, 1);
						couple.endFowardTime = contactList.get(j).endTime;
						couple.fowardTimes++;
						couple.success = true;
						Main.load[desNode.indexInValidNodes]++;
						Main.load[preNode.indexInValidNodes]++;
						break;
					}
					else
					{//还没转发到目的节点
						//preNode.msgQueue.getFirst().TTL--;
//						if(preNode.msgQueue.getFirst().TTL == 0)
//						{
//							preNode.msgQueue.removeFirst();
//							break;
//						}
//						else
						{
							EpidemicCountTest.nodeMsgCount[node.indexInValidNodes]++;
							preNode.copyMessageToAnotherNode(node, 1);
							path.addSide(preNode.name, node.name, 1);
							couple.fowardTimes++;
							Main.load[node.indexInValidNodes]++;
							Main.load[preNode.indexInValidNodes]++;
						}

					}//挑选中继进行转发
				}
			}//遍历contactList
			
			if(couple.success == true)
			{
				path.findShortestPath();
				couple.hops = path.shortestPath.size() - 1;
				if(couple.endFowardTime - couple.startFowardTime > 0)
				{
					couple.delay = couple.endFowardTime - couple.startFowardTime;
				}
			}
			
		}//遍历coupleList，执行每一次转发
	}//execute
	
	public void calcPerformance(int i)
	{	
		for(int w = 0; w<coupleList.size(); w++)
		{
			Couple couple = coupleList.get(w);
			if(couple.success == true)
			{
				Main.successArray[i]++;
				Main.delayArray[i] = Main.delayArray[i] + couple.delay;
				Main.hopArray[i] = Main.hopArray[i] + couple.hops;
			}
			Main.coupleArray[i]++;
			Main.forwardArray[i] = Main.forwardArray[i] + couple.fowardTimes;
		}
		Main.maxLoadInOneTest[i] = getMaxLoad(Main.load);
		Main.averageLoadInOneTest[i] = getAverageLoad(Main.load);
		
		double deliverRatio = (double)Main.successArray[i]/Main.coupleArray[i];
		double averageDelay = (double)Main.delayArray[i]/Main.successArray[i];
		double averageHops = (double)Main.hopArray[i]/Main.successArray[i];
		double averageForwards = (double)Main.forwardArray[i]/Main.coupleArray[i];
		System.out.println("\nEpidemicRouting---第"+(i+1)+"次测试总数:"+Main.coupleArray[i]);
		System.out.println("路由成功数目 : "+Main.successArray[i]);
		System.out.println("成功率:"+deliverRatio);
		System.out.println("平均延时:"+averageDelay);
		System.out.println("平均跳数:"+averageHops);
		System.out.println("平均转发次数:"+averageForwards);
		
	}//showPerf()
	
	public int getMaxLoad(int[] load)
	{
		int max = 0;
		
		for(int i = 0; i<load.length; i++)
		{
			if(load[i]>max)
				max = load[i];
		}
		return max;
	}
	
	public double getAverageLoad(int[] load)
	{
		double sum = 0;
		
		for(int i = 0; i<load.length; i++)
		{
				sum += load[i];
		}
		return sum/load.length;
	}
	
}
