import java.util.*;

//Enable hints (proximity to famous numbers)

//Game class contains all the necessary components of the game
public class Game {
    //Creates all the necessary variables
    private Scanner scanner = new Scanner(System.in);
    private String difficulty;
    private int upperNumMin;
    private int randomNumber;
    private ArrayList<Integer> failedAttempts = new ArrayList<>();
    private ArrayList<ComplexNumber> failedAttemptsComplex = new ArrayList<>();
    private int attempts;
    private int tries;
    private String name;
    private ScoreManager scoreManager = new ScoreManager();

    //For multiplayer - storing multiple players and their respective scores. Not implemented yet
    private List<String> names = new ArrayList<>();
    private List<Integer> scores = new ArrayList<>();

    private ComplexNumber complexNumber = new ComplexNumber();
    private boolean isComplex;

    public void start() {
        System.out.println("Welcome to Guess The Number Ultimate");
        //Pauses for 500 ms (for effect)
        GameTools.waitFor(500);
        //Gets the user upper bound for the number generation range
        int number = setupGame();
        //Runs the game
        runGame(number);
    }

    private int getPlayers() {
        System.out.print("Enter amount of players: ");
        if (!scanner.hasNextInt()) {
            System.out.println("Not an integer!");
            System.out.println("Please enter a valid number!");
            scanner.next();
            return getPlayers();
        }

        int numPlayers = scanner.nextInt();
        scanner.nextLine();
        return numPlayers;
    }

    /**
     * To get the upper range for the number generation range
     * @param upperNumMin An integer that signifies the lower limit of the upper bound that the user can enter, provided by the difficulty in setupGame
     * @return The upper range provided the user
     */
    private int getRange(int upperNumMin) {
        if (isComplex) {
            System.out.print("Enter maximum absolute value for real and imaginary parts (" + upperNumMin + " or greater): 1 and ... ");
        }
        else {
            System.out.print("Choose the number generation range (" + upperNumMin + " or greater): 1 and... ");
        }
        if (!scanner.hasNextInt()) {
            System.out.println("Not an integer!");
            System.out.println("Please enter a valid number!");
            scanner.next();
            return getRange(upperNumMin);
        }

        int number = scanner.nextInt();
        scanner.nextLine();

        if (number < upperNumMin || number > 1000000) {
            System.out.println("Number not in valid range!");
            System.out.println("Please enter a number within the valid range.");
            return getRange(upperNumMin);
        }

        return number;
    }

    /**
     * Seutp the conditions for the complex number guessing mode, also determining the upper bound for the (a, b) of complex number
     * @return The upper bound for the complex number generation
     */
    private int setupComplexGame() {
        System.out.println("Complex Number mode!");
        System.out.println("The real part will always be less than or equal to the imaginary part.");
        System.out.println("(a, b) > 0");
        upperNumMin = 25;

        return getRange(upperNumMin);
    }

    /**
     * Deciding the lower limit of the upper bound based on the difficulty the user entered
     * @return The minimum upper bound
     */
    private int setupGame() {
        System.out.print("Choose mode - [Easy / Medium / Hard / Default / Complex]: ");
        difficulty = scanner.nextLine().toLowerCase();
        //Ensures that the user will enter one of the provided difficulties
        while (!Arrays.asList("easy", "medium", "hard", "default", "complex").contains(difficulty)) {
            System.out.println("Please enter one of the provided modes!");
            System.out.print("Choose mode - [Easy / Medium / Hard / Default / Complex]: ");
            difficulty = scanner.nextLine().toLowerCase();
        }

        isComplex = difficulty.equals("complex");
        if (isComplex) {
            // Set up for complex number guessing
            return setupComplexGame();
        }
        else {
            //The final part the setup is to return the actual upper bound of the user, done by calling the getRange using the min upper bound
            Difficulty difficultyChoose = new Difficulty(difficulty);
            upperNumMin = difficultyChoose.getUpperLowestBound();
            return getRange(upperNumMin);
        }
    }

    public void printResults(int numberUpper, double chance) {
        //Print out the game statistics
        System.out.println();
        System.out.println("Name: " + name);
        System.out.println("Difficulty: " + difficulty);
        System.out.println("Number range minimum: " + upperNumMin);
        System.out.println("Number range: 1 - " + numberUpper);
        if (isComplex) {
            System.out.println("Complex Number: " + complexNumber.toString());
            System.out.println("Magnitude squared: " + Math.round(complexNumber.getMagnitudeSquared()));
            System.out.println("Attempts: " + attempts);
            System.out.println("Failed attempts: " + GameTools.arrayListToString(failedAttemptsComplex));
            System.out.println("Tries left: " + tries);
            System.out.println("Final Score: " + GameTools.computeFinalScoreComplex(tries, attempts, failedAttemptsComplex, complexNumber, upperNumMin));
        }
        else {
            System.out.println("Random Number: " + randomNumber);
            //Inform the user of the probability that they guessed the correct number (that is, if they most optimally used the information of their guesses)
            System.out.println("There was a " + GameTools.roundDecimal(chance*100.0, 2) + "% chance of guessing the correct number!");
            System.out.println("Attempts: " + attempts);
            System.out.println("Failed attempts: " + GameTools.arrayListToString(failedAttempts));
            System.out.println("Tries left: " + tries);
            System.out.println("Final Score: " + GameTools.computeFinalScore(tries, attempts, failedAttempts, randomNumber, upperNumMin));
        }
    }

    public void runGame(int numberUpper) {
        attempts = 0;
        System.out.print("Hello, What is your name? ");
        name = scanner.nextLine();

        //For the multiplayer mode, not implemented yet
        names.add(name);

        System.out.println("Hello " + name + ".");

        //Run the complex mode if selected after the mode exclusive code
        if (isComplex) {
            runComplexGame(numberUpper);
        }
        else {
            randomNumber = new Random().nextInt(numberUpper) + 1;
            tries = (int) (Math.ceil(Math.log(numberUpper) / Math.log(2))) + 2; //Because of binary search, +2 for margin of error -> decreased final score

            System.out.println("Generating random number...");
            GameTools.waitFor(500);
            System.out.println("You have " + tries + " tries!");
            System.out.println("I am thinking of a number between 1 and " + numberUpper);

            while (tries > 0) {
                System.out.print("Have a guess: ");
                int guess = scanner.nextInt();
                scanner.nextLine();
                if ((difficulty.equals("easy") || difficulty.equals("medium")) && failedAttempts.contains(guess)) {
                    System.out.println("You have already guessed this! Please try another number");
                } else if (guess == randomNumber) {
                    break;
                } else {
                    failedAttempts.add(guess);
                    if (guess < randomNumber) {
                        System.out.println("Guess Higher");
                    } else {
                        System.out.println("Guess Lower");
                    }
                }
                tries--;
                attempts++;
            }

            if (tries > 0) {
                //The correct number was guessed, but the final adjustments of the scoring need to be made once more to account for the correct guess out of the loop
                attempts++;
                tries--;
                System.out.println("Congrats, you guessed correctly!");

                //For the multiplayer mode, not implemented yet
                scores.add(tries);

                double chance = GameTools.getChance(failedAttempts, numberUpper, randomNumber);
                //Currently saves the score for the user that played the single player game
                scoreManager.saveScore(name, difficulty, numberUpper, upperNumMin, randomNumber, attempts, failedAttempts, tries, chance);
                printResults(numberUpper, chance);
            } else {
                //The number of tries the user has is 0 - none left
                System.out.println("Sorry, you ran out of tries! The correct number was " + randomNumber);
            }
        }
    }

    private void runComplexGame(int number) {
        //Generates a complex number with a unique magnitude
        boolean isUnique;
        do {
            int a = new Random().nextInt(number) + 1;
            int b = new Random().nextInt(number) + 1;
            complexNumber.setComplexNumber(Math.min(a, b), Math.max(a, b));
            isUnique = complexNumber.isUniqueMagnitude(number);
        } while (!isUnique);

        //Tries determined by the magnitude of the complex number
        tries = (int) (Math.ceil(Math.log(complexNumber.getMagnitudeSquared()))-1);

        System.out.println("Guess the complex number. The magnitude squared is: " + Math.round(complexNumber.getMagnitudeSquared()));
        System.out.println("You have " + tries + " tries");
        while (tries > 0) {
            System.out.print("Guess the real part: ");
            int guessReal = scanner.nextInt();
            System.out.print("Guess the imaginary part: ");
            int guessImag = scanner.nextInt();
            scanner.nextLine();

            ComplexNumber userGuessComplex = new ComplexNumber();
            userGuessComplex.setComplexNumber(guessReal, guessImag);
            
            if (complexNumber.checkEqualComplex(userGuessComplex)) {
                System.out.println("Correct! The complex number was indeed " + complexNumber.toString());
                scoreManager.saveScoreComplex(name, difficulty, number, upperNumMin, complexNumber, attempts, failedAttemptsComplex, tries);
                printResults(number, 0);
                break;
            } else {
                failedAttemptsComplex.add(userGuessComplex);
                complexNumber.compareComplex(userGuessComplex);
            }
            tries--;
            attempts++;
        }

        if (tries <= 0) {
            System.out.println("Out out of tries! The correct complex number was " + complexNumber.toString());
        }
    }
}
