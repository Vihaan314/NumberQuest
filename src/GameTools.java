import java.util.ArrayList;
import java.util.Arrays;

public class GameTools {
    //Contains method that allowed you to pause the game for a set amount of time (ms)
    //Has the possibility to expand if there are any other miscellaneous methods

    /**
     * Pauses the program for given amount of milliseconds
     * @param milliseconds The amount of milliseconds of wait time
     */
    public static void waitFor(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static String arrayListToString(ArrayList<Integer> arrayList) {
        StringBuilder arrayString = new StringBuilder();
        arrayString.append("[");
        for (Integer f : arrayList) {
            arrayString.append(arrayList.indexOf(f) == arrayList.size()-1 ? f.toString() : f.toString() + ", ");
        }
        arrayString.append("]");
        return arrayString.toString();
    }
    public static double standardDeviationNum(ArrayList<Integer> values, int number) {
        int totalDif = 0;
        for (int i = 0; i < values.size(); i++ ){
            totalDif += Math.pow(Math.abs(number-values.get(i)), 2);
        }
        return Math.sqrt((double) totalDif /(values.size()));
    }

    public static int computeFinalScore(int tries, int attempts, ArrayList<Integer> failedAttempts, int randNum, int upperBound) {
        //std deviation of failed, tries, attempts, rand num, range
        double std = standardDeviationNum(failedAttempts, randNum);

        double weightForStd = 0.1;
        double weightForTries = 10.0;
        double weightForAttempts = 20.0;
        double weightForUpperBound = 5;

        double stdScore = std * weightForStd;
        double triesScore = tries * weightForTries;
        double attemptsScore = (1.0 / attempts) * weightForAttempts;
        double upperBoundScore = Math.log(upperBound) * weightForUpperBound;

        System.out.println(Arrays.toString(new double[]{stdScore, triesScore, attemptsScore, upperBoundScore}));

        double finalScore = triesScore + upperBoundScore - stdScore - attemptsScore;

        return Math.max(0, (int) Math.round(finalScore));
    }
}
