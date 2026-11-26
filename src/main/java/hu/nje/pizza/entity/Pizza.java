package hu.nje.pizza.entity;

import jakarta.persistence.*;

/** pizza (nev, kategorianev, vegetarianus) */
@Entity
@Table(name = "pizza")
public class Pizza {

    @Id
    @Column(length = 50)
    private String nev;

    @ManyToOne(optional = false)
    @JoinColumn(name = "kategorianev", nullable = false)
    private Kategoria kategoria;

    @Column(nullable = false)
    private boolean vegetarianus;

    public Pizza() {
    }

    public Pizza(String nev, Kategoria kategoria, boolean vegetarianus) {
        this.nev = nev;
        this.kategoria = kategoria;
        this.vegetarianus = vegetarianus;
    }

    public String getNev() { return nev; }
    public void setNev(String nev) { this.nev = nev; }

    public Kategoria getKategoria() { return kategoria; }
    public void setKategoria(Kategoria kategoria) { this.kategoria = kategoria; }

    public boolean isVegetarianus() { return vegetarianus; }
    public void setVegetarianus(boolean vegetarianus) { this.vegetarianus = vegetarianus; }
}
