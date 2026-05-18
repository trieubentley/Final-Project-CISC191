package budgettracker.exceptions;

// Module 4: Custom Exceptions — thrown when a transaction ID doesn't exist
public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(int id) {
        super("No transaction found with ID: " + id);
    }
}
