public class Payment
{
    private Date date;
    private int amount;
    private boolean paid;

    public Payment(){}

    public Payment (int date, int amount)
    {
        this.date = new Date(date);
        this.amount = amount;
        this.paid = false;
    }
    public Date getDate()
    {
        return date;
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
