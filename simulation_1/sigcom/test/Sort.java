package simulation_1.sigcom.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.Vector;

public class Sort
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		File f = new File("Stage_1/proximities.arff");
		Vector<Proximity> list = new Vector<Proximity>();
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(f));
			String temp;
			br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();
			while((temp=br.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(temp,",");
				Proximity proxi = new Proximity();
				proxi.n1 = st.nextToken();proxi.n2 = st.nextToken();proxi.n3 = st.nextToken();
//				hang.n4 = st.nextToken();hang.n5 = st.nextToken();hang.n6 = st.nextToken();
//				hang.n7 = st.nextToken();hang.n8 = st.nextToken();hang.n9 = st.nextToken();
//				hang.n10 = st.nextToken();hang.n11 = st.nextToken();hang.n12 = st.nextToken();
//				hang.n13 = st.nextToken();
				list.add(proxi);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Collections.sort(list,comp);
		
		try{
			File f2 = new File("Stage_2/proximities_sorted.arff");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f2));
			for(int i= 0; i<list.size(); i++)
			{
				Proximity proxi = list.get(i);
	
//				String str = hang.n1 + ","+hang.n2 + ","+hang.n3 + ","+hang.n4 + ","+hang.n5 + ","+
//						hang.n6 + ","+hang.n7 + ","+hang.n8 + ","+hang.n9 + ","+hang.n10 + ","+
//						hang.n11 + ","+hang.n12 + ","+hang.n13;
				String str = proxi.n1 + ","+proxi.n2 + ","+proxi.n3;
				bw.write(str+"\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("wirte success!");
		

	}


	static Comparator<Proximity> comp=new Comparator<Proximity>(){
		public int compare(Proximity hang1,Proximity hang2){
			if(Integer.parseInt(hang1.n1) <Integer.parseInt(hang2.n1))
				return -1;
			else if(Integer.parseInt(hang1.n1) >Integer.parseInt(hang2.n1))
				return 1;
			else
				return 0;
		}
	};
	

}

class Hang
{
	public String n1;
	public String n2;
	public String n3;
	public String n4;
	public String n5;
	public String n6;
	public String n7;
	public String n8;
	public String n9;
	public String n10;
	public String n11;
	public String n12;
	public String n13;
}

class Proximity
{
	String n1;
	String n2;
	String n3;
}
