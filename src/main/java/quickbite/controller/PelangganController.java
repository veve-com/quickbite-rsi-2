package quickbite.controller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickbite.controller.dto.PelangganInfo;
import quickbite.controller.exception.ResourceNotFoundException;
import quickbite.entity.Pelanggan;
import quickbite.repository.PelangganRepository;

@Service
public class PelangganController {
    private final PelangganRepository pelangganRepo;

    public PelangganController(PelangganRepository pelangganRepo) {
        this.pelangganRepo = pelangganRepo;
    }

    @Transactional(readOnly = true)
    public PelangganInfo infoPelanggan(String id) {
        return toInfo(cari(id));
    }

    @Transactional
    public PelangganInfo ubahAlamat(String id, String alamatBaru) {
        Pelanggan p = cari(id);
        p.ubahAlamat(alamatBaru);
        return toInfo(p);
    }

    private Pelanggan cari(String id) {
        return pelangganRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Pelanggan " + id));
    }

    private PelangganInfo toInfo(Pelanggan p) {
        return new PelangganInfo(p.getIdPelanggan(), p.getInfoPelanggan(),
                p.getNomorTelepon(), p.getAlamat());
    }
}