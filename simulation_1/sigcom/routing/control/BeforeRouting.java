package sigcom.routing.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import sigcom.routing.model.Node;


class BeforeRouting 
{
	int train_Start_Time = 0;//训练的开始时间(sortedContact.dat文件中节点开始发送数据的时间)
	int train_End_Time = 150000;//训练的结束时间<--------->同时也是开始测试的时间

	static int[] test_TimeArray = {160000,167200,174400,181600,188800,196000,203200,210400,217600,224800,232000,239200,246400,253600,260800,268000,275200,282400,289600,296800};
//	static int test_End_Time = 340808;//测试结束时间(sortedContact.dat文件中节点最后一次发送数据的时间)
	
	public BeforeRouting()
	{
		System.out.println("start scan validNodes...");
		createValidNodes();//ok
		System.out.println("scan validNodes complete!\n");
	}
	
	public void createMatrix()
	{
		System.out.println("start create friendShipMatrix...");
		createFriendShipMatrix();
		System.out.println("create friendShipMatrix complete!\n");
		
		System.out.println("start create CommonFriendsMatrix...");
		createCommonFriendsMatrix();
		System.out.println("create CommonFriendsMatrix complete!\n");
	}
	
	public void createValidNodes()
	{
		ArrayList<String> array = new ArrayList<String>();
		
		File f = new File("Stage_2/ValidNodes.txt");
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(f));
			String line = null;
			while((line=br.readLine())!= null)
			{
				array.add(line);
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		String[] validNodesName = array.toArray(new String[] {});
		
		Main.validNodes = new Node[validNodesName.length];
		for(int i = 0; i<validNodesName.length; i++)
		{
			Node node = new Node(validNodesName[i]);
				
			node.indexInValidNodes = getNodeIndex(validNodesName, node.name);
			Main.validNodes[i] = node;
		}
		
	}
	
	public static int getNodeIndex(String[] nodes, String node) 
	{
		int low = 0;
		int high = nodes.length - 1;
		while(low<=high)
		{
			int mid = (low+high) / 2;
			if(nodes[mid].equals(node))
				return mid;
			else if(Integer.parseInt(nodes[mid]) < Integer.parseInt(node))
				low = mid + 1;
			else
				high = mid - 1;

		}
		return -1;
	}
	
	private void createFriendShipMatrix()
	{
		File f = new File("Stage_2/friendshipDM.arff");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//获得contactDuration矩阵
		Main.friendShipMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();
		while (scan.hasNextLine()) 
		{
			String temp = scan.nextLine();
			
			StringTokenizer tempTokenizer = new StringTokenizer(temp,",");
			tempTokenizer.nextToken();
			tempTokenizer.nextToken();
			Node srcNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			Node desNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			if(srcNode == null || desNode == null)
				continue;
			int startTime = Integer.parseInt(tempTokenizer.nextToken());
			int friendShip = Integer.parseInt(tempTokenizer.nextToken());
			
			if(srcNode.indexInValidNodes >= 0 && desNode.indexInValidNodes >= 0 && startTime >= train_Start_Time && startTime <=train_End_Time)
			{
				Main.friendShipMatrix[srcNode.indexInValidNodes][desNode.indexInValidNodes] = friendShip;
				Main.friendShipMatrix[desNode.indexInValidNodes][srcNode.indexInValidNodes] = friendShip;
				if(friendShip > 1)
					System.out.println(friendShip);
			}
		}
		scan.close();
	}
	
	private void createCommonFriendsMatrix()
	{
		File f = new File("Stage_2/friendshipDM.arff");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//获得contactDuration矩阵
		Main.commonFriends = new int[Main.validNodes.length][Main.validNodes.length];
		
		int[] friendsOfNodeI = new int[Main.commonFriends.length];
		int[] friendsOfNodeJ = new int[Main.commonFriends.length];
		for(int i = 0; i<Main.commonFriends.length; i++)
		{
			for(int j = 0; j<Main.commonFriends.length; j++)
			{
				Main.commonFriends[i][j] = 0;
				for(int q = 0; q<Main.friendShipMatrix.length; q++)
				{
					friendsOfNodeI[q] = 0;
					friendsOfNodeJ[q] = 0;
				}
				for(int k = 0; k<Main.friendShipMatrix.length; k++)
				{
					if(Main.friendShipMatrix[i][k] == 1)
						friendsOfNodeI[k] = 1;
				}
				for(int w = 0; w<Main.friendShipMatrix.length; w++)
				{
					if(Main.friendShipMatrix[j][w] == 1)
						friendsOfNodeJ[w] = 1;
				}
				for(int t = 0; t<Main.friendShipMatrix.length; t++)
				{
					if(friendsOfNodeI[t] == 1 && friendsOfNodeJ[t] == 1)
					{
						Main.commonFriends[i][j]++;
					}
				}
			}
		}

		
	}

	
}
