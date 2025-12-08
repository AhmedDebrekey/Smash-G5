import ENUMS.Discipline;

import java.util.*;

public class Club
{
    private String name;
    private String adress;
    private int totalMembers;
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<Coach> coaches = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);
    private Cashier cashier = new Cashier();
    private Map<Match, String> matches = new HashMap<>();
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
        String name = InputHelpers.ReadRegex(
            "\nPlease enter name of member: ",
            "Name must contain only letters.",
            "[a-zA-ZæøåÆØÅ ]+"
        );

        //Må kun indeholde 8 cifre.
        //lave som String.
        int bDay = InputHelpers.ReadDate(
                "\nPlease enter birthdate of member in the format YYYYMMDD: ",
                "Invalid Date use YYYYMMDD.");

        InputHelpers.ClearLine();

        String email = InputHelpers.ReadRegex(
                "\nPlease enter email: ",
                "Invalid email format.",
                "[a-zA-Z0-9._%+-æøåÆØÅ]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        );

        int memberId;
        while (true) {
            memberId = InputHelpers.ReadInteger(
                    "\nPlease enter member ID: ",
                    "Invalid ID, must only contain numbers."
            );

            if (!memberIdExists(memberId, members)) {
                break;
            }

            System.out.print("\nThis ID already exists. Enter another ID.\n");
        }

        System.out.println("\nAvailable coaches:");
        System.out.println(" ");
        for (int i = 0; i < coaches.size(); i++) {
            System.out.println((i + 1) + ". " + coaches.get(i).getName());
        }

        int chooseCoach = InputHelpers.ReadIntInRange(
                "Choose a coach (1-" + coaches.size() + "): ",
                "Invalid input, must only contain numbers. Please try again.",
                "Invalid, please enter a valid number for a coach.",
                1,
                coaches.size()
        );

        Coach chosenCoach = coaches.get(chooseCoach - 1);

        EnumSet<Discipline> chosenDisciplines = InputHelpers.ReadDisciplines();

        boolean isCompetitive = InputHelpers.ReadYesOrNo(
                "\nEnter if the player is competitive [y/n]: ",
            "Invalid Input only use 'y' or 'n'");


        Member member = new Member(name, bDay, email, memberId, chosenCoach, chosenDisciplines, isCompetitive);
        members.add(member);
        cashier.registerMember(member);

    }

    private void showMembers() {
        for (Member member : members) {
            System.out.println("\nName: "+ member.getName() + ", MemberID: " + member.getMemberId());
        }
    }

    private Member findMemberByID() {
        while (true) {
            int id = InputHelpers.ReadInteger(
                    "\nEnter MemberID: ",
                    "MemberID must only contain numbers. Try again."
            );

            for (Member m : members) {
                if (m.getMemberId() == id) {
                    return m; // Found!
                }
            }

            System.out.println("No member found with that ID. Please try again.");
        }
    }

    private Member findMemberByID(int memberID){
        for (Member m : members)
        {
            if (m.getMemberId() == memberID)
            {
                    return m;
            }
        }
        System.out.println("No members in the club ahah losers...");
        return null;
    }



    public void editMember() {
        showMembers();

        Member member = findMemberByID();

        if (member == null) {
            System.out.println("No member found with that ID.");
            return;
        }

        System.out.println("\nEditing member: " + member.getName());

        // ---- NAME ----
        String nameInput = InputHelpers.ReadOptionalRegex(
                "\nEnter new name (press Enter to keep '" + member.getName() + "'): ",
                "Name must contain only letters.",
                "[a-zA-ZæøåÆØÅ ]+"   // allow Danish letters & spaces
        );

        if (!nameInput.isBlank()) {
            member.setName(nameInput);
        }

        // ---- EMAIL ----
        String emailInput = InputHelpers.ReadOptionalRegex(
                "Enter new email (press Enter to keep '" + member.getEmail() + "'): ",
                "Please enter a valid email address.",
                "[a-zA-Z0-9._%+-æøåÆØÅ]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        );

        if (!emailInput.isBlank()) {
            member.setEmail(emailInput);
        }

        // ---- COACH ----
        System.out.println("\nAvailable coaches:");
        for (int i = 0; i < coaches.size(); i++) {
            System.out.println((i + 1) + ". " + coaches.get(i).getName());
        }

        int chooseCoach = InputHelpers.ReadIntInRange(
                "Choose new coach (0 = keep current): ",
                "Invalid input, must only contain numbers. Please try again.",
                "Invalid, please enter a number between 0 and " + coaches.size() + ".",
                0,
                coaches.size()
        );

        if (chooseCoach > 0) {
            member.setCoach(coaches.get(chooseCoach - 1));
        }

        // ---- DISCIPLINES ----
        System.out.println("\nAvailable disciplines:");
        for (int i = 0; i < Discipline.values().length; i++) {
            System.out.println((i + 1) + ". " + Discipline.values()[i]);
        }

        // we want: 0 = keep current, otherwise comma separated list
        String input = InputHelpers.ReadLine(
                "Enter new disciplines (comma separated) or 0 to keep current: "
        ).trim();

        if (!input.equals("0") && !input.isEmpty()) {
            String[] parts = input.split(",");
            EnumSet<Discipline> newDisciplines = EnumSet.noneOf(Discipline.class);

            for (String part : parts) {
                try {
                    int choice = Integer.parseInt(part.trim());
                    if (choice < 1 || choice > Discipline.values().length) {
                        System.out.println("Invalid discipline number: " + choice);
                        continue;
                    }
                    Discipline selected = Discipline.values()[choice - 1];
                    newDisciplines.add(selected);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid discipline: '" + part.trim() + "'");
                }
            }

            if (!newDisciplines.isEmpty()) {
                member.setDisciplines(newDisciplines);
            }
        }

        boolean isCompetitive = InputHelpers.ReadYesOrNo(
                "\nEnter if the player is competitive [y/n]: ",
                "Invalid Input only use 'y' or 'n'");
        member.setCompetitive(isCompetitive);

        System.out.println("\nMember updated.");
        cashier.registerMember(member);
    }


    public void paymentOverview(){
        System.out.println("\n ----PAYMENT OVERVIEW----");

        System.out.println("\n --Members and their fees--");
        for (Member m : members)
        {
            Payment p = cashier.getPayment(m);
            System.out.println(m.getName() + " (ID: " + m.getMemberId() + " ), Fee: " + p.getAmount() + " kr., Paid: " + p.isPaid());
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
            System.out.println("No members are in debt");
        }
    }

    public void registerTournamentResults (){
        showMembers();
        Member member = findMemberByID();
        if (member == null) {
            System.out.println("No member found with that ID.");
            return;
        }
        if (!member.isCompetitive()) {
            System.out.println("Member " + member.getName() + " is not a competitive player.");
            return;
        }
        EnumSet<Discipline> memberDisciplines = member.getDisciplines();

        if (memberDisciplines.isEmpty()) {
            System.out.println("Member is not registered for any disciplines.");
            return;
        }

        System.out.println("\nSelect discipline:");
        List<Discipline> disciplineList = new ArrayList<>(memberDisciplines);
        for (int i = 0; i < disciplineList.size(); i++) {
            System.out.println((i + 1) + ". " + disciplineList.get(i));
        }

        int choice;
        while (true) {
            System.out.println("Enter choice (1-" + disciplineList.size() + "): ");
            choice = scanner.nextInt();
            if (choice >= 1 && choice <= disciplineList.size()) {
                break;
            } else {
                System.out.println("Please choose a valid number between 1 and " + disciplineList.size());
            }
        }

        Discipline chosenDiscipline = disciplineList.get(choice - 1);
        System.out.println("Enter Result");
        int result = scanner.nextInt();
        member.addResult(chosenDiscipline, result);

        System.out.println("Result registered: " + member.getName() +
                " now has " + member.getResult(chosenDiscipline) +
                " in " + chosenDiscipline + ".");
    }

    public void seeRankings(){
        for (Discipline d : Discipline.values())
        {
            List<Member> playersInDiscipline = new ArrayList<>();

            for (Member m : members)
            {
                if (m.isCompetitive() && m.getdiscipline().contains(d))
                {
                    playersInDiscipline.add(m);
                }
            }

            playersInDiscipline.sort(new Comparator<Member>() {
                @Override
                public int compare(Member m1, Member m2) {
                    // sort descending (highest score first)
                    return Integer.compare(
                            m2.getResult(d),
                            m1.getResult(d)
                    );
                }
            });

            System.out.println("\nRanking for discipline: " + d);

            for (int i = 0; i < playersInDiscipline.size(); i++) {
                Member m = playersInDiscipline.get(i);
                System.out.println((i + 1) + ". " + m.getName() + " - " + m.getResult(d) + " points");
            }

            if (playersInDiscipline.isEmpty()) {
                System.out.println("No competitive players in this discipline yet.");
            }
        }
    }

    public void registerTournament() {
        List<Member> teamOne = new ArrayList<>();
        List<Member> teamTwo = new ArrayList<>();
        // ---- Name the Match ----
        String nameOfMatch = InputHelpers.ReadString("Enter the name of the match: ", "Enter a valid name");
        //--- Date of Match ---
        int date = InputHelpers.ReadDate(
                "\nPlease enter the date of the match in the format 'YYYYMMDD': ",
                "Invalid Date use YYYYMMDD.");
        InputHelpers.ClearLine();
        // ---- Choose Disciplin ----
        Discipline chosenDisciplin = InputHelpers.ReadSingleDiscipline();
        int teamOnePlayers;
        int teamTwoPlayers;
        if (chosenDisciplin == Discipline.SINGLE)
        // ---- Adding players ----
        {
            teamOnePlayers = 1;
            teamTwoPlayers = 1;
        }
        else
        {
            teamOnePlayers = 2;
            teamTwoPlayers = 2;
        }
        showMembers();
        for (int i = 0; i < teamOnePlayers; i++) {
            int playerID;
            while (true) {
                playerID = InputHelpers.ReadInteger(
                        "\nPlease enter member ID player nr. " + (i+1) + " in team one: ",
                        "Invalid ID, must only contain numbers."
                );

                if (memberIdExists(playerID, members)) {
                    break;
                }
                else{
                    System.out.println("Player ID does not exist.");
                }

            }
            Member player = findMemberByID(playerID);
            if (player != null){
                teamOne.add(player);
            }
        }




        showMembers();
        for (int i = 0; i < teamTwoPlayers; i++) {
            int playerID;
            while (true) {
                playerID = InputHelpers.ReadInteger(
                        "\nPlease enter member ID player nr. " + (i+1) + " in team two: ",
                        "Invalid ID, must only contain numbers."
                );
                if (memberIdExists(playerID, members) && !memberIdExists(playerID, (ArrayList<Member>) teamOne)) {
                    break;
                }
                else{
                    System.out.println("Player ID does not exist.");
                }

            }
            Member player = findMemberByID(playerID);
            if (player != null){
                teamTwo.add(player);
            }
        }

        // ---- Registering scores for teams ----
        int teamOneScore = InputHelpers.ReadInteger("Enter score for team one: ", "Enter a valid number");
        int teamTwoScore = InputHelpers.ReadInteger("Enter score for team two: ", "Enter a valid number");

        Match match = new Match(teamOne, teamTwo, chosenDisciplin, true, teamOneScore, teamTwoScore, date);
        matches.put(match, nameOfMatch);
    }

    public void ShowMatchResults(){
        int index = 1;
        for (String name : matches.values())
        {
            System.out.println(index + ". " + name);
            index++;
        }


        int matchIndex = InputHelpers.ReadInteger("Enter Match Index: ", "Enter a proper number dumbie");

        List<Map.Entry<Match, String>> entries = new ArrayList<>(matches.entrySet());
        Map.Entry<Match, String> selectedMatch = entries.get(matchIndex - 1);

        System.out.println("Team one score: " + selectedMatch.getKey().getTeamOneScore());
        System.out.println("Team two score: " + selectedMatch.getKey().getTeamTwoScore());
        selectedMatch.getKey().showWinners();
    }
}
