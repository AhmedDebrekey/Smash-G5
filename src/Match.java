import ENUMS.Discipline;

import java.util.List;

public class Match {
    private List<Member> teamOne;
    private List<Member> teamTwo;
    private Discipline discipline;
    private boolean isTournament;
    private Date matchDate;
    private int teamOneScore;
    private int teamTwoScore;

    public Match(List<Member> teamOne, List<Member> teamTwo, Discipline discipline, boolean isTournament, int teamOneScore, int teamTwoScore, int matchDate) {
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.discipline = discipline;
        this.matchDate = new Date(matchDate);
        this.isTournament = isTournament;
        this.teamOneScore = teamOneScore;
        this.teamTwoScore = teamTwoScore;
    }

    public List<Member> getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(List<Member> teamOne) {
        this.teamOne = teamOne;
    }

    public List<Member> getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(List<Member> teamTwo) {
        this.teamTwo = teamTwo;
    }

    public Discipline getDiscipline() {
        return discipline;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    public boolean isTournament() {
        return isTournament;
    }

    public void setTournament(boolean tournament) {
        isTournament = tournament;
    }

    public int getTeamOneScore() {
        return teamOneScore;
    }

    public void setTeamOneScore(int teamOneScore) {
        this.teamOneScore = teamOneScore;
    }

    public int getTeamTwoScore() {
        return teamTwoScore;
    }

    public void setTeamTwoScore(int teamTwoScore) {
        this.teamTwoScore = teamTwoScore;
    }

    // compare to the list in Register tournament to see which won.
    public void showWinners()
    {
        System.out.println("Winners are:");
        if (teamOneScore > teamTwoScore) {
            for (Member player : teamOne)
            {
                System.out.println(player.getName() + "\n");
            }
        }
        else {
            for (Member player : teamTwo)
            {
                System.out.println(player.getName() + "\n");
            }
        }
    }

    public void updateMatchPlayersScore(){
        for (Member player : teamOne)
        {
            player.addResult(discipline, player.getResult(discipline) + teamOneScore);
        }

        for (Member player : teamTwo)
        {
            player.addResult(discipline, player.getResult(discipline) + teamTwoScore);
        }
    }

    public Date getMatchDate()
    {
        return matchDate;
    }
}
