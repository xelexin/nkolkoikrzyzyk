package nkolkoikrzyzyk;

import java.util.Scanner;



public class nkolkoikrzyzyk {

	public static void main(String[] args) {
		neurons siec = new neurons(0.5);
		int[] plansza = new int[9];
		
		while(true)
		{
			System.out.println("Kolejna gra");
			for(int i=0;i<9;i++)
				plansza[i]=0;
			for(int j=0;j<9;j++)
			{
				for(int i=0;i<9;i++)
				{
					if(i%3==2)
					{
						System.out.println(plansza[i]);
					}
					else
					{
						System.out.print(plansza[i]);
					}
				}
				Scanner reader = new Scanner(System.in);
				System.out.println("podaj pozycje krzyzka");
				int input = reader.nextInt();
				plansza[input]=1;
				if(ifwin(plansza)==true)
				{
					System.out.println("Gracz wygra³");
					break;
				}
				plansza = siec.outputSignal(plansza);
				if(ifwin(plansza)==true)
				{
					System.out.println("Komputer wygra³");
					break;
				}
			}
			
		}
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
