import java.util.Vector;
import java.util.Arrays;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.Random;

/**
 * This class handles rules for the game
 */
public class TestStrength {

    public static void main(String[] args) throws IOException{

        // Set the number of games that by self play will train the NN
        int games = 1000;
        //Initialize the variables responsible for the training
        int i = 0; int k = 0;
        int status1 = 0;
        int status2 = 0;
        int[] tom = new int[0];
        Vector<int[]> temp;
        System.out.println("Start testing against the preferedMove()");
        Exbot eb = new Exbot();
        //Random rand = new Random(); // Used to choose a random move from the available ones in the possibleMoves vector
        Player player = new Player(0.0,0.0,false,eb);

        // Plays the numbers og games indicated by the variable games
        while (i < games){
   
            if (i%250 == 0){
                System.out.println(i);
            }
            
            // Loops until game victory
            while (status1 < 1 && status2 < 1) {
                // Gets a vector of possible moves
                temp = eb.getPossibleMoves();

                if (temp.size() != 0){

                    if(eb.getGame()[52] == 1){      
                        int [] bestMove = player.move(temp, eb.getGame());
                        status1 = eb.makeMoves(bestMove);
                    }
                    else{
                        // Play with preferedMove
                        status2 = eb.makeMoves(eb.getPreferedMove());

                        //Play with a random move from the vector of possible moves
                      /*  status2 = eb.makeMoves(temp.get(rand.nextInt(temp.size()))); */
                    }
                }else {
                    if(eb.getGame()[52] == 1)
                        status1 = eb.makeMoves(tom);
                    else
                        status2 = eb.makeMoves(tom);
                }
            }

            if (status1 > 0){
                  k++;
            }
   
            // Checks if matchs finnished and sets up next game/match
            i += Math.abs(eb.resolveVictory());

            status1 = 0; status2 = 0;
            
        }

        System.out.println("NN won " + k + " times or " + (double) (100*k)/games + "% of the times: ");
    }
}