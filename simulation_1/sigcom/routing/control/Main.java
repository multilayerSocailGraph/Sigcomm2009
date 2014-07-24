package sigcom.routing.control;

import java.text.DecimalFormat;
import sigcom.routing.model.Node;
  
public class Main
{
	public static Node[] validNodes;			//被选节点
	public static int[][] friendShipMatrix;
	public static int[][] commonFriends;    	//两个节点共同朋友的个数
	public static int[][] socialGraphMatrix;
	public static double[] betweeness;
	public static int[][] similarityMatrix;
	public static int[][] contactCountMatrix;
	public static int[][] transmissionCountMatrix;
	public static int[][] commonInterestsMatrix;
	
	public static int testTimes = BeforeRouting.test_TimeArray.length-1;//进行的测试次数
	public static int[] coupleArray = new int[testTimes];//某次测试的样例数，样例事先在程序中已设定好，一般在每个路由算法的构造函数中设定
	public static int[] successArray = new int[testTimes];//某次测试最终交付成功样例的数目
	public static int[] delayArray = new int[testTimes];//某次测试中所有成功交付样例的延时的总和
	public static int[] hopArray = new int[testTimes];//某次测试中所有成功交付样例的跳数的总和
	public static int[] forwardArray = new int[testTimes];//某次测试中的所有样例的转发次数的总和
	public static int[] maxLoadInOneTest = new int[testTimes];//某次测试中的所有节点的最大负载
	public static double[] averageLoadInOneTest = new double[testTimes];//某次测试中的所有节点的平均负载
	
	public static int[] load;//每一个节点的负载情况
	
	public static void main(String[] args)
	{
		BeforeRouting beforeRounting = new BeforeRouting();
		beforeRounting.createMatrix();
			
		Main.load = new int[Main.validNodes.length];
		
//		System.out.println("\n------------------FriendShipRouting--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)//FriendShipRouting
//		{
//			FriendShipRouting friendshipRouting = new FriendShipRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			friendshipRouting.execute();
//			friendshipRouting.calcPerformance(i);
//			Main.clearLoad();
//			if(i == 10)
//			{
//				try{
//					File f = new File("Stage_2/friendship_failed.txt");
//					BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//					for(int j = 0; j<friendshipRouting.coupleList.size(); j++)
//					{
//						if(friendshipRouting.coupleList.get(j).success == false)
//						{
//							bw.write(friendshipRouting.coupleList.get(j).srcNode.name+","+friendshipRouting.coupleList.get(j).desNode.name+"\n");
//						}
//					}
//					bw.close();
//					System.out.println("失败信息统计成功！");
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				
//			}//统计测试时间为十小时时的失败的couple对
//			
//		}
//		showPerformance();
		
//		System.out.println("\n------------------FriendShipRoutingV4--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)//FriendShipRouting
//		{
//			FriendShipRoutingV4 friendshipRoutingV4 = new FriendShipRoutingV4(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			friendshipRoutingV4.execute();
//			friendshipRoutingV4.calcPerformance(i);
//			Main.clearLoad();
//		}
//		showPerformance();
		
//		System.out.println("\n------------------EpidemicRouting--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)//FriendShipRouting
//		{
//			EpidemicRouting epidemic = new EpidemicRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			epidemic.execute();
//			epidemic.calcPerformance(i);
//			Main.clearLoad();
//		}
//		showPerformance();
		
//		System.out.println("\n------------------TransmissionRouting--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)
//		{
//			TransmissionRouting transRouting = new TransmissionRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			transRouting.execute();
//			transRouting.calcPerformance(i);
//			Main.clearLoad();
//			if(i == 10)
//			{
//				try{
//					File f = new File("Stage_2/transmission_failed.txt");
//					BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//					for(int j = 0; j<transRouting.coupleList.size(); j++)
//					{
//						if(transRouting.coupleList.get(j).success == false)
//						{
//							bw.write(transRouting.coupleList.get(j).srcNode.name+","+transRouting.coupleList.get(j).desNode.name+"\n");
//						}
//					}
//					bw.close();
//					System.out.println("失败信息统计成功！");
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				
//			}//统计测试时间为十小时时的失败的couple对
//		}
//		showPerformance();
		
//		System.out.println("\n------------------InterestRouting--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)
//		{
//			InterestRouting interestRouting = new InterestRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			interestRouting.execute();
//			interestRouting.calcPerformance(i);
//			Main.clearLoad();
//			if(i == 10)
//			{
//				try{
//					File f = new File("Stage_2/interest_failed.txt");
//					BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//					for(int j = 0; j<interestRouting.coupleList.size(); j++)
//					{
//						if(interestRouting.coupleList.get(j).success == false)
//						{
//							bw.write(interestRouting.coupleList.get(j).srcNode.name+","+interestRouting.coupleList.get(j).desNode.name+"\n");
//						}
//					}
//					bw.close();
//					System.out.println("失败信息统计成功！");
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				
//			}//统计测试时间为十小时时的失败的couple对
//		}
//		showPerformance();
		
//		System.out.println("\n------------------Greedy-Total--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)
//		{
//			GreedyTotal greedyTotal = new GreedyTotal(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			greedyTotal.execute();
//			greedyTotal.calcPerformance(i);
//			Main.clearLoad();
//			if(i == 10)
//			{
//				try{
//					File f = new File("Stage_2/proximity_failed.txt");
//					BufferedWriter bw = new BufferedWriter(new FileWriter(f));
//					for(int j = 0; j<greedyTotal.coupleList.size(); j++)
//					{
//						if(greedyTotal.coupleList.get(j).success == false)
//						{
//							bw.write(greedyTotal.coupleList.get(j).srcNode.name+","+greedyTotal.coupleList.get(j).desNode.name+"\n");
//						}
//					}
//					bw.close();
//					System.out.println("失败信息统计成功！");
//				}catch(Exception e){
//					e.printStackTrace();
//				}
//				
//			}//统计测试时间为十小时时的失败的couple对
//		}
//		showPerformance();
		
//		System.out.println("\n------------------LayerByLayerRouting--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)
//		{
//			LayerByLayerRouting lbl = new LayerByLayerRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			lbl.execute();
//			lbl.calcPerformance(i);
//			Main.clearLoad();
//		}
//		showPerformance();
		
//		System.out.println("\n------------------FIPTRouting--------------");
//		clearData();
//		for(int i = 0; i<testTimes; i++)
//		{
//			FIPTRouting fiptRouting = new FIPTRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
//			fiptRouting.execute();
//			fiptRouting.calcPerformance(i);
//			Main.clearLoad();
//		}
//		showPerformance();
		
		System.out.println("\n------------------AvoidFailingRouting--------------");
		clearData();
		for(int i = 0; i<testTimes; i++)
		{
			AvoidFailingRouting avoidFailingRouting = new AvoidFailingRouting(BeforeRouting.test_TimeArray[0], BeforeRouting.test_TimeArray[i+1]);
			avoidFailingRouting.execute();
			avoidFailingRouting.calcPerformance(i);
			Main.clearLoad();
		}
		showPerformance();
	}
	
	public static void showPerformance()
	{
		System.out.print("delivery ratio:");
		for(int i = 0; i<testTimes; i++)
		{
			double deliRatio = (double)successArray[i]/Main.coupleArray[i];
			DecimalFormat df = new DecimalFormat("#.0000");
			System.out.print("0"+df.format(deliRatio)+",");
		}
		System.out.print("\ndelay:");
		for(int i = 0; i<testTimes; i++)
		{
			double delay = (double)delayArray[i]/Main.successArray[i];
			DecimalFormat df = new DecimalFormat("#.000");
			System.out.print(df.format(delay)+",");
		}
		System.out.print("\nhop:");
		for(int i = 0; i<testTimes; i++)
		{
			double hop = (double)hopArray[i]/Main.successArray[i];
			DecimalFormat df = new DecimalFormat("#.000");
			System.out.print(df.format(hop)+",");
		}
		System.out.print("\nforwards:");
		for(int i = 0; i<testTimes; i++)
		{
			double forward = (double)forwardArray[i]/6006;
			DecimalFormat df = new DecimalFormat("#.000");
			if(forward >= 1)
				System.out.print(df.format(forward)+",");
			else
				System.out.print("0"+df.format(forward)+",");
		}
		System.out.print("\nmaxLoad:");
		for(int i = 0; i<testTimes; i++)
		{
			System.out.print(Main.maxLoadInOneTest[i]+",");
		}
		System.out.print("\naverageLoad:");
		for(int i = 0; i<testTimes; i++)
		{
			DecimalFormat df = new DecimalFormat("#.000");
			System.out.print(df.format(Main.averageLoadInOneTest[i])+",");
		}
	}
		
	public static void clearData()
	{
		for(int i = 0; i<testTimes; i++)//BetweennessRouting
		{
			coupleArray[i] = 0;
			successArray[i] = 0;
			delayArray[i] = 0;
			hopArray[i] = 0;
			forwardArray[i] = 0;
			maxLoadInOneTest[i] = 0;
			averageLoadInOneTest[i] = 0;
		}
		for(int i = 0; i<load.length; i++)
		{
			load[i] = 0;
		}
	}
	
	public static void clearLoad()
	{
		for(int i = 0; i<Main.load.length; i++)
		{
			Main.load[i] = 0;
		}
	}
	
}
