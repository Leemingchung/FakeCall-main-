package com.example.fakecall.DAO;

public class ModelCharacter {
    String name , sdt ;
     byte[] hinhanh;

    public ModelCharacter() {
    }

    public ModelCharacter(String name, String sdt, byte[] hinhanh) {
        this.name = name;
        this.sdt = sdt;
        this.hinhanh = hinhanh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public byte[] getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(byte[] hinhanh) {
        this.hinhanh = hinhanh;
    }

}
