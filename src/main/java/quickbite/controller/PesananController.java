package quickbite.controller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickbite.controller.dto.PesananInfo;
import quickbite.controller.exception.ResourceNotFoundException;
import quickbite.entity.Pembayaran;
import quickbite.entity.Pesanan;
import quickbite.entity.StatusPesanan;
import quickbite.repository.PesananRepository;
import java.util.List;

@Service
public class PesananController {
    private final PesananRepository pesananRepo;

    public PesananController(PesananRepository pesananRepo) {
        this.pesananRepo = pesananRepo;
    }

    @Transactional(readOnly = true)
    public List<PesananInfo> daftarPesanan() {
        return pesananRepo.findAllByOrderByTanggalDesc().stream()
                .map(this::toInfo).toList();
    }

    @Transactional
    public PesananInfo ubahStatus(String idPesanan, String statusBaru) {
        Pesanan p = pesananRepo.findById(idPesanan).orElseThrow(
                () -> new ResourceNotFoundException("Pesanan " + idPesanan));
        p.updateStatus(StatusPesanan.valueOf(statusBaru.toUpperCase()));
        return toInfo(p);
    }

    private PesananInfo toInfo(Pesanan p) {
        Pembayaran bayar = p.getPembayaran();
        return new PesananInfo(p.getIdPesanan(),
                p.getPelanggan().getInfoPelanggan(),
                p.getTanggal().toString(), p.getStatus().name(),
                p.getTotalBiaya(), p.getItems().size(),
                bayar != null && bayar.prosesVerifikasi());
    }
}