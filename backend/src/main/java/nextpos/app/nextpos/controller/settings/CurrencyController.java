package nextpos.app.nextpos.controller.settings;

import lombok.RequiredArgsConstructor;
// import nextpos.app.nextpos.model.dto.request.CreateCurrencyRequest;
// import nextpos.app.nextpos.model.dto.request.UpdateRequest.UpdateCurrencyRequest;
import nextpos.app.nextpos.model.dto.response.CurrencyResponse;
import nextpos.app.nextpos.service.interf.CurrencyService;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/currencies")
// @CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    // @PostMapping
    // public ResponseEntity<CurrencyResponse> createCurrency(@Valid @RequestBody CreateCurrencyRequest request) {
    //     return ResponseEntity.status(HttpStatus.CREATED).body(currencyService.createCurrency(request));
    // }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyResponse> getCurrencyById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.getCurrencyById(id));
    }

    // @GetMapping("/user/{userId}")
    // public ResponseEntity<List<CurrencyResponse>> getCurrencyByUserId(@PathVariable Long userId) {
    //     return ResponseEntity.ok(currencyService.findAllByCreatedBy(userId));
    // }

    @GetMapping
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<CurrencyResponse> updateCurrency(@PathVariable Long id,
    //         @Valid @RequestBody UpdateCurrencyRequest request) {
    //     return ResponseEntity.ok(currencyService.updateCurrency(id, request));
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
    //     currencyService.deleteCurrency(id);
    //     return ResponseEntity.noContent().build();
    // }
}