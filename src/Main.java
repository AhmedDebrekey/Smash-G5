import ENUMS.Discipline;
import java.util.InputMismatchException;
import java.util.EnumSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Club smash = new Club("Smash", "Guldbergsgade 29N");

        Cashier cashier = smash.getCashier();

        LoadData.loadFromFile("Smash_Data.txt", smash);

        smash.addDataChangeListener(new AutoSaveListener (smash, "Smash_Data.txt", cashier));

        Coach johan = new Coach("Johan Hansen", 19880713, "Johanhansen@gmail.dk");
        Coach NotJohan = new Coach("Not Johan Hansen", 19880713, "Johanhansen@gmail.dk");
        smash.addCoach(johan);
        smash.addCoach(NotJohan);

        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== SMASH CLUB MANAGEMENT SYSTEM =====");
            System.out.println("1. Add member");
            System.out.println("2. Edit member");
            System.out.println("3. Register Tournament");
            System.out.println("4. Show Match Rankings");
            System.out.println("5. Show Members Rankings");
            System.out.println("6. Payment overview");
            System.out.println("7. Mark member as paid");
            System.out.println("8. Save data");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.println("\n>>> Adding member... ");
                    smash.createMember();
                }

                case "2" -> {
                    System.out.println("\n>>> Editing member... ");
                    smash.editMember();
                }
                case "3"  -> {
                    System.out.println("\n>>> Register Tournament... ");
                    smash.registerTournament();
                }
                case "4" -> {
                    System.out.println("\n>>> Showing rankings... ");
                    smash.ShowMatchResults();
                }
                case "5"  -> {
                    System.out.println("\n>>> Showing Members Rankings... ");
                    smash.ShowMembersResults();
                }
                case "6"  -> {
                    System.out.println("\n>>> Showing payment overview...");
                    smash.paymentOverview();
                }
                case "7" -> {
                    System.out.println("\n>>> Showing members...");
                    smash.setMemberAsPaid();
                }
                case "8" -> {
                    System.out.println("\n>>> Saving data... is done automatically after any change");
                    // TODO: Save the data
                    smash.notifyDataChanged();
                }
                case "0" -> {
                    System.out.println("\nExiting program...");
                    running = false;
                }

                default -> {
                    System.out.println("\nInvalid option. Try again.");
                }            }
        }

        input.close();
    }
}