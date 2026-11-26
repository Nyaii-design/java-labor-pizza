package hu.nje.pizza.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/** rendeles (az, pizzanev, darab, felvetel, kiszallitas) */
@Entity
@Table(name = "rendeles")
public class Rendeles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long az;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pizzanev", nullable = false)
    private Pizza pizza;

    @Column(nullable = false)
    private int darab;

    private LocalDateTime felvetel;

    private LocalDateTime kiszallitas;

    public Long getAz() { return az; }

    public Pizza getPizza() { return pizza; }
    public void setPizza(Pizza pizza) { this.pizza = pizza; }

    public int getDarab() { return darab; }
    public void setDarab(int darab) { this.darab = darab; }

    public LocalDateTime getFelvetel() { return felvetel; }
    public void setFelvetel(LocalDateTime felvetel) { this.felvetel = felvetel; }

    public LocalDateTime getKiszallitas() { return kiszallitas; }
    public void setKiszallitas(LocalDateTime kiszallitas) { this.kiszallitas = kiszallitas; }

    /** A rendelés értéke forintban: darab × a pizza kategóriájának ára. */
    public int getErtek() {
        return darab * pizza.getKategoria().getAr();
    }
}
