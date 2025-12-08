import ENUMS.Discipline;

import java.util.EnumSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class InputHelpers {
    private static final Scanner scanner = new Scanner(System.in);

    static public int ReadInteger(String Message, String ErrorMessage){
        while (true) {
            try {
                System.out.print(Message);
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            } catch (InputMismatchException e) {
                System.out.println(ErrorMessage);
                scanner.nextLine();
            }
        }
    }

    public static int ReadIntInRange(String Message, String ParseErrorMessage,
                                     String RangeErrorMessage, int min, int max) {
        while (true) {
            System.out.print(Message);
            try {
                int value = scanner.nextInt();
                scanner.nextLine(); // clear newline

                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println(RangeErrorMessage);
                }
            } catch (InputMismatchException e) {
                System.out.println(ParseErrorMessage);
                scanner.nextLine(); // clear invalid input
            }
        }
    }

    static public String ReadString(String Message, String ErrorMessage) {
        while (true) {
            System.out.print(Message);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println(ErrorMessage);
        }
    }

    static public boolean ReadYesOrNo(String Message, String ErrorMessage) {
        while (true) {
            System.out.print(Message);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("y")) return true;
            if (input.equals("n")) return false;

            System.out.println(ErrorMessage);
        }
    }

    static public int ReadDate(String Message, String ErrorMessage) {
        while (true) {
            System.out.print(Message);
            int value = scanner.nextInt();
            Date date = new Date(value);

            if (date.valid()) {
                return value;
            }

            System.out.println(ErrorMessage);
            scanner.nextLine();
        }
    }

    public static String ReadRegex(String Message, String ErrorMessage, String Regex) {
        while (true) {
            System.out.print(Message);
            String input = scanner.nextLine().trim();

            if (input.matches(Regex)) {
                return input;
            }

            System.out.println(ErrorMessage);
        }
    }

    public static EnumSet<Discipline> ReadDisciplines() {
        EnumSet<Discipline> chosenDisciplines = EnumSet.noneOf(Discipline.class);

        while (true) {
            System.out.println("\nAvailable disciplines:\n");
            int index = 1;
            for (Discipline d : Discipline.values()) {
                System.out.println(index + ". " + d);
                index++;
            }

            System.out.print("\nEnter the number(s) for the disciplines (separated by commas): ");
            String line = scanner.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("You must choose at least one discipline.");
                continue;
            }

            String[] parts = line.split(",");
            chosenDisciplines.clear();
            boolean hasError = false;

            for (String part : parts) {
                String trimmed = part.trim();
                try {
                    int choice = Integer.parseInt(trimmed);
                    if (choice < 1 || choice > Discipline.values().length) {
                        System.out.println("Invalid discipline number: " + choice);
                        hasError = true;
                        break;
                    }
                    Discipline selected = Discipline.values()[choice - 1];
                    chosenDisciplines.add(selected);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input: '" + trimmed + "' is not a number.");
                    hasError = true;
                    break;
                }
            }

            if (!hasError && !chosenDisciplines.isEmpty()) {
                return chosenDisciplines;
            }

            System.out.println("Please try again and enter valid discipline numbers.");
        }
    }

    public static Discipline ReadSingleDiscipline(){
        Discipline chosenDiscipline;
        System.out.println("\nAvailable disciplines:\n");
        int index = 1;
        for (Discipline d : Discipline.values()) {
            System.out.println(index + ". " + d);
            index++;
        }

        while (true) {
            int number = ReadInteger("Choose discipline: (1-" +  Discipline.values().length + "): ", "Invalid input");
            if (number < 1 || number > Discipline.values().length) {
                System.out.println("Invalid input: '" + number + "' is not a disciplin.");
            }
            else {
                chosenDiscipline = Discipline.values()[number - 1];
                break;
            }
        }
        return chosenDiscipline;
    }

    // Read a line (can be empty – no validation)
    public static String ReadLine(String Message) {
        System.out.print(Message);
        return scanner.nextLine();
    }

    // Read with regex, but allow empty input (keep current)
    public static String ReadOptionalRegex(String Message, String ErrorMessage, String Regex) {
        while (true) {
            System.out.print(Message);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return "";
            }

            if (input.matches(Regex)) {
                return input;
            }

            System.out.println(ErrorMessage);
        }
    }


    public static void ClearLine() {
        if (scanner.hasNextLine())
            scanner.nextLine();
    }
}
