public class Budget {
    Category ExpenseCategory = new Category();
    
    private double monthlyLimit;
    private double currentAmountSpent; 

    if (0.0 > monthlyLimit) {
        throw new IllegalArgumentException("Monthly Limit must not be less than 0.");
    }

    if (0.0 > currentAmountSpent) {
        throw new IllegalArgumentException("Expenditures must not be less than 0.");
    }

    public boolean isExceeded() {
        if (monthlyLimit < currentAmountSpent) { return true; }
        if (monthlyLimit >= currentAmountSpent) { return false; }
    }
}