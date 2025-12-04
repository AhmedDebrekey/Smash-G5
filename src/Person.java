public class Person
{
    protected String name;
    protected Date bDay;
    protected String email;

    public Person(){}

    public Person (String name, int bDay, String email)
    {
        this.name = name;
        this.bDay = new Date(bDay);
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public Date getbDay()
    {
        return bDay;
    }

    public String getEmail()
    {
        return email;
    }
}
