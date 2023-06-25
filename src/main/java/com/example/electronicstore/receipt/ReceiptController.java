package com.example.electronicstore.receipt;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {
    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @Operation(summary = "Calculate the receipt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculate a receipt"),
            @ApiResponse(responseCode = "404", description = "Basket not found")})
    @GetMapping(path = "")
    public Receipt calculateReceipt(@RequestParam @NotNull Long basketId) {
        return receiptService.calculate(new ReceiptRequest(basketId));
    }
}
