public class Income {
    // we can come back to this one
    private double incomeAdded;

    if (0.0 > incomeAdded) {
        throw new IllegalArgumentException("Added Income must not be less than 0.");
    }
}