import java.util.Vector;
import net.*;
import java.io.File;
import java.io.IOException;

import java.util.Arrays;

public class Player{
	
	//static as it is shared between the two players
	public static NeuralNetwork net = new NeuralNetwork(196, new int[] {100, 1});
	private final boolean learningMode;
	private double LAMBDA, ALPHA, BETA;
	//Not static since each player have its own copy of eligibility traces
	private double [][] Ew = new double[net.hidden[0].length][net.hidden[1].length];
	private double [][][] Ev = new double[net.input.length][net.hidden[0].length][net.hidden[1].length];
	Exbot eb;

	public Player(double lambda, double alpha, boolean learningmode, Exbot eb){
		this.eb = eb;
		for (int j = 0; j < net.hidden[0].length; j++)
            for (int k = 0; k < net.hidden[1].length; k++) {
                Ew[j][k]=0.0;
                for (int i = 0; i < net.input.length; i++) Ev[i][j][k] = 0.0;
            }
        LAMBDA = lambda;
        ALPHA = alpha*0.8;
        BETA = alpha*0.2;
        learningMode = learningmode;

        if (new File("SavedNN").isFile()){
            try {
                net = NeuralNetwork.readFrom("SavedNN");
                System.out.println("Import of old NN successful");
            } catch (ClassNotFoundException  e) {
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
	}

	/**
	 * Changing the valiables ALPHA, BETA and LAMBDA during training
	 * 
	 */
	public void setVariables(double lambda, double alpha){
		LAMBDA = lambda;
		ALPHA = alpha*0.8;
        BETA = alpha*0.2; 
	}


	public int[] move(Vector <int[]> temp, int[] getGame){
		int[] bestmove = null;

		int[] originalBoard = getGame;
		int[] nextBoard = null;
		int currentPlayer = getGame[52];		

		double expectedUtility = -1.0;	// When I check whether the utility of the opponent is minimun (for other player)
		//double expectedUtility = 0.0;		// When I check whether the utility of the current player is maximum (for current player)
		for (int[] m : temp){
		// This piece of code is used if I would want to choose a move without taking care if it is the winning one,
		// but letting the NN to choose the one according to its weight tuning and the value it returns. 	
			eb.makeMoves(m);
			double[] output = net.getValue(Utility.boardToVector(eb.getGame()[52], eb.getGame()));	//For other player
			double utility = Utility.computeUtility(output);	
		//	double[] output = net.getValue(Utility.boardToVector(currentPlayer, eb.getGame()));		//For current player
		//	double utility = -Utility.computeUtility(output);
			
			if(utility > expectedUtility){
				bestmove = m;
				expectedUtility = utility;
				nextBoard = eb.getGame();
			}
			eb.setGame(originalBoard);		
		}
		
		if(learningMode){
			double [] currentInput = Utility.boardToVector(currentPlayer, originalBoard);
			double [] currentOutput = net.getValue(currentInput);
			double[] nextOutput = net.getValue(Utility.boardToVector(currentPlayer, nextBoard));
			backprop(currentInput, currentOutput, nextOutput);
		}

		return bestmove;
	}

	public void lost(int[] getGame){
		if(learningMode){
			// It's '-getGame[52]' because when there is win, the active player does not change
			double[] in = Utility.boardToVector(-getGame[52], getGame); 
			double[] out = net.getValue(in);
			double[] actual = {0.0};
			backprop(in, out, actual);
		}
	}

	public void won(int[] getGame){
		if(learningMode){
			double[] in = Utility.boardToVector(getGame[52], getGame);
			double[] out = net.getValue(in);
			double[] actual = {1.0};
			backprop(in, out, actual);
		}
	}


	   public static double gradient(HiddenUnit u) {
        return u.getValue() * (1.0 - u.getValue());
    }


    /* Ew and Ev must be set up somewhere to the proper size and set to 0 */
    public void backprop(double[] in, double[] out, double[] expected) {
        /* compute eligibility traces */
        for (int j = 0; j < net.hidden[0].length; j++)
            for (int k = 0; k < out.length; k++) {
                /* ew[j][k] = (lambda * ew[j][k]) + (gradient(k)*hidden_j) */
                Ew[j][k] = (LAMBDA * Ew[j][k]) + (gradient(net.hidden[1][k]) * net.hidden[0][j].getValue());
                for (int i = 0; i < in.length; i++)
                    /* ev[i][j][k] = (lambda * ev[i][j][k]) + (gradient(k)+w[j][k]+gradient(j)+input_i)*/
                    Ev[i][j][k] = ( ( LAMBDA * Ev[i][j][k] ) + ( gradient(net.hidden[1][k]) * net.hidden[1][k].weights[j] * gradient(net.hidden[0][j])* in[i]));
            }
        double error[] = new double[out.length];
        for (int k =0; k < out.length; k++)
            error[k] = expected[k] - out[k];
        for (int j = 0; j < net.hidden[0].length; j++)
            for (int k = 0; k < out.length; k++) {
                /* weight from j to k, shown with learning param of BETA */
                net.hidden[1][k].weights[j] += BETA * error[k] * Ew[j][k];
                for (int i = 0; i < in.length; i ++) {
                    net.hidden[0][j].weights[i] += ALPHA * error[k] * Ev[i][j][k];
                }
            }
    }

}