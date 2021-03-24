import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

public class Providers {
    public static Stream<Arguments> testIsUserAuthenticatedProvider() {
        return Stream.of(
                arguments(100L, true),
                arguments(99L, false),
                arguments(null, false)
        );
    }

    public static Stream<Arguments> testDepositOkProvider() {
        return Stream.of(
                arguments(100L, 50L, "OK"),
                arguments(50L, 50L, "OK")
        );
    }

    public static Stream<Arguments> testDepositInsufficientFundsProvider() {
        return Stream.of(
                arguments(100L, 100L),
                arguments(100L, 101L),
                arguments(99L, 102L)
        );
    }

    public static Stream<Arguments> testDepositPaymentOkProvider() {
        return Stream.of(
                arguments(100L, 50L),
                arguments(100L, null)
        );
    }

    public static Stream<Arguments> testDepositPaymentInsufficientFundsProvider() {
        return Stream.of(
                arguments(100L, 100L),
                arguments(100L, 101L)
        );
    }

    public static Stream<Arguments> testDepositPaymentSecurityExceptionProvider() {
        return Stream.of(
                arguments(99L, 50L),
                arguments(50L, 101L),
                arguments(null, null)
        );
    }
}
