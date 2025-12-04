import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== SMASH CLUB MANAGEMENT SYSTEM =====");
            System.out.println("1. Add member");
            System.out.println("2. Edit member");
            System.out.println("3. Register result");
            System.out.println("4. Show rankings");
            System.out.println("5. Payment overview");
            System.out.println("6. Save data");
            System.out.println("7. Load data");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");
            System.out.println("hej");
            System.out.println("farvel");

            String choice = input.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.println(">>> Adding member... ");
                    // TODO: Add Member stuff
                }

                case "2" -> {
                    System.out.println(">>> Editing member... ");
                    // TODO: Edit that memeber
                }
                case "3"  -> {
                    System.out.println(">>> Registering result... ");
                    // TODO: Register the resutls
                }
                case "4" -> {
                    System.out.println(">>> Showing rankings... ");
                    // TODO: Show rankings
                }
                case "5"  -> {
                    System.out.println(">>> Showing payment overview...");
                    // TODO: Show overview over payments
                }
                case "6" -> {
                    System.out.println(">>> Saving data... ");
                    // TODO: Save the data
                    }

                case "7" -> {
                    System.out.println(">>> Loading data... ");
                    // TODO: Load Data
                }
                case "0" -> {
                    System.out.println("Exiting program...");
                    running = false;
                }

                default -> {
                    System.out.println("Invalid option. Try again.");
                }            }
        }

        input.close();
    }
}
