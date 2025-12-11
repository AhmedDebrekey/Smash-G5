import ENUMS.AgeGroup;
import ENUMS.Discipline;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Club
{
    private final String name;
    private final String adress;
    private int totalMembers;
    private ArrayList<Member> members = new ArrayList<>();
    private ArrayList<Coach> coaches = new ArrayList<>();
    private Cashier cashier = new Cashier();
    private Map<Match, String> matches = new HashMap<>();
    private List<DataChangeListener> listeners = new ArrayList<>();

    public Club(String name, String adress)
    {
        this.name = name;
        this.adress = adress;
        totalMembers = 0;
    }

    public void addDataChangeListener(DataChangeListener listener) {
        listeners.add(listener);
    }

    public void removeDataChangeListener(DataChangeListener listener) {
        listeners.remove(listener);
    }

    public void notifyDataChanged() {
        for (DataChangeListener listener : listeners) {
            listener.onDataChanged();
        }
    }

    public Cashier getCashier()
    {
        return cashier;
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
        cashier.registerMember(member);
        notifyDataChanged();
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

    public Map <Match, String > getMatches()
    {
        return matches;
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


        java.time.LocalDate today = java.time.LocalDate.now();
        Date registerationDate = new Date( today.getYear() * 10000 + today.getMonthValue() * 100 + today.getDayOfMonth());
        Member member = new Member(name, bDay, email, memberId, chosenCoach, chosenDisciplines, isCompetitive, registerationDate);
        addMember(member);
    }

    private void showMembers() {
        Collections.sort(members);
        for (Member member : members) {
            System.out.println("\nName: "+ member.getName() + ", MemberID: " + member.getMemberId());
        }
    }
    
    public Member findMemberByID() {
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

    public Member findMemberByID(int memberID){
        for (Member m : members)
        {
            if (m.getMemberId() == memberID)
            {
                    return m;
            }
        }
        System.out.println("No members in the club...");
        return null;
    }

    public void editMember() {
        showMembers();

        if (members.isEmpty()) {
            System.out.println("No members in the club...");
            return;
        }

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
        notifyDataChanged();
    }


    public void paymentOverview(){
        cashier.updatePayments();
        System.out.println("\n ----PAYMENT OVERVIEW----");

        System.out.println("\n --Members and their fees--");
        // ---------- Member fees (null-safe) ----------
        for (Member m : members)
        {
            Payment p = cashier.getPayment(m);

            if (p == null) {
                // No payment record for this member
                System.out.println(m.getName() + " (ID: " + m.getMemberId() + ") - No payment record");
                continue;
            }

            String paidText = p.isPaid() ? "Yes" : "No";
            System.out.println(m.getName() + " (ID: " + m.getMemberId() + ") " + "Amount:" + p.getAmount() + " - Paid: " + paidText + ", Charge Date: " + p.getDate().getDate());
        }


        System.out.println("\n --Members in debt--");
        boolean anyInDebt = false;

        for (Member m : members)
        {
            if (cashier.isInDebt(m))
            {
                System.out.println(m.getName() + " (ID: " + m.getMemberId() + ")");
                anyInDebt = true;
            }
        }

        if(!anyInDebt)
        {
            System.out.println("No members are in debt");
        }
    }

    public void setMemberAsPaid()
    {
        boolean inDebt = false;
        System.out.println("\n--- Members with unpaid fees ---\n");

        for (int i = 0; i < members.size(); i++)
        {
            Member member = members.get(i);
            Payment payment = cashier.getPayment(member);
            if (!payment.isPaid())
            {
                System.out.println("Name: " + member.getName() + ", ID: " + member.getMemberId());
                inDebt = true;
            }
        }

        if (!inDebt)
        {
            System.out.println("No members in debt!");
            return;
        }

        Member member = findMemberByID();

        Payment payment = cashier.getPayment(member);

        if (payment == null)
        {
            System.out.println("No payment found for this member.");
            return;
        }

        payment.setPaid(true);
        notifyDataChanged();
        System.out.println(member.getName() + " has now paid the fee!");
    }

    public void registerTournament() {
        ArrayList<Member> teamOne = new ArrayList<>();
        List<Member> teamTwo = new ArrayList<>();
        // ---- Name the Match ----
        String nameOfMatch = InputHelpers.ReadString("Enter the name of the match: ", "Enter a valid name");
        // ---- Date of Match ----
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
        boolean hasPlayers = showEligibleMembers(teamOne, chosenDisciplin);
        if (!hasPlayers)
        {
            System.out.println("NO PLAYERS IN THIS DISCIPLINE");
            return;
        }
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

        hasPlayers = showEligibleMembers(teamOne, chosenDisciplin);
        if (!hasPlayers)
        {
            System.out.println("NO PLAYERS IN THIS DISCIPLINE");
            return;
        }
        for (int i = 0; i < teamTwoPlayers; i++) {
            int playerID;
            while (true) {
                playerID = InputHelpers.ReadInteger(
                        "\nPlease enter member ID player nr. " + (i+1) + " in team two: ",
                        "Invalid ID, must only contain numbers."
                );
                if (memberIdExists(playerID, members) && !memberIdExists(playerID, teamOne)) {
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
        int teamOneScore = 0;
        int teamTwoScore = 0;

        while (true) {
            // ---- Registering scores for teams ----
            teamOneScore = InputHelpers.ReadInteger("Enter score for team one: ", "Enter a valid number");
            teamTwoScore = InputHelpers.ReadInteger("Enter score for team two: ", "Enter a valid number");

            if (ValidateTeamScore(teamOneScore, teamTwoScore)) {
                break;
            }
            else  {
                System.out.println("Invalid score for match. It is a best of 3. List of possible answers: 2-0, 2-1, 0-2, 1-2.");
            }
        }


        Match match = new Match(teamOne, teamTwo, chosenDisciplin, true, teamOneScore, teamTwoScore, date);
        match.updateMatchPlayersScore();
        matches.put(match, nameOfMatch);

        notifyDataChanged();

    }

    private boolean ValidateTeamScore(int teamOneScore, int teamTwoScore) {

        if (teamOneScore == 2 && (teamTwoScore == 0 || teamTwoScore == 1))
        {
            return true;
        }
        if (teamTwoScore == 2 && (teamOneScore == 0 || teamOneScore == 1))
        {
            return true;
        }

        return false; // bad input lmao
    }

    public void ShowMatchResults(){
        if (matches.isEmpty())
        {
            System.out.println("No matches found.");
            return;
        }

        int index = 1;
        for (String name : matches.values())
        {
            System.out.println(index + ". " + name);
            index++;
        }


        int matchIndex = InputHelpers.ReadInteger("Enter Match Index: ", "Enter a proper number");

        List<Map.Entry<Match, String>> entries = new ArrayList<>(matches.entrySet());
        Map.Entry<Match, String> selectedMatch = entries.get(matchIndex - 1);

        System.out.println("Team one score: " + selectedMatch.getKey().getTeamOneScore());
        System.out.println("Team two score: " + selectedMatch.getKey().getTeamTwoScore());
        selectedMatch.getKey().showWinners();
    }



    public void ShowMembersResults(){
        for (Discipline d : Discipline.values()) {
            List<Member> sortedJuniorMembers = members.stream()
                    .filter(m -> m.getdiscipline().contains(d) && m.getAgeGroup() == AgeGroup.JUNIOR && m.isCompetitive())
                    .sorted(Comparator.comparingInt(m -> m.getResult(d)))
                    .toList()
                    .reversed();

            List<Member> sortedSeniorMembers = members.stream()
                    .filter(m -> m.getdiscipline().contains(d) && m.getAgeGroup() == AgeGroup.SENIOR && m.isCompetitive())
                    .sorted(Comparator.comparingInt(m -> m.getResult(d)))
                    .toList()
                    .reversed();

            System.out.println("Results for Juniors in " + d + ":");
            printRankings(d, sortedJuniorMembers);
            System.out.println("Results for Seniors in " + d + ":");
            printRankings(d, sortedSeniorMembers);
        }
    }

    private static void printRankings(Discipline d, List<Member> sortedMembers) {
        int index = 1;
        for (Member m : sortedMembers) {
            System.out.println(index + ". " + m.getName() + " - " + m.getResult(d));
            if (index == 5) {
                break;
            }
            index++;
        }
    }

    private boolean showEligibleMembers(List<Member> otherMembers, Discipline discipline) {
        boolean eligibleMember = false;

        for (int i = 0; i < members.size(); i++) {
            Member m = members.get(i);

            if (m.getdiscipline().contains(discipline) &&
                    m.isCompetitive() &&
                    m.isActive() &&
                    !otherMembers.contains(m)) {

                eligibleMember = true;
                System.out.println("\nName: "+ m.getName() +
                        ", MemberID: " + m.getMemberId() +
                        ", Age Group: " + m.getAgeGroup());
            }
        }
        return eligibleMember;
    }
}
