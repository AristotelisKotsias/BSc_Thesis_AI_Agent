import java.util.Vector;


public class Utility{

  public static double computeUtility(double[] output){
        return -output[0];      // The array output has only one element (because we have only one output unit) 
                                // and since we try to minimize the utility for the "other player", we take the 
                                // negative value of the output unit
    }
	
	public static Vector<int[]> playerStatus(int player, int[] getGame){
  		Vector<int[]> players = new Vector<int[]>(2);
  		int [] player1 = new int[24];
  		int [] player2 = new int[24];
  		if(player == 1){  // 1 means player 1 while -1 is player 2
  			System.arraycopy(getGame, 1, player1, 0, 24);
  			System.arraycopy(getGame, 27, player2, 0, 24);
  			players.add(player1); players.add(player2);
  		}else{
  			//Player 1
  			for (int i=0; i<12; i++)
  				player1[i] = getGame[i+13];
  			for (int i=12; i<24; i++)
  				player1[i] = getGame[i-11];
  			//Player 2
  			for (int i=0; i<12; i++)
  				player2[i] = getGame[i+39];
  			for (int i=12; i<24; i++)
  				player2[i] = getGame[i+15];
  			players.add(player2); players.add(player1);
  		}
  		return players;
 	 }


  	public static int bar1 (int[] getGame){
  		return getGame[0];
  	}

  	public static int bar2 (int[] getGame){
  		return getGame[26];
  	}

  	public static int home1 (int[] getGame){
  		return getGame[25];
  	}

  	public static int home2 (int[] getGame){
  		return getGame[51];
  	}

  	public static double[] boardToVector(int player, int[] getGame){
  		double[] input = new double[196];
  		Vector<int[]> players = playerStatus(player, getGame);
  		int [] player1 = players.get(0);
  		int [] player2 = players.get(1);
  		int counter = 0;
  		int checkers;   // doesn't matter if it's white or black

  		//First for the opponent of the current player
  		for (int i=0; i<24 ; i++){
  			checkers = player2[i];
  			if (checkers == 0){
  				counter += 4;
  			}
  			else if (checkers == 1){
  				input[counter]=1;
  				counter += 4;
  			}
  			else if (checkers == 2){
  				input[counter]=1;
  				input[counter+1]=1;
  				counter += 4;
  			}
  			else if (checkers >= 3){
  				for (int j=0; j<3; j++){
  					input[counter] = 1;
  					counter += 1;
  				}
  				input[counter] = (checkers - 3.0)/2.0;
  				counter +=1;
			}
  		}

		if(player == 1){ // 1 means player 1
			input[counter] = bar1(getGame) / 2.0;
			input[counter+1] = bar2(getGame) / 2.0;
			input[counter+3] = home1(getGame) / 15.0;
			input[counter+4] = home2(getGame) / 15.0;
			counter += 4;
  		} else {
  			input[counter] = bar2(getGame) / 2.0;
			input[counter+1] = bar1(getGame) / 2.0;
			input[counter+3] = home2(getGame) / 15.0;
			input[counter+4] = home1(getGame) / 15.0;
			counter += 4;
  		}

  		// Now for the current player
  		for (int i=0; i<24; i++){
  			checkers = player1[i];
  			if (checkers == 0){
  				counter += 4;
  			}
  			else if (checkers == 1){
  				input[counter]=1;
  				counter += 4;
  			}
  			else if (checkers == 2){
  				input[counter]=1;
  				input[counter+1]=1;
  				counter += 4;
  			}
  			else if (checkers >= 3){
  				for (int j=0; j<3; j++){
  					input[counter] = 1;
  					counter += 1;
  				}
  				input[counter] = (checkers - 3.0)/2.0;
  				counter +=1;
  			}
  		}
  	
  		if (counter!=196)
  			System.out.println("No 196 input array");

  		return input;
  	}

    
}