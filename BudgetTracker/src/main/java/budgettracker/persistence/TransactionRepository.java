package budgettracker.persistence;

import budgettracker.exceptions.TransactionNotFoundException;
import budgettracker.models.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Module 4: File I/O — saves and loads transactions from a CSV file
// Module 6: Collections — stores transactions in an ArrayList
public class TransactionRepository implements Repository<Transaction, Integer> {

    private final List<Transaction> transactions = new ArrayList<>();
    private final String filePath;

    public TransactionRepository(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void add(Transaction t) {
        transactions.add(t);
    }

    @Override
    public void remove(Integer id) {
        boolean removed = transactions.removeIf(t -> t.getId() == id);
        if (!removed) throw new TransactionNotFoundException(id); // Module 4: custom exception
    }

    @Override
    public Transaction findById(Integer id) {
        return transactions.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions); // return a copy so the internal list stays safe
    }

    // Module 4: File I/O — write each transaction as a CSV line
    // Format: TYPE,amount,description,date,category
    public void saveToFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Transaction t : transactions) {
                String type = (t instanceof Income) ? "INCOME" : "EXPENSE";
                writer.write(type + "," + t.getAmount() + "," + t.getDescription()
                        + "," + t.getDate() + "," + t.getCategory());
                writer.newLine();
            }
        }
    }

    // Module 4: File I/O — read each line and recreate the correct Transaction subclass
    public void loadFromFile() throws IOException {
        File file = new File(filePath);
        if (!file.exists()) return; // nothing to load yet, that's fine

        transactions.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue; // skip malformed lines

                String type        = parts[0];
                double amount      = Double.parseDouble(parts[1]);
                String description = parts[2];
                LocalDate date     = LocalDate.parse(parts[3]);
                Category category  = Category.valueOf(parts[4]);

                if (type.equals("INCOME")) {
                    transactions.add(new Income(amount, description, date, category));
                } else {
                    transactions.add(new Expense(amount, description, date, category));
                }
            }
        }
    }
}
