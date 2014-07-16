package sigcom.routing.model;


public class Contact 
{
	public Node firstNode = null;
	public Node secondNode = null;
	public int startTime;//timestamp
	public int endTime;//timestamp+duration
	public int duration;
	
	public Contact(Node firstNode, Node secondNode, int startTime, int endTime, int duration)
	{
		this.firstNode = firstNode;
		this.secondNode = secondNode;
		this.startTime = startTime;
		this.endTime = endTime;
		this.duration = duration;
	}
}
