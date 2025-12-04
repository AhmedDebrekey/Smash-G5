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
        System.out.print("Please enter name of member: ");
        String name = scanner.next();
        while (!name.matches("[a-zA-ZæøåÆØÅ]+"))
        {
            System.out.print("Name must contain only letters: ");
            name = scanner.next();
        }

        scanner.nextLine();

        System.out.print("Please enter birthdate of member in the format YYYYMMDD: ");
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
                    System.out.print("Please enter a valid birthdate of member in the format YYYYMMDD: ");
                }
            }
            catch (InputMismatchException e)
            {
                System.out.print("Please enter a valid birthdate of member in the format YYYYMMDD: ");
                scanner.nextLine();
            }
        }

        System.out.print("Please enter email of member: ");
        String email = scanner.next();
        while (!email.matches("[a-zA-Z0-9æøåÆØÅ@.]+") || !email.contains("@") || !email.contains("."))
        {
            System.out.print("Please enter valid email address: ");
            email = scanner.next();
        }

        System.out.print("Please enter MemberID of member: ");
        int memberId = 0;
        try
        {
            memberId = scanner.nextInt();
            while (memberIdExists(memberId, members))
            {
                System.out.print("This ID already exists. Enter another ID: ");
                memberId = scanner.nextInt();
            }
        }
        catch (InputMismatchException e)
        {
            System.out.print("Invalid ID, must only contain numbers");
        }

        System.out.println("Available coaches:");
        for (int i = 0; i < coaches.size(); i++)
        {
            Coach coach = coaches.get(i);
            System.out.println((i + 1) + ". " + coach.getName());
        }
        System.out.print("Please enter the coach: ");
        int chooseCoach = scanner.nextInt();
        System.out.println();
        Coach chosenCoach = coaches.get(chooseCoach - 1);

        System.out.println("Available disciplines:");
        int index = 1;
        for (Discipline d : Discipline.values())
        {
            System.out.println(index + ". " + d);
            index++;
        }
        System.out.print("Enter the number(s) for the disciplines (seperated by commas): ");
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
                System.out.println("Invalid discipline");
            }
        }

        Member member = new Member(name, bDay, email, memberId, chosenCoach, chosenDisciplines);
        members.add(member);
    }
}
