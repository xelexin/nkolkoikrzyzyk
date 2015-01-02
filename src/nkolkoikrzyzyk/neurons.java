package nkolkoikrzyzyk;

import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import com.sun.xml.internal.ws.api.pipe.Fiber;

public class neurons {
	
	private double a;
	private double[][] hiddenNeurons;
	private int size;
	Stack<Integer> stos;
	public neurons(double d) {
		a = d;
		hiddenNeurons = new double[5][9];
		size=0;
		stos  = new Stack<>();
		for(int j=0;j<5;j++)
		{
			hiddenNeurons[j]=new double[9];
			for(int i=0;i<9;i++)
			{
				hiddenNeurons[j][i]=0.5;
			}
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
			if(hiddenNeurons[size][v.get(max)]<hiddenNeurons[size][v.get(i)])
			{
				max = i;
			}
		}
		Vector<Integer>najwieksze = new Vector<>();
		for(int i=0;i<v.size();i++)
		{
			if(hiddenNeurons[size][v.get(max)]==hiddenNeurons[size][v.get(i)])
			{
				najwieksze.add(v.get(i));
			}
		}
		Random generator = new Random();
		System.out.println("wywala tutaj najwieksze.size="+najwieksze.size());
		int randomNumber = generator.nextInt(najwieksze.size());
		signals[najwieksze.get(randomNumber)]=-1;
		stos.push(najwieksze.get(randomNumber));
		size++;
		return signals;
	}
	
	public void youWin(int []plansza)
	{
		int previous;
		previous = stos.pop();
		size--;
		hiddenNeurons[size][previous]=1;
		while(!stos.empty())
		{
			size--;
			int temp = stos.pop();
			hiddenNeurons[size][temp]=hiddenNeurons[size][temp]+a*(hiddenNeurons[size+1][previous]-hiddenNeurons[size][temp]);
			System.out.println("Nowa wartosc neuronu ("+size+","+temp+") to "+hiddenNeurons[size][temp]);
			previous=temp;
		}
		size=0;
	}
	public void youLost()
	{
		while(!stos.empty())
			stos.pop();
		size=0;
	}
}
