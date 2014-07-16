package simulation_1.sigcom.routing.model;


public class Couple 
{
	public Node srcNode = null;
	public Node desNode = null;
	
    public int startFowardTime = -1;
    public int endFowardTime = -1;
    public int fowardTimes = 0;	//转发次数，不论消息是否成功交付，转发一次，fowardTimes就自增1
    public int hops = 0;//路由跳数，如果消息成功交付，那么跳数等于转发次数；否则跳数为0
    public int delay = 0;//消息延时，消息成功交付时，交付时间减去消息生成时间就是延时
    public boolean success = false;
    public int contactTimes = 0;
    
    public Couple(Node srcNode, Node desNode)
    {
    	this.srcNode = srcNode;
    	this.desNode = desNode;
    }//Couple
    
}
