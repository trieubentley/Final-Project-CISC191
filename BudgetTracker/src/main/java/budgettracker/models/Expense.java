package budgettracker.models;

import java.time.LocalDate;

// Module 3: Inheritance — Expense extends Transaction.
// getSignedAmount() returns a negative value because expenses reduce your balance.
public class Expense extends Transaction {

    public Expense(double amount, String description, LocalDate date, Category category) {
        super(amount, description, date, category);
    }

    @Override
    public double getSignedAmount() {
        return -getAmount(); // expenses subtract from balance
    }

    @Override
    public String toString() {
        return "EXPENSE: " + super.toString();
    }
}
