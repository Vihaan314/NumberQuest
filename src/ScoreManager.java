import java.io.*;
import java.util.ArrayList;
import java.util.List;

//Score manager will save all the necessary data from a number guessing game
public class ScoreManager {
    /**
     * To save all the data of the game to a text file, identified by the user's name
     * @param name The user's name entered
     * @param difficulty The difficulty that the user chose (Easy, Medium, Hard, Default)
     * @param upperNumMin The minumum number for the upper bound of the number generation
     * @param randomNumber The random number that was chosen
     * @param attempts The amount of attempts it took the user to guess the correct number
     * @param tries The amount of attempts the user had left after guessing the correct number
     */
    public void saveScore(String name, String difficulty, int upperNumMin, int randomNumber, int attempts, ArrayList<Integer> failedAttempts, int tries) {
        //Create the filename by using their inputted name
        String filename = "Guess_the_number_" + name + ".txt";
        //This uses a combination of the FileWriter, BufferedWriter, and PrinterWriter classes
        //This combination is used for more efficient file writing
        //First, we create a FileWriter instance, which is used to directly write character data to a file
        //However, writing each piece to a file can be inefficient, which is why we then have a BufferedWriter.
        try (FileWriter fw = new FileWriter(filename, true);
             //The BufferedWriter instance allows data to be accumulated in an internal buffer, which can improve performance for these small pieces of data
             BufferedWriter bw = new BufferedWriter(fw);
             //The PrintWriter finally allows for the formatted text to be written to an output stream.
             //Now, the combination of the BufferedWriter and the PrintWriter allows for the more efficient writing operations and writing formatted text.
             PrintWriter out = new PrintWriter(bw)) {
            out.println("Name: " + name);
            out.println("Difficulty: " + difficulty);
            out.println("Number range: 1 - " + upperNumMin);
            out.println("Number: " + randomNumber);
            out.println("Attempts: " + attempts);
            out.println("Failed attempts: " + GameTools.arrayListToString(failedAttempts));
            out.println("Tries left: " + tries);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void saveScoreLeaderboard(String difficulty, List<String> names, List<Integer> scores) {
        //To be created
        //will allow you to take in a list of names and scores, and save all of that data in a text file
    }
}
