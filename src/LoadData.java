import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import ENUMS.Discipline;

public class LoadData
{

    private static class RawMember
    {
        String name = "";
        int birth = 0;
        String email = "";
        int memberId = 0;
        String coachName = "";
        boolean competitive = false;
        EnumSet<Discipline> disciplines = EnumSet.noneOf(Discipline.class);
    }

    public static void loadFromFile(String filename, Club club)
    {

        File f = new File(filename);
        if (!f.exists())
        {
            System.out.println("No save file found — starting with empty club.");
            return;
        }

        List<RawMember> rawMembers = new ArrayList<>();
        int coachesParsed = 0;
        int matchesParsed = 0;


        try
        {
            if (club.getMembers() instanceof Collection)
                ((Collection<?>) club.getMembers()).clear();
            if (club.getCoaches() instanceof Collection)
                ((Collection<?>) club.getCoaches()).clear();
            if (club.getMatches() instanceof Collection)
                ((Collection<?>) club.getMatches()).clear();
        } catch (Exception ignored) {}

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8)))
        {

            String line;
            int section = 0; // 1 = Member, 2 = Coach, 3 = Match

            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.isEmpty()) continue;

                String low = line.toLowerCase();


                if (low.startsWith("[member]")) { section = 1; continue; }
                if (low.startsWith("[coach]"))  { section = 2; continue; }
                if (low.startsWith("[match]"))  { section = 3; continue; }

                String[] parts = line.split(";");
                for (int i = 0; i < parts.length; i++)
                    parts[i] = parts[i].trim();

                switch (section)
                {
                    case 1 ->
                    {
                        RawMember rm = new RawMember();
                        rm.name = parts.length > 0 ? parts[0] : "";
                        rm.birth = parts.length > 1 ? parseIntSafe(parts[1]) : 0;
                        rm.email = parts.length > 2 ? parts[2] : "";
                        rm.memberId = parts.length > 3 ? parseIntSafe(parts[3]) : 0;
                        rm.coachName = parts.length > 4 ? parts[4] : "";


                        if (parts.length > 5)
                        {
                            String discRaw = parts[5].replace("[", "").replace("]", "").trim();
                            if (!discRaw.isEmpty())
                            {
                                for (String d : discRaw.split(","))
                                {
                                    String trimmed = d.trim();
                                    if (!trimmed.isEmpty())
                                    {
                                        try
                                        {
                                            rm.disciplines.add(Discipline.valueOf(trimmed));
                                        }
                                        catch (Exception ignored) {}
                                    }
                                }
                            }
                        }
                        if (parts.length > 6)
                            rm.competitive = Boolean.parseBoolean(parts[6]);
                        rawMembers.add(rm);
                    }

                    case 2 ->
                    {
                        String name = parts.length > 0 ? parts[0] : "";
                        int birth = parts.length > 1 ? parseIntSafe(parts[1]) : 0;
                        String email = parts.length > 2 ? parts[2] : "";

                        Coach coach = new Coach(name, birth, email);

                        if (club.getCoaches() instanceof Collection)
                            ((Collection) club.getCoaches()).add(coach);

                        coachesParsed++;
                    }
                }
            }
            int membersAdded = 0;

            for (RawMember rm : rawMembers)
            {
                Coach coachObj = null;
                if (coachObj == null)
                {
                    coachObj = new Coach("NoCoach", 0, "");
                    if (club.getCoaches() instanceof Collection)
                    {
                        ((Collection) club.getCoaches()).add(coachObj);
                    }
                }


                Member member = new Member(
                        rm.name,
                        rm.birth,
                        rm.email,
                        rm.memberId,
                        coachObj,
                        rm.disciplines,
                        rm.competitive
                );


                try { member.setCompetitive(rm.competitive); } catch (Exception ignored) {}

                if (club.getMembers() instanceof Collection)
                    ((Collection) club.getMembers()).add(member);

                membersAdded++;
            }


            System.out.println("In Smash: " + membersAdded + " members... ");

        } catch (Exception e)
        {
            System.out.println("Error loading save data: " + e.getMessage());
        }
    }

    private static int parseIntSafe(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch (Exception e)
        {
            return 0;
        }
    }
}
