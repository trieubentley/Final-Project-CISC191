package budgettracker.models;

import java.time.LocalDate;

// Module 3: Inheritance — abstract base class for Expense and Income.
// Both share an amount, description, date, and category.
public abstract class Transaction implements Comparable<Transaction> {

    private static int nextId = 1; // auto-increment ID for each transaction

    private final int id;
    private final double amount;
    private final String description;
    private final LocalDate date;
    private final Category category;

    public Transaction(double amount, String description, LocalDate date, Category category) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative.");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description cannot be empty.");
        if (date == null) throw new IllegalArgumentException("Date cannot be null.");
        if (category == null) throw new IllegalArgumentException("Category cannot be null.");

        this.id = nextId++;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.category = category;
    }

    // Module 3: Polymorphism — subclasses define whether it adds or subtracts money
    public abstract double getSignedAmount();

    // Module 3: Comparable — lets us sort transactions by date
    @Override
    public int compareTo(Transaction other) {
        return this.date.compareTo(other.date);
    }

    // --- Getters ---
    public int getId()              { return id; }
    public double getAmount()       { return amount; }
    public String getDescription()  { return description; }
    public LocalDate getDate()      { return date; }
    public Category getCategory()   { return category; }

    @Override
    public String toString() {
        return String.format("[%d] %s | $%.2f | %s | %s", id, description, amount, category, date);
    }

    // Used by tests to reset ID counter between runs
    public static void resetIdCounter() { nextId = 1; }
}
