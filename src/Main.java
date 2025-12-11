import ENUMS.Discipline;
import java.util.InputMismatchException;
import java.util.EnumSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Club smash = new Club("Smash", "Guldbergsgade 29N");

        Cashier cashier = smash.getCashier();

        Coach johan = new Coach("Johan Hansen", 19880713, "Johanhansen@gmail.dk");
        Coach NotJohan = new Coach("Not Johan Hansen", 19880713, "Johanhansen@gmail.dk");
        smash.addCoach(johan);
        smash.addCoach(NotJohan);

        LoadData.loadFromFile("Smash_Data.txt", smash, cashier);

        smash.addDataChangeListener(new AutoSaveListener (smash, "Smash_Data.txt", cashier));

        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== SMASH CLUB MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Member");
            System.out.println("2. Remove Member");
            System.out.println("3. Edit Member");
            System.out.println("4. Register Match");
            System.out.println("5. Show Match Rankings");
            System.out.println("6. Show Members Rankings");
            System.out.println("7. Payment Overview");
            System.out.println("8. Mark Member as Paid");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = input.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.println("\n>>> Adding member... ");
                    smash.createMember();
                }
                case "2" -> {
                    System.out.println("\n>>> Showing members...");
                    smash.removeMemberFromClub();
                }
                case "3" -> {
                    System.out.println("\n>>> Editing member... ");
                    smash.editMember();
                }
                case "4"  -> {
                    System.out.println("\n>>> Register Match... ");
                    smash.registerMatch();
                }
                case "5" -> {
                    System.out.println("\n>>> Showing rankings... ");
                    smash.ShowMatchResults();
                }
                case "6"  -> {
                    System.out.println("\n>>> Showing Members Rankings... ");
                    smash.ShowMembersResults();
                }
                case "7"  -> {
                    System.out.println("\n>>> Showing payment overview...");
                    smash.paymentOverview();
                }
                case "8" -> {
                    System.out.println("\n>>> Showing members...");
                    smash.setMemberAsPaid();
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