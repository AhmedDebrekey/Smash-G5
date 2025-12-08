import ENUMS.Discipline;

import java.io.FileWriter;
import java.io.IOException;

public class AutoSaveListener implements DataChangeListener{
    private final Club club;
    private final String fileName;

    public AutoSaveListener(Club club, String fileName) {
        this.club = club;
        this.fileName = fileName;
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
                        member.isActive() + ";" +
                        member.getResult(Discipline.SINGLE) + ";" +
                        member.getResult(Discipline.DOUBLE) + ";" +
                        member.getResult(Discipline.MIXED_DOUBLE) + ";\n");
            }
        }
        catch (IOException e)
        {
            System.out.println("There was error writing to the file: " + e.getMessage());
        }
    }
}
