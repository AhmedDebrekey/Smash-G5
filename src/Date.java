public class Date
{
    private int date; // Format: YYYYMMDD

    public Date() {}

    public Date(int date)
    {
        this.date = date;
    }

    public int getDate()
    {
        return date;
    }

    public int getYear()
    {
        return getDate() / 10000;
    }

    public int getDay() {
        int day = 0;
        day = (getDate() % 100);
        return day;
    }

    public boolean skudAar()
    {
        if (getYear() % 4 != 0)
            return false;
        if (getYear() % 100 == 0 && getYear() % 400 != 0)
            return false;
        return true;
    }

    public boolean erDetSkudDag()
    {
        if (skudAar() && getMonth() != 2 && getDay() != 29)
            return false;
        return true;
    }

    public int getMonth() {
        int maaned = 0;
        maaned = ((getDate() / 100) % 100);
        return maaned;
    }

    public boolean valid()
    {
        if (getDate() < 17000301)
            return false;
        if (getMonth() < 1 || getMonth() > 12)
            return false;
        if (getDay() < 1 || getDay() > 31)
            return false;
        if (getMonth() == 2 && !skudAar() && getDay() > 28)
            return false;
        if (getMonth() == 2 && skudAar() && getDay() > 29)
            return false;
        if (getDay() > 30 && (getMonth() == 4 || getMonth() == 6 || getMonth() == 9 || getMonth() == 11 ))
            return false;
        return true;
    }
    public int getAge() {
        Date today = new Date(
                java.time.LocalDate.now().getYear() * 10000 +
                        java.time.LocalDate.now().getMonthValue() * 100 +
                        java.time.LocalDate.now().getDayOfMonth()
        );

        int age = today.getYear() - this.getYear();


        if (today.getMonth() < this.getMonth() ||
                (today.getMonth() == this.getMonth() && today.getDay() < this.getDay())) {
            age--;
        }
        return age;
    }

}
