import ENUMS.Discipline;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Club
{
    private String name;
    private String adress;
    private int totalMembers;
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<Coach> coaches = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    private Cashier cashier = new Cashier();

    public Club(){}

    public Club(String name, String adress)
    {
        this.name = name;
        this.adress = adress;
        totalMembers = 0;
    }

    public String getName()
    {
        return name;
    }

    public String getAdress()
    {
        return adress;
    }

    public void addMember(Member member)
    {
        members.add(member);
        totalMembers++;
    }

    public void removeMember(Member member)
    {
        members.remove(member);
    }

    public ArrayList<Member> getMembers()
    {
        return members;
    }

    public void addCoach(Coach coach)
    {
        coaches.add(coach);
    }

    public void removeCoach(Coach coach)
    {
        coaches.remove(coach);
    }

    public ArrayList<Coach> getCoaches()
    {
        return coaches;
    }

    private boolean memberIdExists(int memberId, ArrayList<Member> members)
    {
        for (Member member : members)
        {
            if (member.getMemberId() == memberId)
            {
                return true;
            }
        }
        return false;
    }

    public void createMember()
    {
        System.out.print("\nPlease enter name of member: ");
        String name = scanner.next();
        while (!name.matches("[a-zA-ZæøåÆØÅ]+"))
        {
            System.out.print("\nName must contain only letters: ");
            name = scanner.next();
        }

        scanner.nextLine();

        System.out.print("\nPlease enter birthdate of member in the format YYYYMMDD: ");
        int bDay;
        while (true)
        {
            try
            {
                bDay = scanner.nextInt();
                Date date = new Date(bDay);
                if (date.valid())
                {
                    break;
                }
                else
                {
                    System.out.print("\nPlease enter a valid birthdate of member in the format YYYYMMDD: ");
                }
            }
            catch (InputMismatchException e)
            {
                System.out.print("\nPlease enter a valid birthdate of member in the format YYYYMMDD: ");
                scanner.nextLine();
            }
        }

        System.out.print("\nPlease enter email of member: ");
        String email = scanner.next();
        while (!email.matches("[a-zA-Z0-9æøåÆØÅ@.]+") || !email.contains("@") || !email.contains("."))
        {
            System.out.print("\nPlease enter valid email address: ");
            email = scanner.next();
        }

        System.out.print("\nPlease enter MemberID of member: ");
        int memberId = 0;
        try
        {
            memberId = scanner.nextInt();
            while (memberIdExists(memberId, members))
            {
                System.out.print("\nThis ID already exists. Enter another ID: ");
                memberId = scanner.nextInt();
            }
        }
        catch (InputMismatchException e)
        {
            System.out.print("\nInvalid ID, must only contain numbers");
        }

        System.out.println("\nAvailable coaches:");
        System.out.println(" ");
        for (int i = 0; i < coaches.size(); i++) {
            System.out.println((i + 1) + ". " + coaches.get(i).getName());
        }

        System.out.print("\nPlease enter the coach (number): ");

        int chooseCoach = 0;
        while (true)
        {
            try
            {
                chooseCoach = scanner.nextInt();

                if (chooseCoach >= 1 && chooseCoach <= coaches.size())
                {
                    break; // gyldigt - forlad loopet
                }
                else
                {
                    System.out.print("Invalid, Please enter a valid number for a coach: ");
                }
            }
            catch (InputMismatchException e)
            {
                System.out.print("Invalid, input must only contain numbers, please try again: ");
                scanner.nextLine();
            }
        }
        Coach chosenCoach = coaches.get(chooseCoach - 1);

        System.out.println("\nAvailable disciplines:");
        System.out.println(" ");
        int index = 1;
        for (Discipline d : Discipline.values())
        {
            System.out.println(index + ". " + d);
            index++;
        }
        System.out.print("\nEnter the number(s) for the disciplines (seperated by commas): ");
        String input = scanner.next();
        String[] parts = input.split(",");
        EnumSet<Discipline> chosenDisciplines = EnumSet.noneOf(Discipline.class);
        for (String part : parts)
        {
            try
            {
                int choice = Integer.parseInt(part.trim());
                Discipline selected = Discipline.values()[choice - 1];
                chosenDisciplines.add(selected);
            }
            catch (Exception e)
            {
                System.out.println("\nInvalid discipline");
            }
        }
        //Skal bruge mere test

        Member member = new Member(name, bDay, email, memberId, chosenCoach, chosenDisciplines);
        members.add(member);
    }

    public void editMember() {

        scanner.nextLine();
        for (int i = 0; i < members.size(); i++)
        {
            Member member = members.get(i);
            System.out.println("\nName: "+ member.getName() + ", MemberID: " + member.getMemberId());
        }
        System.out.print("\nEnter MemberID to edit: ");

        Member member = null;
        try
        {
            int id = scanner.nextInt();

            for (int i = 0; i < members.size(); i++)
            {
                if (members.get(i).getMemberId() == id)
                {
                    member = members.get(i);
                    break;
                }
                else
                {
                    System.out.print("Invalid MemberID, please try again: ");
                }
            }

        }
        catch (InputMismatchException e)
        {
            System.out.println("MemberID can only consist of numbers, please try again: ");
        }

        if (member == null) {
            System.out.println("No member found with that ID.");
            return;
        }

        System.out.println("\nEditing member: " + member.getName());
        scanner.nextLine();

        System.out.print("\nEnter new name (press Enter to keep '" + member.getName() + "'): ");
        String name = scanner.nextLine();

        if (!name.isBlank()) {
            while (!name.matches("[a-zA-ZæøåÆØÅ]+")) {
                System.out.print("Name must contain only letters: ");
                name = scanner.nextLine();
            }
            member.setName(name);
        }

        System.out.print("Enter new email (press Enter to keep '" + member.getEmail() + "'): ");
        String email = scanner.nextLine();

        if (!email.isBlank()) {
            while (!email.matches("[a-zA-Z0-9æøåÆØÅ@.]+") || !email.contains("@") || !email.contains(".")) {
                System.out.print("Please enter valid email address: ");
                email = scanner.nextLine();
            }
            member.setEmail(email);
        }

        System.out.println("Available coaches:");
        for (int i = 0; i < coaches.size(); i++) {
            System.out.println((i + 1) + ". " + coaches.get(i).getName());
        }

        System.out.print("Choose new coach (0 = keep current): ");
        int chooseCoach = scanner.nextInt();

        if (chooseCoach > 0 && chooseCoach <= coaches.size()) {
            member.setCoach(coaches.get(chooseCoach - 1));
        }

        System.out.println("Available disciplines:");

        for (int i = 0; i < Discipline.values().length; i++) {
            System.out.println((i + 1) + ". " + Discipline.values()[i]);
        }

        System.out.print("Enter new disciplines (comma separated) or 0 to keep current: ");
        String input = scanner.next();

        if (!input.equals("0")) {
            String[] parts = input.split(",");
            EnumSet<Discipline> newDisciplines = EnumSet.noneOf(Discipline.class);

            for (int i = 0; i < parts.length; i++) {
                try {
                    int choice = Integer.parseInt(parts[i].trim());
                    Discipline selected = Discipline.values()[choice - 1];
                    newDisciplines.add(selected);
                } catch (Exception e) {
                    System.out.println("Invalid discipline");
                }
            }

            member.setDisciplines(newDisciplines);
        }

        System.out.println("Member updated.");

        cashier.registerMember(member);
    }

    public void paymentOverview(){
        System.out.println("\n ----PAYMENT OVERVIEW----");

        System.out.println("\n --Members and thier fees--");
        for (Member m : members)
        {
            Payment p = cashier.getPayment(m);
            System.out.println(m.getName() + "(ID: " + m.getMemberId() + " ), Fee: " + p.getAmount() + " kr., Paid: " + p.isPaid());
        }

        System.out.println("\n --Members in debt--");
        boolean anyInDebt = false;

        for (Member m : members)
        {
            if (cashier.isInDebt(m))
            {
                System.out.println("- " + m.getName() + " (ID: " + m.getMemberId() + ")");
                anyInDebt = true;
            }
        }

        if(!anyInDebt)
        {
            System.out.println("No member are in debt");
        }
    }
}
