public class MultipleLettersException extends Exception {
    @Override
    public String getMessage() {
        return "More than one letter was entered";
    }
}