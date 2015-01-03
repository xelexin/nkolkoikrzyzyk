package nkolkoikrzyzyk;

import java.util.Random;
import java.util.Scanner;

import nkolkoikrzyzyk.model.neurons;



public class nkolkoikrzyzyk {

	public static void main(String[] args) {
		neurons siec = new neurons(0.5, -1);
		int[] plansza = new int[9];
		int k=0;
		int max=100;
		while(true)
		{
			System.out.println("Gra nr "+k);
			for(int i=0;i<9;i++)
				plansza[i]=0;
			for(int j=0;j<9;j++)
			{
				plansza = siec.outputSignal(plansza);
				printBoard(plansza);
				if(ifwin(plansza)==true)
				{
					System.out.println("Komputer wygra³");
					siec.youWin(plansza);
					break;
				}
				
				if(noMoreMove(plansza)==true)
				{
					siec.youLost();
					System.out.println("Remis");
					break;
				}
				
				int input;
				if(k>max)
				{
					Scanner reader = new Scanner(System.in);
					System.out.println("podaj pozycje krzyzyka");
					input = reader.nextInt();
					while(plansza[input]!=0)
					{
						System.out.println("podaj pozycje krzyzyka");
						input = reader.nextInt();
					}
				}
				else
				{
					int liczba;
					Random losowa = new Random();
					liczba = losowa.nextInt(9);
					while(plansza[liczba]!=0)
					{
						liczba=losowa.nextInt(9);
					}
					input = liczba;
				}
				plansza[input]=1;
				if(ifwin(plansza)==true)
				{
					System.out.println("Gracz wygra³");
					siec.youLost();
					break;
				}
				
				
			}
			k++;
		}
	}
	
	private static void printBoard(int[] plansza) {
		for(int i=0;i<9;i++)
		{

			if(plansza[i]<0)
				System.out.print("O");
			else if(plansza[i]>0)
				System.out.print("X");
			else
				System.out.print(".");
			
			if (i%3==2)
				System.out.println();
		}
	}
	
	private static boolean noMoreMove(int[] plansza) {
		for(int i=0;i<9;i++)
		{
			if(plansza[i]==0)
				return false;
		}
		return true;
	}

	private static boolean ifwin(int[] plansza) {
		boolean win=false;
		//test poziomy
		for(int i=0;i<3;i++)
		{
			if(plansza[i*3]==plansza[i*3+1] && plansza[i*3+1]==plansza[i*3+2] && (plansza[i*3]==1 || plansza[i*3]==-1))
			{
				win=true;
			}
		}
		//test pionowy
		for(int i=0;i<3;i++)
		{
			if(plansza[i]==plansza[(i+6)] && plansza[(i+6)]==plansza[(i+3)] && (plansza[i]==1 || plansza[i]==-1))
			{
				win=true;
			}
		}
		//test z lewej na prawa
		if(plansza[0]==plansza[4] && plansza[8]==plansza[4] && (plansza[0]==1 || plansza[0]==-1))
		{
			win=true;
		}
		//test z prawej na lewa
		if(plansza[2]==plansza[4] && plansza[6]==plansza[4] && (plansza[4]==1 || plansza[4]==-1))
		{
			win=true;
		}
		return win;
	}

}
