// this currently looks fine to me, but we can come back

public class Transaction {
    private int id;
    private double amount;
    private String description;

    public Transaction(int id, double amount, String description) {
        if (0 > id) {
            throw new IllegalArgumentException("ID must be greater than 0.");
        }

        if (0 > amount) {
            throw new IllegalArgumentException("Amount must not less than 0.");
        }

        this.id = id;
        this.
    }
    

    // probably add a check for the date

    public double getAmount() { 
        return amount;
    }

    public String getTransactionDescription() { 
        return description; 
    }

    // idk if we need this, probably not
    public String toString() {
        return ""
    }
}