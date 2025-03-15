package pt.henriquebarradas;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculatorService {

    public BigDecimal calculate(String operation, BigDecimal a, BigDecimal b) {
        return switch (operation) {
            case "sum" -> a.add(b);
            case "subtract" -> a.subtract(b);
            case "multiply" -> a.multiply(b);
            case "divide" -> {
                if (b.compareTo(BigDecimal.ZERO) == 0) {
                    throw new ArithmeticException("Division by zero is not allowed");
                }
                yield a.divide(b, 10, RoundingMode.HALF_UP);
            }
            default -> throw new IllegalArgumentException("Invalid operation: " + operation);
        };
    }
}