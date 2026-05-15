// this currently looks fine to me, but we can come back

public abstract class Transaction {
    private int id;
    private double amount;
    private LocalDate date;
    private String description;

    public double getAmount() { return amount;}

    public String getTransactionDescription() { return description; }
}