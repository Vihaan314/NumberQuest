import java.util.HashMap;

/**
 * This is a simple class that returns the max number range based on the difficulty the user has chosen
 */
public class Difficulty {
    HashMap<String, Integer> difficultyMap = new HashMap<>();
    String difficulty;
    //Constructor takes in the difficulty as a string, and creates the dictionary of difficulties to their corresponding upper bound
    public Difficulty(String difficulty) {
        difficultyMap.put("easy", 10);
        difficultyMap.put("medium", 100);
        difficultyMap.put("hard", 500);
        difficultyMap.put("default", 50);
        difficultyMap.put("complex", 25);
        this.difficulty = difficulty;
    }

    /**
     * @return The lower bound for the upper bound of the number generation range
     */
    public int getUpperLowestBound() {
        return difficultyMap.get(this.difficulty);
    }
}
