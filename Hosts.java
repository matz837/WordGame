public class Hosts extends Players {

    public Hosts(String firstName, String lastName) {
        // A host starts with 0 money by default.
        super(firstName, lastName);
        this.setMoney(0);
    }

    public void randomizeNum() {
        System.out.println("\n" + getFirstName() + " is thinking of a new number between 0 and 100...");
        // Call the static method directly on the Numbers class.
        Numbers.generateNumber();
    }
}