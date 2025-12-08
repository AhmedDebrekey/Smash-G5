import ENUMS.Discipline;
import java.util.InputMismatchException;
import java.util.EnumSet;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Club smash = new Club("Smash", "Guldbergsgade 29N");

        Coach johan = new Coach("Johan Hansen", 19880713, "Johanhansen@gmail.dk");
        Coach NotJohan = new Coach("Not Johan Hansen", 19880713, "Johanhansen@gmail.dk");

        smash.addCoach(johan);
        smash.addCoach(NotJohan);

        Member testMember = new Member("Mattias", 19990101, "test@smash.dk", 1, johan, EnumSet.of(Discipline.SINGLE, Discipline.DOUBLE), true);
        Member testMember2 = new Member("Gustav", 19990101, "test@smash.dk", 2, johan, EnumSet.of(Discipline.SINGLE, Discipline.DOUBLE), true);
        Member testMember3 = new Member("Liam", 20000315, "liam@smash.dk", 3, johan, EnumSet.of(Discipline.SINGLE, Discipline.DOUBLE), true);
        Member testMember4 = new Member("Sofia", 19981222, "sofia@smash.dk", 4, johan, EnumSet.of(Discipline.SINGLE, Discipline.DOUBLE), true);
        Member testMember5 = new Member("Noah", 19970509, "noah@smash.dk", 5, johan, EnumSet.of(Discipline.SINGLE, Discipline.DOUBLE), true);
        Member testMember6 = new Member("Freja", 20010130, "freja@smash.dk", 6, johan, EnumSet.of(Discipline.SINGLE, Discipline.DOUBLE), true);
        Member testMember7 = new Member("Oliver", 19981111, "oliver@smash.dk", 7, johan, EnumSet.of(Discipline.SINGLE, Discipline.DOUBLE), true);

        smash.addMember(testMember);
        smash.addMember(testMember2);
        smash.addMember(testMember3);
        smash.addMember(testMember4);
        smash.addMember(testMember5);
        smash.addMember(testMember6);
        smash.addMember(testMember7);

        Scanner input = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n===== SMASH CLUB MANAGEMENT SYSTEM =====");
            System.out.println("1. Add member");
            System.out.println("2. Edit member");
            System.out.println("3. Register Tournament");
            System.out.println("4. Show rankings");
            System.out.println("5. Payment overview");
            System.out.println("6. Save data");
            System.out.println("7. Load data");
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
                    System.out.println("\n>>> Register Tournamet... ");
                    smash.registerTournament();
                }
                case "4" -> {
                    System.out.println("\n>>> Showing rankings... ");
                    smash.ShowMatchResults();
                }
                case "5"  -> {
                    System.out.println("\n>>> Showing payment overview...");
                    smash.paymentOverview();
                }
                case "6" -> {
                    System.out.println("\n>>> Saving data... ");
                    smash.saveDataToFile("Smash_Data.txt");
                    // TODO: Save the data
                }

                case "7" -> {
                    System.out.println("\n>>> Loading data... ");
                    // TODO: Load Data
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