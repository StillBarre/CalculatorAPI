package pt.henriquebarradas;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;


@Service
public class CalculatorConsumer {

    private final CalculatorService calculatorService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public CalculatorConsumer(CalculatorService calculatorService, KafkaTemplate<String, String> kafkaTemplate) {
        this.calculatorService = calculatorService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "calculator_topic", groupId = "calculator_group")
    public void processMessage(String message) {
        String[] parts = message.split(",");
        String requestId = parts[0];
        String operation = parts[1];
        BigDecimal a = new BigDecimal(parts[2]);
        BigDecimal b = new BigDecimal(parts[3]);

        try {
            BigDecimal result = calculatorService.calculate(operation, a, b);
            kafkaTemplate.send("calculator_response_topic", requestId + "," + result);
        } catch (ArithmeticException e) {
            kafkaTemplate.send("calculator_response_topic", requestId + ",ERROR: " + e.getMessage());
        } catch (Exception e) {
            kafkaTemplate.send("calculator_response_topic", requestId + ",ERROR: Calculation failed");
        }
    }
}
