import java.util.Vector;
import java.util.Arrays;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * This class handles rules for the game
 */
public class Training {

	public static void main(String[] args) throws IOException{

        // Setting the learning and decay parameters from the terminal
        int games=0;          // Set the number of games that by self play will train the NN
        double lambda=0;      // Decay parameter
        double alpha=0;       // Learning parameter
        
        // Therefore if someone wants to run 10000 games with λ=0.7 and α=0.1 through the terminal, then it can
        // be performed like that: java Training 10000 0.7 0.1
        try{
            games = Integer.parseInt(args[0]);
            lambda = Double.parseDouble(args[1]);
            alpha = Double.parseDouble(args[2]);
        } catch (NumberFormatException nfe) {
            System.out.println("Wrong arguments through the terminal");
            System.exit(1);
        }
		
        //Initialize the variables responsible for counting during training
		int i = 0; int k = 0;
		int status1 = 0;
		int status2 = 0;

		int[] tom = new int[0];
		Vector<int[]> temp;
		System.out.println("Start the training of the Neural Network");
		Exbot eb = new Exbot();
   		Player player1 = new Player(lambda, alpha, true, eb);
   		Player player2 = new Player(lambda, alpha, true, eb);

        long start = System.currentTimeMillis();
        long elapsedTime = 0;
   		// Plays the matches that train the NN
   		while (i < games){

            // Needs more testing if we would want to change the parameters regarding their
            // actual divergence or convergence. It is performed more intuitively here
            switch(i){
                case 10000:     Player.net.writeTo("10k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (10k): " + elapsedTime/60000);                                                           
                                break;
                case 20000:     Player.net.writeTo("20k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (20k): " + elapsedTime/60000);                                                           
                                break;
                case 30000:     Player.net.writeTo("30k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (30k): " + elapsedTime/60000);
                                break;
                case 40000:     Player.net.writeTo("40k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (40k): " + elapsedTime/60000);
                                break;
                case 50000:     Player.net.writeTo("50k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (50k): " + elapsedTime/60000);
                                break;                                
                case 75000:     Player.net.writeTo("75k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (75k): " + elapsedTime/60000);
                                break;
                case 100000:    Player.net.writeTo("100k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (100k): " + elapsedTime/60000);
                                break;
                case 125000:    Player.net.writeTo("125k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (125k): " + elapsedTime/60000);
                                break;
                case 150000:    Player.net.writeTo("150k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (150k): " + elapsedTime/60000);
                                break;
                case 175000:    Player.net.writeTo("175k");
                                elapsedTime = System.currentTimeMillis() - start;
                                System.out.println("Total time in min so far (175k): " + elapsedTime/60000);
                                break;
                    default:    break;                                
            }            
   
   			// This is where the game is played. Loops until game victory.
   			while (status1 < 1 && status2 < 1) {
   			
    			// Gets a vector of possible moves
       			temp = eb.getPossibleMoves();
    			
    			if (temp.size()!=0){
                    // separate player 1 (1) from player 2 (-1)
     				if(eb.getGame()[52] == 1){			
						int [] bestMove = player1.move(temp, eb.getGame());
                        status1 = eb.makeMoves(bestMove);
					}
					else{
						status2 = eb.makeMoves(player2.move(temp, eb.getGame()));
					}
    			} else {
     				if(eb.getGame()[52] == 1)
     					status1 = eb.makeMoves(tom);
     				else
     					status2 = eb.makeMoves(tom);
    			}
   			}

            // Here is performed the training of the NN when the game has ended
   			if (status1 > 0){
   				player1.won(eb.getGame());
   				player2.lost(eb.getGame());
   			}
   			else if (status2 > 0){
   				player2.won(eb.getGame());
   				player1.lost(eb.getGame());
   			}

            // Checks if matchs finnished and sets up next game/match
            i += Math.abs(eb.resolveVictory());
            status1 = 0; status2 = 0;

   		}

        Player.net.writeTo("SavedNN");

        elapsedTime = System.currentTimeMillis() - start;
        System.out.println("Total time in min: " + elapsedTime/60000);
  	}
}