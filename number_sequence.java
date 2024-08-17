import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class NumberSequencingGame {

    private static final int INITIAL_LENGTH = 3;
    private static List<Integer> sequence;
    private static int highScore = 0;
    private static List<Integer> highScores = new ArrayList<>();
    private static double averageScore = 0.0;
    private static int totalGames = 0;

    public static List<Integer> generateSequence(int length) {
        List<Integer> seq = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            seq.add(random.nextInt(10));
        }
        return seq;
    }

    public static void displaySequence(List<Integer> sequence, int displayTime) {
        System.out.println("Memorize this sequence:");
        for (Integer num : sequence) {
            System.out.print(num + " ");
        }
        System.out.println();

        try {
            Thread.sleep(displayTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Error: Interrupted while displaying sequence.");
        }

        // Clear console by printing new lines
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.err.println("Error: Unable to clear console.");
        }
    }

    public static void showHint(List<Integer> sequence, int hintSize) {
        System.out.println("Hint: The first " + hintSize + " numbers are " + sequence.subList(0, hintSize));
    }

    public static void printMenu() {
        System.out.println("Number Sequencing Game");
        System.out.println("1. Start New Game");
        System.out.println("2. Show High Score");
        System.out.println("3. Show Average Score");
        System.out.println("4. Show Leaderboard");
        System.out.println("5. Exit");
    }

    private static void showLeaderboard() {
        System.out.println("Leaderboard:");
        Collections.sort(highScores, Collections.reverseOrder()); // Sort high scores in descending order
        for (int i = 0; i < Math.min(highScores.size(), 10); i++) {
            System.out.println((i + 1) + ". " + highScores.get(i));
        }
    }

    private static void updateAverageScore(int rounds) {
        totalGames++;
        averageScore = ((averageScore * (totalGames - 1)) + rounds) / totalGames;
    }

    private static int getValidatedIntInput(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer.");
            scanner.next(); // clear the invalid input
        }
        return scanner.nextInt();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");
            int choice = getValidatedIntInput(scanner);

            switch (choice) {
                case 1:
                    int length = INITIAL_LENGTH;
                    int rounds = 0;
                    boolean continuePlaying = true;

                    while (continuePlaying) {
                        System.out.println("Choose difficulty level: 1) Easy 2) Medium 3) Hard");
                        int difficulty = getValidatedIntInput(scanner);
                        int displayTime = 2000; // Default 2 seconds
                        int hintSize = 2;
                        switch (difficulty) {
                            case 1: displayTime = 3000; hintSize = 3; break; // Easy: 3 seconds
                            case 2: displayTime = 2000; hintSize = 2; break; // Medium: 2 seconds
                            case 3: displayTime = 1000; hintSize = 1; break; // Hard: 1 second
                            default: System.out.println("Invalid choice. Defaulting to Medium.");
                        }

                        sequence = generateSequence(length);
                        displaySequence(sequence, displayTime);

                        List<Integer> userGuess = new ArrayList<>();
                        System.out.println("Enter your guess (" + length + " numbers):");

                        for (int i = 0; i < length; i++) {
                            userGuess.add(getValidatedIntInput(scanner));
                        }

                        if (sequence.equals(userGuess)) {
                            System.out.println("Correct! Moving to the next round.");
                            length++;
                            rounds++;
                            if (rounds > highScore) {
                                highScore = rounds;
                            }
                        } else {
                            System.out.println("Incorrect. The sequence was " + sequence);
                            showHint(sequence, hintSize); // hint
                            System.out.println("Game Over. You played " + rounds + " rounds.");
                            System.out.println("Your highest score is " + highScore + ".");
                            updateAverageScore(rounds);
                            System.out.println("Your average score is " + averageScore + ".");
                            highScores.add(rounds); // Add score to leaderboard
                            System.out.println("Would you like to play again? (yes/no)");
                            String response = scanner.next().toLowerCase();
                            if (!response.equals("yes")) {
                                continuePlaying = false;
                            }
                            length = INITIAL_LENGTH; // Reset sequence length
                            rounds = 0; // Reset round count
                        }
                    }
                    break;
                case 2:
                    System.out.println("High Score: " + highScore);
                    break;
                case 3:
                    System.out.println("Average Score: " + averageScore);
                    break;
                case 4:
                    showLeaderboard();
                    break;
                case 5:
                    System.out.println("Exiting the game. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
        scanner.close();
    }
}
