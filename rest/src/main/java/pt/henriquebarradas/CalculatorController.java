package pt.henriquebarradas;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final CalculationService calculationService;

    public CalculatorController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping("/sum")
    public Map<String, BigDecimal> sum(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        return Map.of("result sum", calculationService.sendCalculationRequest("sum", a, b));
    }

    @GetMapping("/subtract")
    public Map<String, BigDecimal> subtract(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        return Map.of("result subtract", calculationService.sendCalculationRequest("subtract", a, b));
    }

    @GetMapping("/multiply")
    public Map<String, BigDecimal> multiply(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {
        return Map.of("result teste multiply", calculationService.sendCalculationRequest("multiply", a, b));
    }

    @GetMapping("/divide")
    public Map<String, BigDecimal> divide(@RequestParam BigDecimal a, @RequestParam BigDecimal b) {

        return Map.of("result divide", calculationService.sendCalculationRequest("divide", a, b));
    }
}
