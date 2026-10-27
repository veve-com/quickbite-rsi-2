package quickbite.controller.dto;

import java.util.List;

public record EstimasiResult(List<Baris> baris, double total) {
    public record Baris(String idMenu, String namaMenu, int qty,
                        double subtotal, boolean stokCukup) { }
}