CREATE TABLE pelanggan (
    id_pelanggan   VARCHAR(255) PRIMARY KEY,
    nama           VARCHAR(255),
    email          VARCHAR(255),
    nomor_telepon  VARCHAR(255),
    alamat         VARCHAR(255)
);
CREATE TABLE menu (
    id_menu    VARCHAR(255) PRIMARY KEY,
    nama_menu  VARCHAR(255),
    harga      DOUBLE PRECISION NOT NULL,
    stok       INTEGER NOT NULL
);
CREATE TABLE pesanan (
    id_pesanan    VARCHAR(255) PRIMARY KEY,
    tanggal       TIMESTAMP,
    total_biaya   DOUBLE PRECISION NOT NULL,
    status        VARCHAR(255),
    id_pelanggan  VARCHAR(255) NOT NULL REFERENCES pelanggan(id_pelanggan)
);
CREATE TABLE item_pesanan (
    id_item     VARCHAR(255) PRIMARY KEY,
    jumlah      INTEGER NOT NULL,
    subtotal    DOUBLE PRECISION NOT NULL,
    id_pesanan  VARCHAR(255) NOT NULL REFERENCES pesanan(id_pesanan),
    id_menu     VARCHAR(255) NOT NULL REFERENCES menu(id_menu)
);
CREATE TABLE pembayaran (
    id_pembayaran   VARCHAR(255) PRIMARY KEY,
    jumlah_bayar    DOUBLE PRECISION NOT NULL,
    metode          VARCHAR(255),
    status_bayar    VARCHAR(255),
    kode_referensi  VARCHAR(255),
    id_pesanan      VARCHAR(255) NOT NULL UNIQUE REFERENCES pesanan(id_pesanan)
);