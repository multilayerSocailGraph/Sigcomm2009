package sigcom.test;

import java.util.Scanner;
import java.util.StringTokenizer;

public class CalcAverageDeliveryRatio 
{
	private static Scanner scan;

	public static void main(String[] args) 
	{
		System.out.print("Please input :");
		System.out.flush();
		scan = new Scanner(System.in);
		String str = scan.nextLine();
		StringTokenizer st = new StringTokenizer(str,",");
		double total = 0;
		
		st.nextToken();st.nextToken();st.nextToken();st.nextToken();st.nextToken();
		for(int i = 0; i<37; i++)
		{
			String s = st.nextToken();
			double d = Double.parseDouble(s);
			total += d;
		}
		st.nextToken();st.nextToken();st.nextToken();st.nextToken();st.nextToken();
		
		double average = total/37.0;
		System.out.println("Average Delivery Ratio: "+average);
	}
}
