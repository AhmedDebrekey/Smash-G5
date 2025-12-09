import ENUMS.AgeGroup;

import java.util.HashMap;

public class Cashier {

    public Cashier(){};

    private HashMap<Member, Payment> payments = new HashMap<>();

    public void registerMember(Member member) {

        int fullDate = member.getRegistrationDate().getDate();
        int price = calculatePrice(member);
        payments.put(member, new Payment(fullDate, price));
    }
    private int calculatePrice (Member member)
    {
        int age = member.getbDay().getAge();

        if (!member.isActive())
        {
            return 250; // passivt medlemskab
        }
        if (member.getAgeGroup() == AgeGroup.JUNIOR)
        {
            return 800; // junior
        } else if (member.getAgeGroup() == AgeGroup.SENIOR && age > 60)
        {
            return (int) (1500 * 0.75); // Senior med 25%
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

    public void updatePayments()
    {
        for (Member m : payments.keySet())
        {
            Payment p = payments.get(m);
            int paymentYear = p.getDate().getYear();
            int currentYear = java.time.LocalDate.now().getYear();

            if (currentYear > paymentYear)
            {
                int newPrice = calculatePrice(m);
                int newDate = java.time.LocalDate.now().getYear();
                Payment newPayment = new Payment(newDate, newPrice);
                payments.put(m, newPayment);
            }
        }
    }

}
