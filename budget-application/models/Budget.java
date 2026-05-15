public class Budget {
    Category ExpenseCategory = new Category();
    
    private double monthlyLimit;
    private double currentAmountSpent; 

    public boolean isExceeded() {
        if (monthlyLimit < currentAmountSpent) { return true; }
        if (monthlyLimit >= currentAmountSpent) { return false; }
    }
}