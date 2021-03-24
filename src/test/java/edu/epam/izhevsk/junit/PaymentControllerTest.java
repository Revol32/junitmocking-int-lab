package edu.epam.izhevsk.junit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.*;
import static org.mockito.Mockito.anyLong;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentControllerTest {
    @Mock
    AccountService accountServiceMock;
    @Mock
    DepositService depositServiceMock;
    @InjectMocks
    PaymentController paymentController;

    @BeforeAll
    void setup() throws InsufficientFundsException {
        MockitoAnnotations.initMocks(this);
        Mockito.when(accountServiceMock.isUserAuthenticated(100L)).thenReturn(true);
        Mockito.when(accountServiceMock.isUserAuthenticated(not(cmpEq(100L)))).thenReturn(false);
        Mockito.when(depositServiceMock.deposit(lt(100L), anyLong())).thenReturn("OK");
        Mockito.when(depositServiceMock.deposit(geq(100L), anyLong())).thenThrow(new InsufficientFundsException());
    }

    @ParameterizedTest
    @DisplayName("Тест AccountService.isUserAuthenticated()")
    @MethodSource("Providers#testIsUserAuthenticatedProvider")
    void isUserAuthenticatedTest(Long id, boolean expected) {
        assertThat(accountServiceMock.isUserAuthenticated(id), equalTo(expected));
    }

    @ParameterizedTest
    @DisplayName("Тест DepositService.deposit() OK")
    @MethodSource("Providers#testDepositOkProvider")
    void depositDepositOkTest(Long id, Long amount, String expected) throws InsufficientFundsException {
        assertThat(depositServiceMock.deposit(amount, id), equalTo(expected));
    }

    @ParameterizedTest
    @DisplayName("Тест DepositService.deposit() InsufficientFunds")
    @MethodSource("Providers#testDepositInsufficientFundsProvider")
    void depositDepositInsufficientFundsTest(Long id, Long amount) {
        assertThrows(InsufficientFundsException.class, () -> depositServiceMock.deposit(amount, id));
    }

    @ParameterizedTest
    @DisplayName("Тест PaymentController.deposit() OK")
    @MethodSource("Providers#testDepositPaymentOkProvider")
    void depositOkTest(Long id, Long amount) {
        assertDoesNotThrow(() -> paymentController.deposit(amount, id));
    }

    @ParameterizedTest
    @DisplayName("Тест PaymentController.deposit() InsufficientFunds")
    @MethodSource("Providers#testDepositPaymentInsufficientFundsProvider")
    void depositInsufficientFundsTest(Long id, Long amount) {
        assertThrows(InsufficientFundsException.class, () -> paymentController.deposit(amount, id));
    }

    @ParameterizedTest
    @DisplayName("Тест PaymentController.deposit() SecurityException")
    @MethodSource("Providers#testDepositPaymentSecurityExceptionProvider")
    void depositSecurityExceptionTest(Long id, Long amount) {
        assertThrows(SecurityException.class, () -> paymentController.deposit(amount, id));
    }
}
