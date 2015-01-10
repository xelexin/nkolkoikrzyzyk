package nkolkoikrzyzyk.controller;

import java.util.Arrays;
import java.util.Random;

import nkolkoikrzyzyk.model.Mark;
import nkolkoikrzyzyk.model.NeuralNetwork;

public class NeuralNetworkPlayer extends Player 
{
	private NeuralNetwork network;
	private Mark markType;
	private Random random;
		
	public NeuralNetworkPlayer(String name, Mark markType, NeuralNetwork network) {
		super(name, markType);
		this.network = network;
		this.markType = markType;
		random = new Random();
	}

	@Override
	public int[] makeMove( int[] board, int position ) {
		int bestMove = -1;
		
		if (network != null) {
			float[] input = new float[9];
			for (int i = 0; i < 9; i++)
				input[i] = (float)board[i];
			// TODO: conversion from Xs to Os will be probably needed
			
			float[] output = network.run(input);
			System.out.println("Neural network output = " + Arrays.toString(output));
			
			float bestProbability = Float.NEGATIVE_INFINITY;
			for (int i = 0; i < 9; i++)
				if (output[i] > bestProbability && board[i] == 0) {
					bestMove = i;
					bestProbability = output[i];
				}		
		} else
			System.out.println("network == null, doing random move...");
		
		if (bestMove == -1)
			do {
				bestMove = random.nextInt(9);
			} while (board[bestMove] == 0);

		System.out.println("Neural network player selected " + bestMove + " as a best move");
		board[bestMove] = markType.value();
		return board;
	}

	@Override
	public void youWin(int[] board) {
	}

	@Override
	public void youLost() {
	}
	
	@Override
	public void youDraw(int[] board) {
	}
}
