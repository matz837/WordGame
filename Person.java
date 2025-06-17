public class Person {
    private String firstName;
    private String lastName;

    // Constructor with only first name
    public Person(String firstName) {
        this.firstName = firstName;
        this.lastName = "";
    }

    // Constructor with first and last name
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
