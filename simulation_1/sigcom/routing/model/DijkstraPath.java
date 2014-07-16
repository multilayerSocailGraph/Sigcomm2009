package sigcom.routing.model;

import java.util.ArrayList;


/*
 * 解释说明：
 * 
 */

public class DijkstraPath
{
	public ArrayList<Side> map;
	public ArrayList<String> redAgg;
	public ArrayList<String> blueAgg;
	public Side[] parents;
	public String sourceNode;//源节点
	public String destinationNode;//目的节点
	public ArrayList<String> addedNodeList;//记录已经添加的结点
	public ArrayList<String> shortestPath;//源结点到目的结点的最短路径
	public int shortestPathWeight;//源结点到目的结点的最短路径的权值

	public DijkstraPath(String sourceNode,String destinationNode)
	{
		this.map = new ArrayList<Side>();
		this.redAgg = null;
		this.blueAgg = null;
		this.parents = null;
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.addedNodeList = new ArrayList<String>();
		this.addedNodeList.add(sourceNode);
		this.shortestPath = null;
		this.shortestPathWeight = -1;
	}

	public String getParent(Side[] parents, String node)
	{
		if (parents != null)
		{
			for (Side nd : parents)
			{
				if (nd.node.equals(node))
				{
					return nd.preNode;
				}
			}
		}
		return "None";
	}

	public boolean addSide(String nodeI,String nodeJ,int weight)
	{
    	String preNode;
    	String node;
    	if(nodeI.equals(nodeJ))
    	{
    		return false;
    	}
    	//设定根据nodeI和nodeJ在addedNodeList中的存在与否，设定preNode和node
    	else{
        	int nodeIIndex = this.addedNodeList.indexOf(nodeI);
        	int nodeJIndex = this.addedNodeList.indexOf(nodeJ);
        	//如果nodeI或nodeJ是sourceNode,则设定其为preNode
        	if(nodeIIndex == 0){
        		preNode = nodeI;
        		node = nodeJ;
        	}
        	else if(nodeJIndex == 0){
        		preNode = nodeJ;
        		node = nodeI;
        	}
        	//如果nodeI或nodeJ都不是sourceNode
        	else{
        		//如果nodeI和nodeJ都存在于addedNodeList中，则次序不变
        	    if(nodeIIndex > 0 && nodeJIndex > 0){
        			preNode = nodeI;
        			node = nodeJ;
        	    }
        	    //如果只有nodeI存在于addedNodeList中，则设其为preNode
        		else if(nodeIIndex > 0 && nodeJIndex == -1){
        			preNode = nodeI;
        			node = nodeJ;
        		}
        	    //如果只有nodeJ存在于addedNodeList中，则设其为preNode
        		else if(nodeIIndex == -1 && nodeJIndex > 0) {
        			preNode = nodeJ;
        			node = nodeI;
        		}
        	    //如果nodeI和nodeJ都不存在于addedNodeList中，则不添加此边
        		else{
        			return false;
        		}
        	}
 	    }
    	//如果node不存在于addedNodeList中，则添加
    	if(!this.addedNodeList.contains(node))
    		this.addedNodeList.add(node);
    	this.map.add(new Side(preNode,node,weight));
       	return true;
	}

	/**
	 * 重新设置蓝点集中剩余节点的最短路径长度
	 */
	public void setWeight(String preNode)
	{
		if (this.map != null && this.parents != null && this.blueAgg != null)
		{
			for (String node : this.blueAgg)
			{
				MinShortPath msp= getMinPath(node);
				int w1 = msp.getWeight();
				if (w1 == -1)
					continue;
				for (Side n : this.parents)
				{
					if (n.node == node)
					{
						if (n.weight == -1 || n.weight > w1)
						{
							n.weight = w1;
							n.preNode = (preNode);//重新设置顶点的父顶点
							break;
						}
					}
				}
			}
		}
	}
	/**
	 * 得到两点节点之间的权重
	 *
	 * @param map
	 * @param preNode
	 * @param node
	 * @return
	 */
	public int getWeight(String preNode, String node)
	{
		if (this.map != null){
			for (Side s : this.map){
				if (s.preNode.equals(preNode) && s.node.equals(node))
					return s.weight;
			}
		}
		return -1;
	}
	/**
	 * 从蓝点集合中找出路径最小的那个节点
	 *
	 * @param map
	 * @param blueAgg
	 * @return
	 */
	public MinShortPath getMinSideNode(){
		MinShortPath minMsp = null;
		if (this.blueAgg.size() > 0){
			int index = 0;
			for (int j = 0; j < this.blueAgg.size(); j++){
				MinShortPath msp = getMinPath(this.blueAgg.get(j));
				if (minMsp == null || msp.getWeight()!=-1 && msp.getWeight() < minMsp.getWeight()){
					minMsp = msp;
					index = j;
				}
			}
			this.blueAgg.remove(index);
		}
		return minMsp;
	}
	/**
	 * 得到某一节点的最短路径(实际上可能有多条,现在只考虑一条)
	 *
	 * @param node
	 * @return
	 */
	public MinShortPath getMinPath(String node)
	{
		MinShortPath msp = new MinShortPath(node);
		if (this.parents != null && this.redAgg != null){
			for (int i = 0; i < this.redAgg.size(); i++){
				MinShortPath tempMsp = new MinShortPath(node);
				String parent = this.redAgg.get(i);
				String curNode = node;
				while (!parent.equals("None")){
					int weight = getWeight(parent, curNode);
					if (weight > -1){
						tempMsp.addNode(parent);
						tempMsp.addWeight(weight);
						curNode = parent;
						parent = getParent(this.parents, parent);
					} else
						break;
				}
				if (msp.getWeight() == -1 || tempMsp.getWeight()!=-1 && msp.getWeight() > tempMsp.getWeight())
					msp = tempMsp;
			}
		}
		return msp;
	}
	/**
	 * 得到从源点到目的节点的最短路径
	 *
	 * @param nodes
	 * @return
	 */
	public boolean findShortestPath()
	{
		//初始化已知最短路径的顶点集，即红点集，只加入顶点0
		this.redAgg = new ArrayList<String>();
		this.redAgg.add(this.sourceNode);
		//初始化未知最短路径的顶点集,即蓝点集
		this.blueAgg = new ArrayList<String>();
		for (int i = 1; i < this.addedNodeList.size(); i++)
			this.blueAgg.add(this.addedNodeList.get(i));
		//初始化每个顶点在最短路径中的父结点,及它们之间的权重,权重-1表示无连通
		this.parents = new Side[this.addedNodeList.size()];
		this.parents[0] = new Side("None", this.sourceNode, 0);
		for (int i = 0; i < this.blueAgg.size(); i++){
			String n = this.blueAgg.get(i);
			this.parents[i + 1] = new Side(this.sourceNode, n, this.getWeight(this.sourceNode, n));
		}
		//找从蓝点集中找出权重最小的那个顶点,并把它加入到红点集中
		while (this.blueAgg.size() > 0)
		{
			MinShortPath msp = this.getMinSideNode();
//			System.out.println(msp.getNodeList().toString());//
			String lastNode = msp.getLastNode();
			if(lastNode.equals(this.destinationNode))
			{
				this.shortestPath = msp.getNodeList();
				this.shortestPathWeight = msp.getWeight();
				return true;
			}
			this.redAgg.add(lastNode);
			// 如果因为加入了新的顶点,而导致蓝点集中的顶点的最短路径减小,则要重要设置
			this.setWeight(lastNode);
		}
		return false;
	}

}


class MinShortPath
{
	public ArrayList<String> nodeList;// 最短路径集
	public int weight;// 最短路径
	
	public MinShortPath(String node)
	{
		nodeList = new ArrayList<String>();
		nodeList.add(node);
		weight = -1;
	}
	public ArrayList<String> getNodeList(){
		return nodeList;
	}
	public void setNodeList(ArrayList<String> nodeList){
		this.nodeList = nodeList;
	}
	public void addNode(String node){
		if (nodeList == null)
			nodeList = new ArrayList<String>();
		nodeList.add(0, node);
	}
	public String getLastNode(){
		int size = nodeList.size();
		return nodeList.get(size - 1);
	}
	public int getWeight(){
		return weight;
	}
	public void setWeight(int weight){
		this.weight = weight;
	}
	public ArrayList<String> outputPath(int weight,String srcNode){
		if(weight == -1)
			nodeList.add(srcNode);
		System.out.println(nodeList.toString()+":"+weight);
		return nodeList;
	}
	public void addWeight(int w){
		if (weight == -1)
			weight = w;
		else
			weight += w;
	}
}