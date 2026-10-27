package quickbite.controller.dto;

public record PesananInfo(String idPesanan, String namaPelanggan, String tanggal, String status, double totalBiaya, int jumlahItem, boolean terverifikasi) { }