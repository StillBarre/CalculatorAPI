package pt.henriquebarradas;

import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CalculationService {

    private final KafkaTemplate<String, String> kafkaTemplate;


    private final Map<String, CompletableFuture<BigDecimal>> pendingResults = new ConcurrentHashMap<>();

    public CalculationService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public BigDecimal sendCalculationRequest(String operation, BigDecimal a, BigDecimal b) {
        String requestId = UUID.randomUUID().toString();
        String message = String.format("%s,%s,%s,%s", requestId, operation, a, b);

        CompletableFuture<BigDecimal> future = new CompletableFuture<>();
        pendingResults.put(requestId, future);

        kafkaTemplate.send("calculator_topic", message);

        try {
            String response = String.valueOf(future.get(5, TimeUnit.SECONDS));

            if (response.startsWith("ERROR:")) {
                throw new ArithmeticException(response.substring(6)); 
            }

            return new BigDecimal(response);
        } catch (TimeoutException e) {
            throw new RuntimeException("Calculation timeout");
        } catch (Exception e) {
            throw new RuntimeException("Calculation failed", e);
        } finally {
            pendingResults.remove(requestId);
        }
    }


    @KafkaListener(topics = "calculator_response_topic", groupId = "calculator_group")
    public void receiveCalculationResponse(String message) {
        String[] parts = message.split(",");
        String requestId = parts[0];
        BigDecimal result = new BigDecimal(parts[1]);

        CompletableFuture<BigDecimal> future = pendingResults.remove(requestId);
        if (future != null) {
            future.complete(result);
        }
    }
}
