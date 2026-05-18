package budgettracker.models;

import java.time.LocalDate;

// Module 3: Inheritance — Income extends Transaction.
// getSignedAmount() returns a positive value because income adds to your balance.
public class Income extends Transaction {

    public Income(double amount, String description, LocalDate date, Category category) {
        super(amount, description, date, category);
    }

    @Override
    public double getSignedAmount() {
        return getAmount(); // income adds to balance
    }

    @Override
    public String toString() {
        return "INCOME:  " + super.toString();
    }
}
