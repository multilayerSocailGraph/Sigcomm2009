package simulation_1.sigcom.routing.model;

class Side
{
	public String preNode; // 前向节点
	public String node;// 后向节点
	public int weight;// 权重

	public Side(String preNode, String node, int weight)
	{
		this.preNode = preNode;
		this.node = node;
		this.weight = weight;
	}
}
