public class Players extends Person {
    // Money should be an integer value for this game.
    private int money;

    public Players(String firstName) {
        super(firstName);
        this.money = 1000;
    }

    public Players(String firstName, String lastName) {
        super(firstName, lastName);
        this.money = 1000;
    }

    // --- Getters and Setters for money ---

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        String fullName = getFirstName() + " " + getLastName();
        return "Player: " + fullName.trim() + " | Current Money: $" + money;
    }
}
