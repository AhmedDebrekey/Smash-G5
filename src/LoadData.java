import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import ENUMS.Discipline;

public class LoadData {

    private static class RawMember {
        String name = "";
        int birth = 0;
        String email = "";
        int memberId = 0;
        String coachName = "";
        boolean competitive = false;
        EnumSet<Discipline> disciplines = EnumSet.noneOf(Discipline.class);
        boolean hasPayment = false;
        int paymentAmount = 0;
        int paymentDateInt = 0;
        boolean paymentPaid = false;
        Date registrationDate = null;
    }

    private static class RawMatch {
        String matchName = "";
        Discipline discipline = null;
        boolean isTournament = false;
        int matchDateInt = 0;
        int teamOneScore = 0;
        int teamTwoScore = 0;
        String teamOneIds = "";
        String teamTwoIds = "";
    }

    public static void loadFromFile(String filename, Club club, Cashier cashier) {
        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("No save file found — starting with empty club.");
            return;
        }

        List<RawMember> rawMembers = new ArrayList<>();
        List<RawMatch> rawMatches = new ArrayList<>();
        int coachesParsed = 0;
        int matchesParsed = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))) {
            String line;
            int section = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String low = line.toLowerCase();

                if (low.startsWith("[member]")) { section = 1; continue; }
                if (low.startsWith("[coach]"))  { section = 2; continue; }
                if (low.startsWith("[match]"))  { section = 3; continue; }

                String[] parts = line.split(";");
                for (int i = 0; i < parts.length; i++) parts[i] = parts[i].trim();

                switch (section) {
                    case 1 -> {
                        RawMember rm = new RawMember();
                        rm.name = parts.length > 0 ? parts[0] : "";
                        rm.birth = parts.length > 1 ? parseIntSafe(parts[1]) : 0;
                        rm.email = parts.length > 2 ? parts[2] : "";
                        rm.memberId = parts.length > 3 ? parseIntSafe(parts[3]) : 0;
                        rm.coachName = parts.length > 4 ? parts[4] : "";

                        if (parts.length > 5) {
                            String discRaw = parts[5].replace("[", "").replace("]", "").trim();
                            if (!discRaw.isEmpty()) {
                                for (String d : discRaw.split(",")) {
                                    String trimmed = d.trim();
                                    if (!trimmed.isEmpty()) {
                                        try { rm.disciplines.add(Discipline.valueOf(trimmed)); } catch (Exception ignored) {}
                                    }
                                }
                            }
                        }

                        if (parts.length > 6) rm.competitive = Boolean.parseBoolean(parts[6]);

                        if (parts.length >= 13) {
                            String maybeAmount = parts[parts.length - 3].trim();
                            String maybeDate = parts[parts.length - 2].trim();
                            rm.registrationDate = new Date(Integer.parseInt(maybeDate));
                            String maybePaid = parts[parts.length - 1].trim();
                            int amt = parseIntSafe(maybeAmount);
                            int d = parseIntSafe(maybeDate);
                            boolean paid = "true".equalsIgnoreCase(maybePaid) || "1".equals(maybePaid);
                            if (amt > 0) {
                                rm.hasPayment = true;
                                rm.paymentAmount = amt;
                                rm.paymentDateInt = d;
                                rm.paymentPaid = paid;
                            }
                        }

                        rawMembers.add(rm);
                    }

                    case 2 -> {
                        String name = parts.length > 0 ? parts[0] : "";
                        int birth = parts.length > 1 ? parseIntSafe(parts[1]) : 0;
                        String email = parts.length > 2 ? parts[2] : "";
                        Coach coach = new Coach(name, birth, email);
                        if (club.getCoaches() instanceof Collection) ((Collection) club.getCoaches()).add(coach);
                        coachesParsed++;
                    }

                    case 3 -> {
                        RawMatch rm = new RawMatch();
                        rm.matchName = parts.length > 0 ? parts[0] : "";
                        try {
                            if (parts.length > 1 && !parts[1].isEmpty()) rm.discipline = Discipline.valueOf(parts[1]);
                        } catch (Exception ignored) {}
                        rm.isTournament = parts.length > 2 && Boolean.parseBoolean(parts[2]);
                        rm.matchDateInt = parts.length > 3 ? parseIntSafe(parts[3]) : 0;
                        rm.teamOneScore = parts.length > 4 ? parseIntSafe(parts[4]) : 0;
                        rm.teamTwoScore = parts.length > 5 ? parseIntSafe(parts[5]) : 0;
                        rm.teamOneIds = parts.length > 6 ? parts[6] : "";
                        rm.teamTwoIds = parts.length > 7 ? parts[7] : "";
                        rawMatches.add(rm);
                        matchesParsed++;
                    }
                }
            }

            int membersAdded = 0;

            for (RawMember rm : rawMembers) {
                Coach coachObj = null;
                if (rm.coachName != null && !rm.coachName.isBlank()) {
                    Object coachesObj = club.getCoaches();
                    if (coachesObj instanceof Collection) {
                        for (Object o : (Collection<?>) coachesObj) {
                            try {
                                Coach c = (Coach) o;
                                if (c != null && c.getName() != null && c.getName().equalsIgnoreCase(rm.coachName.trim())) {
                                    coachObj = c;
                                    break;
                                }
                            } catch (Exception ignore) {}
                        }
                    }
                }

                Member member = new Member(rm.name, rm.birth, rm.email, rm.memberId, coachObj, rm.disciplines, rm.competitive, rm.registrationDate);
                if (club.getMembers() instanceof Collection) club.addMember(member);
                membersAdded++;
            }

            for (RawMember rm : rawMembers) {
                if (!rm.hasPayment) continue;
                Member found = null;
                Object membersObj = club.getMembers();
                if (membersObj instanceof Collection) {
                    for (Object o : (Collection<?>) membersObj) {
                        try {
                            Member m = (Member) o;
                            if (m != null && m.getMemberId() == rm.memberId) {
                                found = m;
                                break;
                            }
                        } catch (Exception ignore) {}
                    }
                }
                if (found == null) continue;
                Payment p = new Payment(rm.paymentDateInt, rm.paymentAmount);
                p.setPaid(rm.paymentPaid);
                try { cashier.setPaymentForMember(found, p); } catch (Throwable t) {}
            }

            for (RawMatch rmatch : rawMatches) {
                Discipline disc = rmatch.discipline;
                boolean isTournament = rmatch.isTournament;
                int dateInt = rmatch.matchDateInt;
                int t1score = rmatch.teamOneScore;
                int t2score = rmatch.teamTwoScore;

                List<Member> teamOne = new ArrayList<>();
                List<Member> teamTwo = new ArrayList<>();

                if (rmatch.teamOneIds != null && !rmatch.teamOneIds.isBlank()) {
                    String[] ids = rmatch.teamOneIds.split(",");
                    for (String idS : ids) {
                        int id = parseIntSafe(idS.trim());
                        if (id == 0) continue;
                        Member m = club.findMemberByID(id);
                        if (m != null) teamOne.add(m);
                    }
                }

                if (rmatch.teamTwoIds != null && !rmatch.teamTwoIds.isBlank()) {
                    String[] ids = rmatch.teamTwoIds.split(",");
                    for (String idS : ids) {
                        int id = parseIntSafe(idS.trim());
                        if (id == 0) continue;
                        Member m = club.findMemberByID(id);
                        if (m != null) teamTwo.add(m);
                    }
                }

                Match match = new Match(teamOne, teamTwo, disc, isTournament, t1score, t2score, dateInt);

                boolean addedViaMethod = false;
                try {
                    java.lang.reflect.Method addMatchMethod = club.getClass().getMethod("addMatch", Match.class);
                    addMatchMethod.invoke(club, match);
                    addedViaMethod = true;
                } catch (NoSuchMethodException nsme) {
                    try {
                        java.lang.reflect.Method addMatchMethod2 = club.getClass().getMethod("registerMatch", Match.class);
                        addMatchMethod2.invoke(club, match);
                        addedViaMethod = true;
                    } catch (NoSuchMethodException nsme2) {
                        // fallback
                    } catch (Throwable ignore) {}
                } catch (Throwable ignore) {}

                try {
                    Object matchesObj = club.getMatches();
                    if (matchesObj instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<Match, String> map = (Map<Match, String>) matchesObj;
                        map.put(match, rmatch.matchName);
                    } else if (matchesObj instanceof Collection) {
                        @SuppressWarnings("unchecked")
                        Collection<Match> coll = (Collection<Match>) matchesObj;
                        coll.add(match);
                        try {
                            if (club.getMatches() instanceof Map) {
                                @SuppressWarnings("unchecked")
                                Map<Match, String> map2 = (Map<Match, String>) club.getMatches();
                                map2.put(match, rmatch.matchName);
                            }
                        } catch (Throwable ignore) {}
                    }
                } catch (Exception ignored) {}

                try { match.updateMatchPlayersScore(); } catch (Throwable ignore) {}
                try { club.notifyDataChanged(); } catch (Throwable ignore) {}
            }

            System.out.println("Loaded: " + membersAdded + " members, " + matchesParsed + " matches.");
        } catch (Exception e) {
            System.out.println("Error loading save data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}
