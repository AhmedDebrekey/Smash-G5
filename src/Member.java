import ENUMS.AgeGroup;
import ENUMS.Discipline;

import java.util.EnumMap;
import java.util.EnumSet;

public class Member extends Person implements Comparable<Member> {
    private int memberId;
    private Coach coach;
    private boolean active;
    private Date registrationDate;
    private EnumSet<Discipline> disciplines;

    private boolean competitive;
    private EnumMap<Discipline, Integer> results;

    public Member() {
    }

    public Member(String name, int bDay, String email, int memberId, Coach coach, EnumSet<Discipline> disciplines, boolean competitive) {
        super(name, bDay, email);
        this.memberId = memberId;
        this.active = true;
        this.disciplines = disciplines;
        this.competitive = competitive;

        if (coach != null)
        {
            this.coach = coach;
        }
        else
        {
            this.coach = null;
        }

        this.results = new EnumMap<>(Discipline.class);

        for (Discipline d : disciplines) {
            this.results.put(d, 0);
        }

        java.time.LocalDate today = java.time.LocalDate.now();
        int fullDate = today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth();
        this.registrationDate = new Date(fullDate);
    }

    public int getMemberId() {
        return memberId;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setToActive() {
        active = true;
    }

    public void setToPassive() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public EnumSet<Discipline> getdiscipline() {
        return disciplines;
    }

    public AgeGroup getAgeGroup() {
        if (bDay.getAge() >= 18)
            return AgeGroup.SENIOR;
        else
            return AgeGroup.JUNIOR;
    }

    public void setCompetitive(boolean competitive) {
        this.competitive = competitive;
    }

    public boolean isCompetitive() {
        return competitive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthdate(int bDay) {
        this.bDay = new Date(bDay);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public EnumSet<Discipline> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(EnumSet<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public void addResult(Discipline discipline, int value) {
        if (!disciplines.contains(discipline)) {
            System.out.println("Member is not registered for discipline " + discipline);
            return;
        }

        results.put(discipline, value);
    }

    public int getResult(Discipline discipline) {
        return results.getOrDefault(discipline, 0);
    }

    public Date getRegistrationDate()
    {
        return registrationDate;
    }

    @Override
    public int compareTo(Member o) {
        return Integer.compare(this.getMemberId(), o.getMemberId());
    }
}