package hu.nje.pizza.entity;

import jakarta.persistence.*;

/** kategoria (nev, ar) – a kategóriába tartozó pizzák ára. */
@Entity
@Table(name = "kategoria")
public class Kategoria {

    @Id
    @Column(length = 50)
    private String nev;

    @Column(nullable = false)
    private int ar;

    public Kategoria() {
    }

    public Kategoria(String nev, int ar) {
        this.nev = nev;
        this.ar = ar;
    }

    public String getNev() { return nev; }
    public void setNev(String nev) { this.nev = nev; }

    public int getAr() { return ar; }
    public void setAr(int ar) { this.ar = ar; }
}
