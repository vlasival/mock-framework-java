package org.example.bigTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mock.core.MockFramework.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mock.annotation.Mock;
import org.mock.core.MockFramework;
import org.mock.junit.MockingExtension;

@ExtendWith(MockingExtension.class)
public class BillingServiceTest {

    @Mock
    private TaxCalculator taxCalculator;

    private BillingService billingService;

    @BeforeEach
    void setUp() {
        billingService = new BillingService(taxCalculator);
    }

    @Test
    void testCalculateFinalAmount() {
        MockFramework.startMocking();
        when(taxCalculator.calculateTax(100.0)).thenReturn(15.0);
        MockFramework.stopMocking();

        double finalAmount = billingService.calculateFinalAmount(100.0);
        assertEquals(115.0, finalAmount, 0.001);
    }

    @Test
    void testCalculateFinalAmountDefault() {
        // Без настройки stub‑правила метод calculateTax возвращает 0.0
        double finalAmount = billingService.calculateFinalAmount(200.0);
        assertEquals(200.0, finalAmount, 0.001);
    }

    @Test
    void testCalculateFinalAmountWithException() {
        MockFramework.startMocking();
        when(taxCalculator.calculateTax(-50.0)).thenThrow(new IllegalArgumentException("Invalid amount"));
        MockFramework.stopMocking();

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> billingService.calculateFinalAmount(-50.0));
        assertEquals("Invalid amount", exception.getMessage());
    }

    @Test
    void testMultipleStubbingRules() {
        MockFramework.startMocking();
        when(taxCalculator.calculateTax(150.0)).thenReturn(20.0);
        // Переопределяем правило: теперь для суммы 150.0 налог равен 25.0
        when(taxCalculator.calculateTax(150.0)).thenReturn(25.0);
        MockFramework.stopMocking();

        double tax = taxCalculator.calculateTax(150.0);
        assertEquals(25.0, tax, 0.001);
    }

    @Test
    void testSequentialCalls() {
        MockFramework.startMocking();
        when(taxCalculator.calculateTax(120.0)).thenReturn(18.0);
        MockFramework.stopMocking();

        // Первый вызов возвращает настроенное значение
        assertEquals(18.0, taxCalculator.calculateTax(120.0), 0.001);
        // Второй вызов не покрыт stub‑правилом – возвращается 0.0
        assertEquals(0.0, taxCalculator.calculateTax(100.0), 0.001);
    }
}