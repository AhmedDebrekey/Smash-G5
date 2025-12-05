import java.util.HashMap;

public class Cashier {

    private HashMap<Member, Payment> payments = new HashMap<>();

    public void registerMember (Member member)
    {
        int currentYear = java.time.LocalDate.now().getYear();
        int price = calculatePrice(member);
        payments.put(member, new Payment (currentYear, price ));
    }

    private int calculatePrice (Member member)
    {
        int age = member.getbDay().getAge();

        if (!member.isActive())
        {
            return 250; // passivt medlemskab
        }

        if (age < 18)
        {
            return 800; // junior
        } else if (age >= 60)
        {
            return (int) (1500 + 0.75); // Senior med 25%
        } else
        {
            return 1500; // senior
        }
    }

    public boolean isInDebt(Member member)
    {
        if (payments.containsKey(member)) {
            return !payments.get(member).isPaid();
        }
        return false;
    }

    public Payment getPayment(Member member)
    {
        return payments.get(member);
    }

}
