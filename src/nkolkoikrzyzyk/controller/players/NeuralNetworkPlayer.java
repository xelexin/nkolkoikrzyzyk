package nkolkoikrzyzyk.controller.players;

import java.util.Random;

import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.NeuralNetwork;

public abstract class NeuralNetworkPlayer extends Player 
{
	protected NeuralNetwork network;
	protected Random random = new Random(System.currentTimeMillis());
		
	public NeuralNetworkPlayer(String name, Mark markType, NeuralNetwork network) 
	{
		super(name, markType);
		this.network = network;
	}

//	@Override
//	public int[] makeMove( int[] board, int position ) {
//		int bestMove = -1;
//		
//		if (network != null) {
//			float[] input = GameModel.toFloat(board);
//			// TODO: conversion from Xs to Os will be probably needed
//			
//			float[] output = network.run(input);
//			System.out.println("Neural network output = " + Arrays.toString(output));
//			
//			float bestProbability = Float.NEGATIVE_INFINITY;
//			for (int i = 0; i < 9; i++)
//				if (output[i] > bestProbability && board[i] == 0) {
//					bestMove = i;
//					bestProbability = output[i];
//				}		
//		} else
//			System.out.println("network == null, doing random move...");
//		
//		if (bestMove == -1)
//			do {
//				bestMove = random.nextInt(9);
//			} while (board[bestMove] == 0);
//
//		System.out.println("Neural network player selected " + bestMove + " as a best move");
//		board[bestMove] = markType.value();
//		return board;
//	}
}
