package sigcom.routing.model;

public class Message 
{
	public static final int MAXTTL = 15;
	public Node srcNode = null;
	public Node desNode = null;
	public int TTL;		//设置一个Message的生存时间，如果TTL变为0就把它丢弃
	
	public int flag = 0;
	
	public Message(Node src, Node des)
	{
		this.srcNode = src;
		this.desNode = des;
		TTL = 5;
	}
	
}