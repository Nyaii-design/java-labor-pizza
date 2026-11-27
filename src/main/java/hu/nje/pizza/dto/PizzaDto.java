package hu.nje.pizza.dto;

import hu.nje.pizza.entity.Pizza;

/** A REST API által visszaadott / fogadott pizza-adat (JSON). Az ár csak kimenet. */
public record PizzaDto(String nev, String kategoria, Integer ar, Boolean vegetarianus) {

    public static PizzaDto of(Pizza p) {
        return new PizzaDto(p.getNev(), p.getKategoria().getNev(), p.getKategoria().getAr(), p.isVegetarianus());
    }

    /** Ha a JSON-ban nincs megadva, akkor nem vegetáriánus. */
    public boolean veg() {
        return Boolean.TRUE.equals(vegetarianus);
    }
}
