public class Payment {
    private int year;
    private int amount;
    private boolean paid;

    public Payment (int year, int amaount)
    {
        this.year = year;
        this.amount = amount;
        this.paid = false;
    }
    public int getYear()
    {
        return year;
    }

    public int getAmount()
    {
        return amount;
    }

    public boolean isPaid()
    {
        return paid;
    }

    public void setPaid(boolean paid)
    {
        this.paid = paid;
    }

}
