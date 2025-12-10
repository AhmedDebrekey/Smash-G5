import ENUMS.Discipline;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class AutoSaveListener implements DataChangeListener{
    private final Club club;
    private final String fileName;
    private final Cashier cashier;


    public AutoSaveListener(Club club, String fileName, Cashier cashier) {
        this.club = club;
        this.fileName = fileName;
        this.cashier = cashier;
    }

    @Override
    public void onDataChanged()
    {
        try (FileWriter writer = new FileWriter(fileName))
        {
            writer.write("[Smash]\n");
            writer.write(club.getName() + ";" + club.getAdress() + "\n\n");

            for (int i = 0; i < club.getMembers().size(); i++)
            {
                Member member = club.getMembers().get(i);
                writer.write("[Member]\n");
                writer.write(member.getName() + ";" +
                        member.getbDay().getDate() + ";" +
                        member.getEmail() + ";" +
                        member.getMemberId() + ";" +
                        member.getCoach().getName() + ";" +
                        member.getDisciplines() + ";" +
                        member.isCompetitive() + ";" +
                        member.getRegistrationDate().getDate()+ ";" +
                        member.isActive() + ";" +
                        member.getResult(Discipline.SINGLE) + ";" +
                        member.getResult(Discipline.DOUBLE) + ";" +
                        member.getResult(Discipline.MIXED_DOUBLE) + ";");
                        Payment p = cashier.getPayment(member);
                        if (p != null)
                        {
                            writer.write(p.getAmount() + ";" +
                                p.getDate().getDate() + ";" +
                                p.isPaid() + ";\n\n");
                        }
            }
            //Name;bDay;email;memberId;coachname;disciplines;competetiveornot;RegistrationDate;activeornot;
            //SingleResults;DoubleResults;MixedDoubleResults;paymentAmount;paymentYear;PaymentStatus;

            for (Match match : club.getMatches().keySet()) {
                String matchName = club.getMatches().get(match);

                String teamOneIDs = "";
                for (Member m : match.getTeamOne()) {
                    teamOneIDs += m.getMemberId() + ","; // bare string + string
                }
                if (!teamOneIDs.isEmpty())
                    teamOneIDs = teamOneIDs.substring(0, teamOneIDs.length() - 1);

                String teamTwoIDs = "";
                for (Member m : match.getTeamTwo()) {
                    teamTwoIDs += m.getMemberId() + ",";
                }
                if (!teamTwoIDs.isEmpty())
                    teamTwoIDs = teamTwoIDs.substring(0, teamTwoIDs.length() - 1);

                writer.write("[Match]\n");
                writer.write(matchName + ";" + match.getDiscipline() + ";" +
                        match.isTournament() + ";" +
                        match.getMatchDate().getDate() + ";" +
                        match.getTeamOneScore() + ";" +
                        match.getTeamTwoScore() + ";" +
                        teamOneIDs + ";" +
                        teamTwoIDs + ";\n\n"
                );
            }
        }
        catch (IOException e)
        {
            System.out.println("There was error writing to the file: " + e.getMessage());
        }
    }
}
