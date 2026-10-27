package quickbite.boundary;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quickbite.boundary.dto.CheckoutRequest;
import quickbite.boundary.dto.CheckoutResponse;
import quickbite.controller.CheckoutController;
import quickbite.controller.dto.CheckoutResult;
import quickbite.controller.dto.EstimasiResult;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutBoundary {
    private final CheckoutController checkoutController;

    public CheckoutBoundary(CheckoutController checkoutController) {
        this.checkoutController = checkoutController;
    }

    @PostMapping
    public ResponseEntity<CheckoutResponse> checkout(
            @Valid @RequestBody CheckoutRequest request) {
        CheckoutResult hasil = checkoutController.prosesCheckout(request.toCommand());
        return ResponseEntity.ok(new CheckoutResponse(hasil.idPesanan(), hasil.total()));
    }
        @PostMapping("/estimasi")
    public EstimasiResult estimasi(@Valid @RequestBody CheckoutRequest request) {
        return checkoutController.hitungEstimasi(request.toCommand());
    }

    @PostMapping("/{idPesanan}/selesai")
    public ResponseEntity<Void> selesai(@PathVariable String idPesanan) {
        checkoutController.selesaikanTransaksi(idPesanan);
        return ResponseEntity.noContent().build();
    }
}