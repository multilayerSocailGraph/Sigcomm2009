package sigcom.routing.control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import sigcom.routing.model.Node;


class BeforeRouting 
{
	int train_Start_Time = 0;	//训练的开始时间(sortedContact.dat文件中节点开始发送数据的时间)
	int train_End_Time = 80000;//训练的结束时间<--------->同时也是开始测试的时间
	
	static int[] test_TimeArray = {80000,83600,87200,90800,94400,98000,101600,105200,108800,112400,116000,119600,123200,126800,130400,134000,137600,141200,144800,148400,152000,155600,159200,162800,166400,170000,173600,177200,180800,184400,188000,191600,195200,198800,202400,206000,209600,213200,216800,220400,224000,227600,231200,234800,238400,242000,245600,249200};
	//static int test_End_Time = 340808;//测试结束时间(sortedContact.dat文件中节点最后一次发送数据的时间)
	
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
		
		System.out.println("start create contactCountMatrix...");
		createContactCountMatrix();
		System.out.println("create contactCountMatrix complete!\n");
		
		System.out.println("start create socialGraphMatrix...");
		createSocialGraphMatrix();
		System.out.println("create socialGraphMatrix complete!\n");
		
		System.out.println("start create betweenessArray...");
		createBetweenessArray();
		System.out.println("create betweenessArray complete!\n");
		
		System.out.println("start create similarityMatrix...");
		createSimilarityMatrix();
		System.out.println("create similarityMatrix complete!\n");
		
		System.out.println("start create transmissionCountMatrix...");
		createtransmissionCountMatrix();
		System.out.println("create transmissionCountMatrix complete!\n");
		
		System.out.println("start create commonInterestsMatrix...");
		createtcommonInterestsMatrix();
		System.out.println("create commonInterestsMatrix complete!\n");
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
			tempTokenizer.nextToken();
			int friendShip = Integer.parseInt(tempTokenizer.nextToken());
			
			if(srcNode.indexInValidNodes >= 0 && desNode.indexInValidNodes >= 0)
			{
				Main.friendShipMatrix[srcNode.indexInValidNodes][desNode.indexInValidNodes] = friendShip;
				Main.friendShipMatrix[desNode.indexInValidNodes][srcNode.indexInValidNodes] = friendShip;
				if(friendShip > 1)
					System.out.println(friendShip);
			}
		}
		scan.close();
	}
	
	private void createtcommonInterestsMatrix()
	{
		Main.commonInterestsMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		
		File f = new File("stage_2/interests_sorted.arff");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		HashMap<String,Vector<String>> hash = new HashMap<String,Vector<String>>();
		while (scan.hasNextLine())
		{
			String temp = scan.nextLine();
			
			StringTokenizer tempTokenizer = new StringTokenizer(temp,",");
			Node node = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			String groupId = tempTokenizer.nextToken();
			if(hash.get(groupId) == null)
			{
				Vector<String> v = new Vector<String>();
				v.add(String.valueOf(node.indexInValidNodes));
				hash.put(groupId, v);
			}
			else
			{
				Vector<String> v = hash.get(groupId);
				v.add(String.valueOf(node.indexInValidNodes));
				hash.remove(groupId);
				hash.put(groupId, v);
			}
		}
		scan.close();
		
		Iterator<String> iterator = hash.keySet().iterator();
		while (iterator.hasNext())
		{
			Vector<String> v = hash.get(iterator.next());
			for(int i = 0; i<v.size(); i++)
			{
				String str1 = v.get(i);
				for(int j = 0; j<v.size(); j++)
				{
					if(i == j)
						continue;
					String str2 = v.get(j);
					Main.commonInterestsMatrix[Integer.parseInt(str1)][Integer.parseInt(str2)]++;
				}
			}
		}
		
	}
	
	private void createCommonFriendsMatrix()
	{
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
	
	private void createtransmissionCountMatrix()
	{
		File f = new File("stage_1/transmissions.arff");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		//获得contactDuration矩阵
		Main.transmissionCountMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();
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
			
			if(srcNode.indexInValidNodes >= 0 && desNode.indexInValidNodes >= 0)
			{
				
				Main.transmissionCountMatrix[srcNode.indexInValidNodes][desNode.indexInValidNodes] ++;
				Main.transmissionCountMatrix[desNode.indexInValidNodes][srcNode.indexInValidNodes] ++;
			}
		}
		scan.close();
	}

	private void createSocialGraphMatrix()
	{
		Main.socialGraphMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		for(int i = 0; i < Main.validNodes.length; i++)
		{
			for(int j = i; j < Main.validNodes.length; j++) 
			{
				//if(Main.friendShipMatrix[i][j] == 1 || Main.commonFriends[i][j] >= 1)
				if(Main.contactCountMatrix[i][j] >= 1)
					Main.socialGraphMatrix[i][j] = Main.socialGraphMatrix[j][i] = 1;
			}
		}
	}

	private void createContactCountMatrix()
	{
		File f = new File("Stage_1/proximities.arff");
		Scanner scan = null;
		try {
			scan = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Main.contactCountMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		scan.nextLine();scan.nextLine();scan.nextLine();scan.nextLine();
		scan.nextLine();scan.nextLine();scan.nextLine();
		while (scan.hasNextLine()) 
		{
			String temp = scan.nextLine();
			StringTokenizer tempTokenizer = new StringTokenizer(temp,",");
			int startTime = Integer.parseInt(tempTokenizer.nextToken());
			Node srcNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			Node desNode = Node.findNodeByName(tempTokenizer.nextToken(), Main.validNodes);
			if(srcNode == null || desNode == null)
				continue;
			
			if(srcNode.indexInValidNodes >= 0 && desNode.indexInValidNodes >= 0  && startTime >= train_Start_Time && startTime < train_End_Time)
			{
				Main.contactCountMatrix[srcNode.indexInValidNodes][desNode.indexInValidNodes] += 1;
				Main.contactCountMatrix[desNode.indexInValidNodes][srcNode.indexInValidNodes] += 1;
			}
		}
		scan.close();
	}
	
	private void createBetweenessArray()
	{
		int nodeNum = Main.validNodes.length;
		Main.betweeness = new double[nodeNum];
		for(int i = 0; i < nodeNum; i++) 
		{
			Main.betweeness[i] = 0;
		}
		for(int s = 0; s < nodeNum; s++) 
		{
			Vector<ArrayList<Integer>> p = new Vector<ArrayList<Integer>>();
			for(int i = 0; i < nodeNum; i++) 
			{
				p.add(new ArrayList<Integer>());
			}
			Stack<Integer> S = new Stack<Integer>();
			Queue<Integer> Q = new LinkedList<Integer>();
			double[] delta = new double[nodeNum];
	   	  	for(int h = 0; h < nodeNum; h++) 
	   	  	{
	   	  		delta[h] = 0.0;
	   	  	}
	   	  	delta[s] = 1.0;
	   	  	int[] d = new int[nodeNum];
	   	  	for(int e = 0 ; e < nodeNum; e++) {
	   	  		d[e] = -1;
	   	  	}
	   	  	d[s] = 0;
	   	  	Q.add(s);
	   	  	while(!Q.isEmpty())
	   	  	{
	   	  		int v = Q.remove();
	   	  		S.push(v);
	   	  		for(int w = 0; w < nodeNum; w++) 
	   	  		{
	   	  			if(Main.socialGraphMatrix[v][w]!=0 && w != v) 
	   	  			{
	   	  				if(d[w] < 0) 
	   	  				{
	   	  					Q.add(w);
	   	  					d[w] = d[v] +1;
	   	  				}
	   	  				//shortest path to w via v
	   	  				if(d[w] == d[v] +1 && w != v)
	   	  				{
	   	  					delta[w] = delta[w] +delta[v];
	   	  					p.elementAt(w).add(v);
	   	  				}
	   	  			}
	   	  		}
	   	  	}
	   	  	
	   	  	double[] sum = new double[nodeNum];
	   	  	int v;
	   	  	for(v = 0; v < nodeNum; v++) 
	   	  	{
	   	  		sum[v]=0;
	   	  	}
	   	  	
	   	  	while(!S.empty()) 
	   	  	{
	   	  		int w = S.pop();
	   	  		Iterator<Integer> ix = p.elementAt(w).iterator();
	   	  		while(ix.hasNext()) 
	   	  		{
	   	  			int tmp = (Integer) ix.next();
	   	  			sum[tmp] = sum[tmp] + (delta[tmp] / delta[w]) * (1.0 + sum[w]);
	   	  		}
	   	  		if(w != s)
	   	  		Main.betweeness[w] = Main.betweeness[w] + sum[w] / 2.0;
	   	  	}
		}
		
		
	}

	private void createSimilarityMatrix()
	{
		Main.similarityMatrix = new int[Main.validNodes.length][Main.validNodes.length];
		int temp;
		for(int i = 0; i < Main.validNodes.length; i++)
		{
			for(int j = Main.validNodes.length - 1; j >= i; j--) 
			{
				temp = 0;
				for(int k = 0; k < Main.validNodes.length; k++) 
				{
					if(Main.socialGraphMatrix[i][k] == 1 && Main.socialGraphMatrix[j][k] == 1)
						temp++;
				}
				Main.similarityMatrix[i][j] = temp;
				Main.similarityMatrix[j][i] = temp;
			}
		}
	}
	
}
