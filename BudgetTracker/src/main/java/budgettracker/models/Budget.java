package budgettracker.models;

// Module 1: Arrays + OO — Budget stores per-category spending limits in an array
// parallel to the Category enum values. Simple and easy to follow.
public class Budget {

    // Parallel array: index matches Category.ordinal()
    // e.g., limits[Category.FOOD.ordinal()] = 300.0
    private final double[] limits;

    public Budget() {
        limits = new double[Category.values().length]; // one slot per category, default 0.0
    }

    // Set a monthly spending limit for a category
    public void setLimit(Category category, double limit) {
        if (limit < 0) throw new IllegalArgumentException("Budget limit cannot be negative.");
        limits[category.ordinal()] = limit;
    }

    // Get the limit for a category
    public double getLimit(Category category) {
        return limits[category.ordinal()];
    }

    // Check if spending in a category has exceeded its limit (0.0 limit = no limit set)
    public boolean isExceeded(Category category, double amountSpent) {
        double limit = limits[category.ordinal()];
        return limit > 0 && amountSpent > limit;
    }

    // Module 1: Array processing — loop through all categories and return any exceeded ones
    public String[] getExceededCategories(double[] spentPerCategory) {
        int count = 0;
        // First pass: count how many are exceeded
        for (int i = 0; i < limits.length; i++) {
            if (limits[i] > 0 && spentPerCategory[i] > limits[i]) count++;
        }

        // Second pass: fill result array
        String[] exceeded = new String[count];
        int index = 0;
        for (int i = 0; i < limits.length; i++) {
            if (limits[i] > 0 && spentPerCategory[i] > limits[i]) {
                exceeded[index++] = Category.values()[i].name();
            }
        }
        return exceeded;
    }
}
