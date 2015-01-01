package nkolkoikrzyzyk;

import java.util.Vector;

public class neurons {
	
	private double a;
	private double[] hiddenNeurons;
	public neurons(double d) {
		a = d;
		hiddenNeurons = new double[9];
		for(int i=0;i<9;i++)
		{
			hiddenNeurons[i]=0.5;
		}
	}

	public int[] outputSignal(int [] signals)
	{
		Vector<Integer> v = new Vector<Integer>();
		for(int i=0;i<9;i++)
		{
			if(signals[i]==0)
			{
				v.addElement(i);
				//System.out.println(i);
			}
		}
		int max =0;
		//System.out.println("Size"+v.size());
		for(int i=1;i<v.size();i++)
		{
			//System.out.println("i="+i+" vget "+v.get(i));
			if(hiddenNeurons[v.get(max)]<hiddenNeurons[v.get(i)])
			{
				max = i;
			}
		}
		signals[v.get(max)]=-1;
		
		return signals;
	};
	
	public void youLost()
	{
		
	};
}
