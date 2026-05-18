package budgettracker.ui;

import budgettracker.models.*;
import budgettracker.persistence.TransactionRepository;
import budgettracker.services.FinanceService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// Module 7: JavaFX Controller — handles all UI events using lambda event handlers.
// Uses MVC-style separation: this class only deals with UI, FinanceService does the math.
public class BudgetController {

    // --- FXML fields (linked to main-view.fxml) ---
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Integer>   colId;
    @FXML private TableColumn<Transaction, String>    colType;
    @FXML private TableColumn<Transaction, String>    colDesc;
    @FXML private TableColumn<Transaction, Double>    colAmount;
    @FXML private TableColumn<Transaction, String>    colCategory;
    @FXML private TableColumn<Transaction, LocalDate> colDate;

    @FXML private TextField     amountField;
    @FXML private TextField     descField;
    @FXML private ComboBox<Category>  categoryBox;
    @FXML private DatePicker    datePicker;
    @FXML private RadioButton   incomeRadio;
    @FXML private RadioButton   expenseRadio;
    @FXML private ToggleGroup   typeGroup;

    @FXML private Label balanceLabel;
    @FXML private TextArea reportArea;
    @FXML private Label statusLabel;

    private FinanceService service;
    private TransactionRepository repo;
    private final ObservableList<Transaction> tableData = FXCollections.observableArrayList();

    // Called from BudgetTrackerApp after FXML loads
    public void init(FinanceService service, TransactionRepository repo) {
        this.service = service;
        this.repo = repo;
        setupTable();
        setupForm();
        refreshTable();
        updateBalance();
    }

    // Wire up table columns
    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Module 7: Lambda — dynamically show "Income" or "Expense" in the Type column
        colType.setCellValueFactory(data -> {
            String type = (data.getValue() instanceof Income) ? "Income" : "Expense";
            return new javafx.beans.property.SimpleStringProperty(type);
        });

        // Module 7: Lambda — color income rows green, expense rows red
        transactionTable.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Transaction t, boolean empty) {
                super.updateItem(t, empty);
                if (empty || t == null) {
                    setStyle("");
                } else if (t instanceof Income) {
                    setStyle("-fx-background-color: #e8f5e9;");
                } else {
                    setStyle("-fx-background-color: #ffebee;");
                }
            }
        });

        transactionTable.setItems(tableData);
    }

    private void setupForm() {
        categoryBox.setItems(FXCollections.observableArrayList(Category.values()));
        categoryBox.getSelectionModel().selectFirst();
        datePicker.setValue(LocalDate.now());
    }

    // --- Event Handlers (wired via FXML onAction) ---

    @FXML
    private void handleAddTransaction() {
        try {
            double amount      = Double.parseDouble(amountField.getText().trim());
            String description = descField.getText().trim();
            LocalDate date     = datePicker.getValue();
            Category category  = categoryBox.getValue();

            if (description.isEmpty()) throw new IllegalArgumentException("Description cannot be empty.");
            if (date == null)          throw new IllegalArgumentException("Please select a date.");
            if (category == null)      throw new IllegalArgumentException("Please select a category.");

            // Module 3: Polymorphism — create the right subclass based on the radio button
            Transaction t = incomeRadio.isSelected()
                    ? new Income(amount, description, date, category)
                    : new Expense(amount, description, date, category);

            service.addTransaction(t);
            saveAsync(); // Module 7: background thread for file I/O
            refreshTable();
            updateBalance();
            clearForm();
            setStatus("Transaction added successfully.");

        } catch (NumberFormatException e) {
            setStatus("Error: Enter a valid number for amount.");
        } catch (IllegalArgumentException e) {
            setStatus("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteTransaction() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Please select a transaction to delete.");
            return;
        }
        service.removeTransaction(selected.getId());
        saveAsync();
        refreshTable();
        updateBalance();
        setStatus("Transaction deleted.");
    }

    @FXML
    private void handleGenerateReport() {
        reportArea.setText(service.generateReport());

        // Also show spending by category
        Map<Category, Double> spending = service.getSpendingByCategory();
        StringBuilder sb = new StringBuilder(service.generateReport());
        sb.append("\n\n--- Spending by Category ---\n");
        spending.forEach((cat, amt) -> sb.append(String.format("%-15s $%.2f%n", cat, amt)));
        reportArea.setText(sb.toString());
    }

    @FXML
    private void handleSortByDate() {
        List<Transaction> sorted = service.sortByDate(service.getAllTransactions());
        tableData.setAll(sorted);
        setStatus("Sorted by date.");
    }

    @FXML
    private void handleFilterExpenses() {
        // Module 2: Functional Interface — passing a lambda as a Predicate<Transaction>
        List<Transaction> expenses = service.filterTransactions(t -> t instanceof Expense);
        tableData.setAll(expenses);
        setStatus("Showing expenses only. Click 'Show All' to reset.");
    }

    @FXML
    private void handleFilterIncome() {
        List<Transaction> income = service.filterTransactions(t -> t instanceof Income);
        tableData.setAll(income);
        setStatus("Showing income only. Click 'Show All' to reset.");
    }

    @FXML
    private void handleShowAll() {
        refreshTable();
        setStatus("Showing all transactions.");
    }

    // --- Helpers ---

    private void refreshTable() {
        tableData.setAll(service.getAllTransactions());
    }

    private void updateBalance() {
        double balance = service.calculateBalance();
        balanceLabel.setText(String.format("Balance: $%.2f", balance));
        balanceLabel.setStyle(balance >= 0 ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    private void clearForm() {
        amountField.clear();
        descField.clear();
        datePicker.setValue(LocalDate.now());
        categoryBox.getSelectionModel().selectFirst();
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
    }

    // Module 7: Multithreading — file saves happen on a background thread so the UI stays responsive
    private void saveAsync() {
        Task<Void> saveTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                repo.saveToFile();
                return null;
            }
            @Override
            protected void failed() {
                Platform.runLater(() -> setStatus("Warning: Could not save to file."));
            }
        };
        new Thread(saveTask, "save-thread").start();
    }
}
