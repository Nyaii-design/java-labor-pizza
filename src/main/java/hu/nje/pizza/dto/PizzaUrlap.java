package hu.nje.pizza.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** A CRUD menü pizza-űrlapja. */
public class PizzaUrlap {

    @NotBlank(message = "A pizza nevének megadása kötelező.")
    @Size(max = 50, message = "A név legfeljebb 50 karakter lehet.")
    private String nev;

    @NotBlank(message = "Válassz kategóriát.")
    private String kategoriaNev;

    private boolean vegetarianus;

    public String getNev() { return nev; }
    public void setNev(String nev) { this.nev = nev; }

    public String getKategoriaNev() { return kategoriaNev; }
    public void setKategoriaNev(String kategoriaNev) { this.kategoriaNev = kategoriaNev; }

    public boolean isVegetarianus() { return vegetarianus; }
    public void setVegetarianus(boolean vegetarianus) { this.vegetarianus = vegetarianus; }
}
