package sigcom.test;

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
		File f = new File("Stage_2/friendshipDM.arff");
		Vector<Hang> list = new Vector<Hang>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String temp;
			br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();
			br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();br.readLine();
			while((temp=br.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(temp,",");
				Hang hang = new Hang();
				hang.n1 = st.nextToken();hang.n2 = st.nextToken();hang.n3 = st.nextToken();
				hang.n4 = st.nextToken();hang.n5 = st.nextToken();hang.n6 = st.nextToken();
				hang.n7 = st.nextToken();hang.n8 = st.nextToken();hang.n9 = st.nextToken();
				hang.n10 = st.nextToken();hang.n11 = st.nextToken();hang.n12 = st.nextToken();
				hang.n13 = st.nextToken();
				list.add(hang);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Collections.sort(list,comp);
		
		try{
			File f2 = new File("Stage_2/friendshipDM_sorted.arff");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f2));
			for(int i= 0; i<list.size(); i++)
			{
				Hang hang = list.get(i);
	
				String str = hang.n1 + ","+hang.n2 + ","+hang.n3 + ","+hang.n4 + ","+hang.n5 + ","+
						hang.n6 + ","+hang.n7 + ","+hang.n8 + ","+hang.n9 + ","+hang.n10 + ","+
						hang.n11 + ","+hang.n12 + ","+hang.n13;
				bw.write(str+"\n");
			}
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println("wirte success!");
		

	}


	static Comparator<Hang> comp=new Comparator<Hang>(){
		public int compare(Hang hang1,Hang hang2){
			if(Integer.parseInt(hang1.n5) <Integer.parseInt(hang2.n5))
				return -1;
			else if(Integer.parseInt(hang1.n5) >Integer.parseInt(hang2.n5))
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
