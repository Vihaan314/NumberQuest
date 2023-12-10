import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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

    /**
     * Converts an ArrayList of integers, or ComplexNumbers to a string
     * @param arrayList The ArrayList
     * @return A string including the brackets of the ArrayList
     */
    public static String arrayListToString(ArrayList<?> arrayList) {
        StringBuilder arrayString = new StringBuilder();
        arrayString.append("[");
        for (Object attempt : arrayList) {
            if (attempt instanceof Integer) {
                arrayString.append(arrayList.indexOf(attempt) == arrayList.size() - 1 ? attempt : attempt + ", ");
            } else if (attempt instanceof ComplexNumber) {
                arrayString.append(arrayList.indexOf(attempt) == arrayList.size() - 1 ? attempt.toString() : attempt.toString() + ", ");
            }
        }
        arrayString.append("]");
        return arrayString.toString();
    }

    /**
     * Custom standard deviation method that returns an a deviation of user guesses and a target number
     * @param values The arraylist of user integer attempts
     * @param number The target (random number)
     * @return A double, containing the custom standard deviation function sqrt((Sum((abs(number-values_i))**2)/ values_length)
     */
    public static double standardDeviationNum(ArrayList<Integer> values, int number) {
        int totalDif = 0;
        for (Integer attempt : values) {
            totalDif += Math.pow(Math.abs(number - attempt), 2);
        }
        return Math.sqrt((double) totalDif /(values.size()));
    }

    /**
     * Custom standard deviation method that returns an a deviation of a complex number and the
     * @param values The arraylist of user integer attempts
     * @param complexNumber The target (random complex number)
     * @return A double, containing the custom standard deviation function sqrt((Sum((abs(|complex-values|_i))**2)/ values_length)
     */
    public static double standardDeviationComplex(ArrayList<ComplexNumber> values, ComplexNumber complexNumber) {
        int totalDif = 0;
        for (ComplexNumber attempt : values) {
            totalDif += Math.pow(Math.abs(complexNumber.magnitude - attempt.magnitude), 2);
        }
        return Math.pow((double) totalDif /(values.size()), (double) (1 / 3));
    }

    public static double getSTDWeight(double std) {
        return 1/(Math.log10(std)+1) + 1/(2*Math.log10(std))-0.5*Math.log10(std/17);
    }

    public static double roundDecimal(double number, double dp) {
        return (double) (Math.round(number * Math.pow(10d, dp))) / Math.pow(10d, dp);
    }

    /**
     * To compute the overall final score of the user through a weighted formula
     * @param tries The amount of attempts the user had left after guessing the correct number
     * @param attempts The amount of attempts it took the user to guess the correct number
     * @param failedAttempts The ArrayList of failed attempts by the user
     * @param randomNumber The random number that was chosen
     * @param upperBound The minumum number for the upper bound of the number generation
     */
    public static int computeFinalScore(int tries, int attempts, ArrayList<Integer> failedAttempts, int randomNumber, int upperBound) {
        //std deviation of failed, tries, attempts, rand num, range
        double std = standardDeviationNum(failedAttempts, randomNumber);

        double weightForStd = getSTDWeight(std);
        double weightForTries = 10 - (double) 25 / upperBound;
        double weightForAttempts = 20.0;
        double weightForUpperBound = 2;

        double stdScore = std * weightForStd;
        double triesScore = tries * weightForTries;
        double attemptsScore = (1.0 / attempts) * weightForAttempts;
        double upperBoundScore = Math.log(upperBound) * weightForUpperBound;
        double luckScore = 5*Math.log10(1/getChance(failedAttempts, upperBound, randomNumber));

        System.out.println("Weighted scores ([std score, tries score, attempts score, upper bound score, luck]): " + Arrays.toString(new double[]{roundDecimal(stdScore, 2), roundDecimal(triesScore, 2), roundDecimal(attemptsScore, 2), roundDecimal(upperBoundScore, 2), roundDecimal(luckScore, 2)}));
        double finalScore = (triesScore + upperBoundScore + luckScore) - (stdScore + attemptsScore);

        return Math.max(1, (int) Math.round(finalScore));
    }

    /**
     * To compute the overall final score of the user through a weighted formula
     * @param tries The amount of attempts the user had left after guessing the correct number
     * @param attempts The amount of attempts it took the user to guess the correct number
     * @param failedAttempts The ArrayList of failed attempts by the user
     * @param complexNumber The random complex number that was chosen
     * @param upperBound The minumum number for the upper bound of the number generation
     */
    public static int computeFinalScoreComplex(int tries, int attempts, ArrayList<ComplexNumber> failedAttempts, ComplexNumber complexNumber, int upperBound) {
        double std = standardDeviationComplex(failedAttempts, complexNumber);

        double weightForStd = Math.log10(upperBound)/10;
        double weightForTries = 10.0;
        double weightForAttempts = 20.0;
        double weightForUpperBound = 3;

        double stdScore = (std == 0) ? 0 : std * weightForStd;
        double triesScore = tries * weightForTries;
        double attemptsScore = (attempts == 0) ? 0 : (1.0 / attempts) * weightForAttempts;
        double upperBoundScore = Math.log(upperBound) * weightForUpperBound;
        System.out.println("Score weights ([std score, tries score, attempts score, upper bound score]): " + Arrays.toString(new double[]{stdScore, triesScore, attemptsScore, upperBoundScore}));
        double finalScore = triesScore + upperBoundScore - stdScore - attemptsScore;

        return Math.max(1, (int) Math.round(finalScore));
    }

    public static double getChance(ArrayList<Integer> failedAttempts, int upperBound, int randomNumber) {
        if (failedAttempts.size() == 0) {
            return (double) 1 / (upperBound);
        }
        else if (failedAttempts.size() == 1) {
            if (randomNumber > failedAttempts.get(0)) {
                return (double) 1 / (upperBound-failedAttempts.get(0));
            }
            else {
                return (double) 1/ (failedAttempts.get(0));
            }
        }
        else {
            ArrayList<Integer> differences = new ArrayList<>();
            for (int attempt : failedAttempts) {
                differences.add(attempt - randomNumber);
            }
            ArrayList<Integer> lower = new ArrayList<>();
            ArrayList<Integer> higher = new ArrayList<>();
            for (int diff : differences) {
                if (diff < 0) {
                    lower.add(diff);
                } else {
                    higher.add(diff);
                }
            }
            int lowerMin = Collections.max(lower);
            int upperMin = Collections.min(higher);
            int lowest = failedAttempts.get(differences.indexOf(lowerMin));
            int highest = failedAttempts.get(differences.indexOf(upperMin));
            return (double) 1 / (highest - lowest-1);
        }
    }
}
