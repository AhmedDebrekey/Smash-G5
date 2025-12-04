import ENUMS.AgeGroup;
import ENUMS.Discipline;

import java.util.EnumSet;

public class Member extends Person
{
    private int memberId;
    private Coach coach;
    private boolean active;
    private EnumSet<Discipline> disciplines;
    private boolean competitive;

    public Member(){}

    public Member(String name, int bDay, String email, int memberId, Coach coach, EnumSet<Discipline> disciplines)
    {
        super (name, bDay, email);
        this.memberId = memberId;
        this.coach = coach;
        active = true;
        this.disciplines = disciplines;
        competitive = true;
    }

    public int getMemberId()
    {
        return memberId;
    }

    public Coach getCoach()
    {
        return coach;
    }

    public void active()
    {
        active = true;
    }

    public void passive()
    {
        active = false;
    }

    public boolean isActive()
    {
        return active;
    }

    public EnumSet<Discipline> getdiscipline()
    {
        return disciplines;
    }

    public AgeGroup getAgeGroup()
    {
        if (bDay.getAge() >= 18)
            return AgeGroup.SENIOR;
        else
            return AgeGroup.JUNIOR;
    }

    public void competitive()
    {
        competitive = true;
    }

    public void motionist()
    {
        competitive = false;
    }

    public boolean isCompetitive()
    {
        return competitive;
    }

}
