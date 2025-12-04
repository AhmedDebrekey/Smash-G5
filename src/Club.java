import java.util.ArrayList;

public class Club
{
    private String name;
    private String adress;
    private int totalMembers;
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<Coach> coaches = new ArrayList<>();

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
}
