package quickbite.boundary;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import quickbite.boundary.dto.UbahAlamatRequest;
import quickbite.controller.PelangganController;
import quickbite.controller.dto.PelangganInfo;

@RestController
@RequestMapping("/api/pelanggan")
public class PelangganBoundary {
    private final PelangganController pelangganController;

    public PelangganBoundary(PelangganController pelangganController) {
        this.pelangganController = pelangganController;
    }

    @GetMapping("/{id}")
    public PelangganInfo info(@PathVariable String id) {
        return pelangganController.infoPelanggan(id);
    }

    @PutMapping("/{id}/alamat")
    public PelangganInfo ubahAlamat(@PathVariable String id,
                                    @Valid @RequestBody UbahAlamatRequest req) {
        return pelangganController.ubahAlamat(id, req.alamat());
    }
}