package com.assignment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * PortfolioManager Class with menu, deposit and withdrawal of cash,
 * buy and sell stock, display transaction history and portfolio information
 */

public class PortfolioManager {
    private ArrayList<TransactionHistory> portfolioList = new ArrayList<TransactionHistory>();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("M/d/yyyy");

    // Method to display the main menu and get user input
    private int displayMenuAndGetInput() {
        System.out.println("==========================================");
        System.out.println("Ashish Gulati Brokerage Account");
        System.out.println("==========================================");
        System.out.println("0 - Exit");
        System.out.println("1 - Deposit Cash");
        System.out.println("2 - Withdraw Cash");
        System.out.println("3 - Buy Stock");
        System.out.println("4 - Sell Stock");
        System.out.println("5 - Display Transaction History");
        System.out.println("6 - Display Portfolio");
        System.out.print("Enter option (0 to 6): ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    public double getAvailableCashBalance() {
        double availableCashBalance = 0.0;

        for (TransactionHistory transaction : portfolioList) {
            if (transaction.getTransType().equals("DEPOSIT")) {
                availableCashBalance += transaction.getQty();
            } else if (transaction.getTransType().equals("WITHDRAW")) {
                availableCashBalance -= transaction.getQty();
            }
        }

        return availableCashBalance;
    }

    // Method to deposit cash into the portfolio
    private void depositCash() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        if (amount <= 0) {
            System.out.println("Invalid deposit amount. Please enter a positive value.");
        } else {
            TransactionHistory depositTransaction = new TransactionHistory("CASH", LocalDate.now().format(dtf),"DEPOSIT", amount, 1.00);
            portfolioList.add(depositTransaction);
            System.out.println("Cash deposited successfully: $" + amount);
        }
    }

    // Method to withdraw cash from the portfolio
    private void withdrawCash() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount. Please enter a positive value.");
        } else {
            double cashBalance = getAvailableCashBalance();
            if (amount > cashBalance) {
                System.out.println("Insufficient cash balance. Withdrawal amount cannot be more than available cash.");
            } else {
                TransactionHistory withdrawTransaction = new TransactionHistory("CASH", LocalDate.now().format(dtf),"WITHDRAW", -amount, 1.00);
                portfolioList.add(withdrawTransaction);
                System.out.println("Cash withdrawn successfully: $" + amount);
            }
        }
    }

    // Method to buy stock
    private void buyStock() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter stock ticker: ");
        String ticker = scanner.nextLine();
        System.out.print("Enter quantity: ");
        double quantity = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter cost basis: ");
        double costBasis = scanner.nextDouble();
        scanner.nextLine();
        double cashBalance = getAvailableCashBalance();
        double totalCost = quantity * costBasis;
        if (totalCost > cashBalance) {
            System.out.println("Insufficient cash balance. Purchase amount cannot be more than available cash.");
        } else {
            TransactionHistory buyTransaction = new TransactionHistory(ticker, LocalDate.now().format(dtf),"BUY", quantity, costBasis);
            portfolioList.add(buyTransaction);
            TransactionHistory cashTransaction = new TransactionHistory("CASH", LocalDate.now().format(dtf),"WITHDRAW", -totalCost, 1.00);
            portfolioList.add(cashTransaction);
            System.out.println("Stock purchased successfully: " + quantity + " shares of " + ticker + " for $" + totalCost);
        }
    }

    // Method to sell stock
    public void sellStock(String ticker, double qty, double costBasis) {
        double cashBalance = getAvailableCashBalance();
        double cashQty = qty * costBasis;
        if(cashQty > cashBalance) {
            System.out.println("Insufficient cash balance. Selling amount cannot be more than available cash.");
        } else {
            TransactionHistory sellTransaction = new TransactionHistory(ticker, LocalDate.now().format(dtf), "SELL", qty, costBasis);
            portfolioList.add(sellTransaction);

            // Update cash balance after selling stock
            TransactionHistory cashTransaction = new TransactionHistory("CASH", LocalDate.now().format(dtf), "DEPOSIT", cashQty, 1.0);
            portfolioList.add(cashTransaction);
            System.out.println("Stock sold successfully: " + qty + " shares of " + ticker + " for $" + cashQty);
        }
    }

    // Method to display transaction history
    public void displayTransactionHistory() {
        if (portfolioList.isEmpty()) {
            System.out.println("Transaction history is empty.");
        } else {
            System.out.println("Transaction History:");
            System.out.println("Date    Ticker   Quantity   Cost Basis  Trans Type");
            System.out.println("==================================================================");
            for (TransactionHistory transaction : portfolioList) {
                System.out.println(transaction.toString());
            }
        }
    }

    public void displayPortfolio() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        System.out.println("Portfolio as of: " + LocalDateTime.now().format(dateTimeFormatter));
        System.out.println("=========================================");
        System.out.println("Ticker Quantity");
        System.out.println("================");
        double totalCash = 0.00;
        double totalBuy = 0.00;
        double totalSell = 0.00;
        for(TransactionHistory history : portfolioList) {
            if(history.getTicker().equalsIgnoreCase("CASH")) {
                totalCash += history.getQty();
            }
            if(history.getTicker().equalsIgnoreCase("DFEN")) {
                if(history.getTransType().equalsIgnoreCase("BUY")) {
                    totalBuy += history.getQty();
                } else {
                    totalSell += history.getQty();
                }
            }
        }
        System.out.println("CASH    " + totalCash);
        System.out.println("DFEN    " + (totalBuy-totalSell));
    }

    public static void main(String[] args) {
        // Create instance of com.assignment.PortfolioManager
        PortfolioManager portfolioManager = new PortfolioManager();

        // Create a Scanner object for user input
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            choice = portfolioManager.displayMenuAndGetInput();
            switch (choice) {
                case 0:
                    System.out.println("Exiting...");
                    break;
                case 1:
                    // Handle deposit cash
                    portfolioManager.depositCash();
                    break;
                case 2:
                    // Handle withdraw cash
                    portfolioManager.withdrawCash();
                    break;
                case 3:
                    // Handle buy stock
                    portfolioManager.buyStock();
                    break;
                case 4:
                    // Handle sell stock
                    System.out.println("Enter ticker: ");
                    String ticker = scanner.nextLine();
                    System.out.println("Enter quantity: ");
                    double qty = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Enter cost basis: ");
                    double costBasis = scanner.nextDouble();
                    scanner.nextLine();
                    portfolioManager.sellStock(ticker, qty, costBasis);
                    break;
                case 5:
                    // Display transaction history
                    portfolioManager.displayTransactionHistory();
                    break;
                case 6:
                    // Display portfolio
                    portfolioManager.displayPortfolio();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (choice != 0);

        // Close Scanner
        scanner.close();
    }
}
