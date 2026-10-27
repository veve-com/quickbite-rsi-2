package quickbite.controller;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickbite.boundary.gateway.PaymentGatewayAPI;
import quickbite.controller.dto.CheckoutCommand;
import quickbite.controller.dto.CheckoutResult;
import quickbite.controller.exception.ResourceNotFoundException;
import quickbite.controller.exception.StokTidakCukupException;
import quickbite.entity.*;
import quickbite.repository.MenuRepository;
import quickbite.repository.PelangganRepository;
import quickbite.repository.PesananRepository;
import quickbite.controller.dto.EstimasiResult;
import quickbite.controller.exception.PaymentFailedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CheckoutController {
    private final PelangganRepository pelangganRepo;
    private final MenuRepository menuRepo;
    private final PesananRepository pesananRepo;
    private final PaymentGatewayAPI paymentGateway;

    public CheckoutController(PelangganRepository pelangganRepo,
                              MenuRepository menuRepo,
                              PesananRepository pesananRepo,
                              PaymentGatewayAPI paymentGateway) {
        this.pelangganRepo = pelangganRepo;
        this.menuRepo = menuRepo;
        this.pesananRepo = pesananRepo;
        this.paymentGateway = paymentGateway;
    }

    @Transactional
    public CheckoutResult prosesCheckout(CheckoutCommand cmd) {
        Pelanggan pelanggan = pelangganRepo.findById(cmd.idPelanggan())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pelanggan " + cmd.idPelanggan()));

        List<ResolvedItem> items = new ArrayList<>();
        for (CheckoutCommand.Item ci : cmd.items()) {
            Menu menu = menuRepo.findByIdForUpdate(ci.idMenu())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Menu " + ci.idMenu()));
            if (!menu.validasiStok(ci.qty())) {
                throw new StokTidakCukupException(ci.idMenu());
            }
            items.add(new ResolvedItem(menu, ci.qty()));
        }

        Pesanan pesanan = new Pesanan(UUID.randomUUID().toString(),
                LocalDateTime.now(), pelanggan);
        for (ResolvedItem ri : items) {
            pesanan.tambahItem(new ItemPesanan(
                    UUID.randomUUID().toString(), ri.qty(), ri.menu()));
        }
        double total = pesanan.hitungTotal();

        String kodeRef = paymentGateway
                .mintaTransaksiPembayaran(total, cmd.tokenKartu());

        Pembayaran pembayaran = new Pembayaran(
                UUID.randomUUID().toString(), pesanan,
                total, "MIDTRANS", "SUKSES", kodeRef);
        pesanan.setPembayaran(pembayaran);

        for (ResolvedItem ri : items) {
            ri.menu().kurangiStok(ri.qty());
        }
        pesanan.updateStatus(StatusPesanan.DIPROSES);
        pesananRepo.save(pesanan);

        return new CheckoutResult(pesanan.getIdPesanan(), total);
    }
        @Transactional(readOnly = true)
    public EstimasiResult hitungEstimasi(CheckoutCommand cmd) {
        Pelanggan pelanggan = pelangganRepo.findById(cmd.idPelanggan())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pelanggan " + cmd.idPelanggan()));

        Pesanan sementara = new Pesanan("ESTIMASI", LocalDateTime.now(), pelanggan);
        for (CheckoutCommand.Item ci : cmd.items()) {
            Menu menu = menuRepo.findById(ci.idMenu())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Menu " + ci.idMenu()));
            sementara.tambahItem(new ItemPesanan(
                    UUID.randomUUID().toString(), ci.qty(), menu));
        }
        double total = sementara.hitungTotal();

        List<EstimasiResult.Baris> baris = sementara.getItems().stream()
                .map(i -> new EstimasiResult.Baris(
                        i.getMenu().getIdMenu(), i.getMenu().getNamaMenu(),
                        i.getJumlah(), i.getSubtotal(),
                        i.getMenu().validasiStok(i.getJumlah())))
                .toList();
        return new EstimasiResult(baris, total);
    }

    @Transactional
    public void selesaikanTransaksi(String idPesanan) {
        Pesanan pesanan = pesananRepo.findById(idPesanan)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pesanan " + idPesanan));
        Pembayaran pembayaran = pesanan.getPembayaran();
        if (pembayaran == null || !pembayaran.prosesVerifikasi()) {
            throw new PaymentFailedException("Pembayaran belum terverifikasi");
        }
        pesanan.updateStatus(StatusPesanan.SELESAI);
    }

    private record ResolvedItem(Menu menu, int qty) { }
}