import java.util.*;


//IMPLEMENT CONDITIONAL NUMBER GUESSING (e.g. x > 5, x <= y, etc)
//Implement final score system that takes into account tries left / attempts and the random number using some algorithm
//Enable hints (proximity to famous numbers)
//Guess complex number (tell magnitude)

//Game class contains all the necessary components of the game
public class Game {
    //Creates all the necessary variables
    private Scanner scanner = new Scanner(System.in);
    private String difficulty;
    private int upperNumMin;
    private int randomNumber;
    private ArrayList<Integer> failedAttempts = new ArrayList<>();
    private int attempts;
    private int tries;
    private String name;
    private ScoreManager scoreManager = new ScoreManager();

    //For multiplayer - storing multiple players and their respective scores. Not implemented yet
    private List<String> names = new ArrayList<>();
    private List<Integer> scores = new ArrayList<>();

    /**
     * Starts the game by calling all the required methods
     */
    public void start() {
        System.out.println("Welcome to Guess The Number Ultimate");
        //Pauses for 500 ms (for effect)
        GameTools.waitFor(500);
        //Gets the user upper bound for the number generation range
        int number = setupGame();
        //Runs the game
        runGame(number);
    }

    public int getPlayers() {
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
    public int getRange(int upperNumMin) {
        System.out.print("Choose the number generation range (" + upperNumMin + " or greater): 1 and... ");
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
     * Deciding the lower limit of the upper bound based on the difficulty the user entered
     *
     * @return The minimum upper bound
     */
    private int setupGame() {
        System.out.print("Choose difficulty - [Easy / Medium / Hard / Default]: ");
        difficulty = scanner.nextLine().toLowerCase();
        //Ensures that the user will enter one of the provided difficulties
        while (!Arrays.asList("easy", "medium", "hard", "default").contains(difficulty.toLowerCase())) {
            System.out.println("Please enter one of the provided difficulties!");
            System.out.print("Choose difficulty - [Easy / Medium / Hard / Default]: ");
            difficulty = scanner.nextLine().toLowerCase();
        }

        //The final part the setup is to return the actual upper bound of the user, done by calling the getRange using the min upper bound
        Difficulty difficultyChoose = new Difficulty(difficulty);
        upperNumMin = difficultyChoose.getUpperLowestBound();
        return getRange(upperNumMin);
    }

    public void runGame(int number) {
        attempts = 0;
        System.out.print("Hello, What is your name? ");
        name = scanner.nextLine();

        //For the multiplayer mode, not implemented yet
        names.add(name);

        System.out.println("Hello " + name + ".");

        randomNumber = new Random().nextInt(number) + 1;
        tries = (int) (Math.ceil(Math.log(number) / Math.log(2)));

        System.out.println("Generating random number...");
        GameTools.waitFor(500);
        System.out.println("You have " + tries + " tries!");
        System.out.println("I am thinking of a number between 1 and " + number);

        while (tries > 0) {
            System.out.print("Have a guess: ");
            int guess = scanner.nextInt();
            scanner.nextLine();
            if ((difficulty.equals("easy") || difficulty.equals("medium")) && failedAttempts.contains(guess)) {
                System.out.println("You have already guessed this! Please try another number");
            }
            else if (guess == randomNumber) {
                break;
            }
            else {
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

            //Currently saves the score for the user that played the single player game
            scoreManager.saveScore(name, difficulty, upperNumMin, randomNumber, attempts, failedAttempts, tries);

            //Print out their stats
            System.out.println("");
            System.out.println("Name: " + name);
            System.out.println("Difficulty: " + difficulty);
            System.out.println("Number range: 1 - " + upperNumMin);
            System.out.println("Number: " + randomNumber);
            System.out.println("Attempts: " + attempts);
            System.out.println("Failed attempts: " + GameTools.arrayListToString(failedAttempts));
            System.out.println("Tries left: " + tries);

        } else {
            //The number of tries the user has is 0 - none left
            System.out.println("Sorry, you ran out of tries! The correct number was " + randomNumber);
        }
    }
}
