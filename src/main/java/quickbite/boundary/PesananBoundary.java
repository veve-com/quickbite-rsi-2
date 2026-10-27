package quickbite.boundary;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import quickbite.boundary.dto.UbahStatusRequest;
import quickbite.controller.PesananController;
import quickbite.controller.dto.PesananInfo;
import java.util.List;

@RestController
@RequestMapping("/api/pesanan")
public class PesananBoundary {
    private final PesananController pesananController;

    public PesananBoundary(PesananController pesananController) {
        this.pesananController = pesananController;
    }

    @GetMapping
    public List<PesananInfo> daftar() {
        return pesananController.daftarPesanan();
    }

    @PatchMapping("/{id}/status")
    public PesananInfo ubahStatus(@PathVariable String id,
                                  @Valid @RequestBody UbahStatusRequest req) {
        return pesananController.ubahStatus(id, req.status());
    }
}