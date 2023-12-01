import java.util.ArrayList;

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
}
