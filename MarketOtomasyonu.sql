CREATE DATABASE MarketOtomasyonu;
USE MarketOtomasyonu;

CREATE TABLE adminler (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kullanici_adi VARCHAR(50) NOT NULL,
    sifre VARCHAR(50) NOT NULL
);

INSERT INTO adminler (kullanici_adi, sifre) VALUES ('admin', '1234');

CREATE TABLE urunler (
    id INT AUTO_INCREMENT PRIMARY KEY,
    urun_adi VARCHAR(100),
    fiyat DOUBLE,
    stok INT,
    tur VARCHAR(20),
    son_kullanim_tarihi VARCHAR(20), 
    garanti_suresi VARCHAR(20),
    marka VARCHAR(50)
);