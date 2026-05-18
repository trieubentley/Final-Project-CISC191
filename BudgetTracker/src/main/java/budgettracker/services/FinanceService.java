package budgettracker.services;

import budgettracker.models.*;
import budgettracker.persistence.TransactionRepository;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// This is the brain of the app. All the core logic lives here.
public class FinanceService {

    private final TransactionRepository repository;

    public FinanceService(TransactionRepository repository) {
        this.repository = repository;
    }

    // --- Basic operations ---

    public void addTransaction(Transaction t) {
        repository.add(t);
    }

    public void removeTransaction(int id) {
        repository.remove(id); // throws TransactionNotFoundException if not found
    }

    public List<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    // -------------------------------------------------------------------------
    // Module 2: Functional Interfaces (Predicate)
    // filterTransactions takes any Predicate<Transaction> so callers can pass
    // any filter condition as a lambda. Very flexible and reusable.
    // -------------------------------------------------------------------------
    public List<Transaction> filterTransactions(Predicate<Transaction> condition) {
        return repository.findAll().stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Module 6: Collections + Streams
    // Group transactions by category and sum amounts per group.
    // -------------------------------------------------------------------------
    public Map<Category, Double> getSpendingByCategory() {
        return repository.findAll().stream()
                .filter(t -> t instanceof Expense)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    // -------------------------------------------------------------------------
    // Module 6: Streams — calculate current balance
    // -------------------------------------------------------------------------
    public double calculateBalance() {
        return repository.findAll().stream()
                .mapToDouble(Transaction::getSignedAmount)
                .sum();
    }

    // -------------------------------------------------------------------------
    // Module 5: Recursion
    // Recursively sum a list of transactions. This is a clean example of how
    // recursion works: base case = empty list; recursive case = first + rest.
    // -------------------------------------------------------------------------
    public double recursiveSum(List<Transaction> list, int index) {
        if (index >= list.size()) return 0.0;                          // base case
        return list.get(index).getSignedAmount() + recursiveSum(list, index + 1); // recursive case
    }

    // -------------------------------------------------------------------------
    // Module 5: Sorting algorithm (insertion sort on a copy of the list)
    // Sorts by date (uses Transaction's Comparable implementation).
    // -------------------------------------------------------------------------
    public List<Transaction> sortByDate(List<Transaction> transactions) {
        List<Transaction> sorted = new ArrayList<>(transactions);
        for (int i = 1; i < sorted.size(); i++) {
            Transaction current = sorted.get(i);
            int j = i - 1;
            while (j >= 0 && sorted.get(j).compareTo(current) > 0) {
                sorted.set(j + 1, sorted.get(j));
                j--;
            }
            sorted.set(j + 1, current);
        }
        return sorted;
    }

    // -------------------------------------------------------------------------
    // Module 6: Streams — generate a simple monthly report string
    // -------------------------------------------------------------------------
    public String generateReport() {
        List<Transaction> all = repository.findAll();
        double income  = all.stream().filter(t -> t instanceof Income).mapToDouble(Transaction::getAmount).sum();
        double expense = all.stream().filter(t -> t instanceof Expense).mapToDouble(Transaction::getAmount).sum();
        double balance = income - expense;

        return String.format(
                "=== Monthly Report ===\nTotal Income:   $%.2f\nTotal Expenses: $%.2f\nNet Balance:    $%.2f",
                income, expense, balance
        );
    }
}
