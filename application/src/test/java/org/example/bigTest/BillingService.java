package org.example.bigTest;

public class BillingService {
    private final TaxCalculator taxCalculator;

    public BillingService(TaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
    }

    /**
     * Метод вычисляет финальную сумму, добавляя налог к базовой сумме.
     */
    public double calculateFinalAmount(double amount) {
        double tax = taxCalculator.calculateTax(amount);
        return amount + tax;
    }
}