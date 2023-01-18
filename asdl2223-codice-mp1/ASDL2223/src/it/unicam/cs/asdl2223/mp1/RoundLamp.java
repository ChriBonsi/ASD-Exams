/**
 * 
 */
package it.unicam.cs.asdl2223.mp1;

import static java.util.Objects.hash;

/**
 * Un oggetto di questa classe rappresenta una lampada che ha un appoggio
 * circolare. Implementa l'interfaccia ShelfItem, ma come lunghezza e larghezza
 * ha il diametro della base. Ridefinisce il metodo di default per calcolare la
 * superficie occupata restituendo l'area del cerchio che corrisponde alla
 * base. Una lampada è identificata dal nome e dal nome del brand.
 *
 * @author Luca Tesei (template)
 *         Christian Bonsignore christian.bonsignore@studenti.unicam.it (implementazione)
 *
 */
public class RoundLamp implements ShelfItem {

    private final double diameter;

    private final double weight;

    private final String name;

    private final String brandName;

    /**
     * @param diameter
     *                      diametro della base in cm
     * @param weight
     *                      peso in grammi
     * @param name
     *                      nome del modello della lampada
     * @param brandName
     *                      nome del brand della lampada
     */
    public RoundLamp(double diameter, double weight, String name, String brandName) {

        if (diameter >= 0) this.diameter = diameter;
        else throw new IllegalArgumentException("Il valore è negativo.");

        if (weight >= 0) this.weight = weight;
        else throw new IllegalArgumentException("Il valore è negativo.");

        if (name == null || brandName == null) {
            throw new NullPointerException("Il codice ISBN di un libro non può essere");
        }
        this.name = name;
        this.brandName = brandName;
    }

    /*
     * Restituisce l'area del cerchio corrispondente alla base
     */
    @Override
    public double getOccupiedSurface() {
        return Math.PI*Math.pow((this.diameter/2),2);
    }

    /*
     * Restituisce il diametro della base
     */
    @Override
    public double getLength() {
        return this.diameter;
    }

    /*
     * Restituisce il diametro della base
     */
    @Override
    public double getWidth() {
        return this.diameter;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * @return the diameter
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the brandName
     */
    public String getBrandName() {
        return brandName;
    }

    /*
     * Due lampade sono considerate uguali se hanno lo stesso nome e lo stesso
     * nome del brand.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("L'oggetto passato è nullo.");
        }
        if (this == obj) return true;
        if (!(obj instanceof RoundLamp)) return false;
        return (this.name.equals(((RoundLamp) obj).name) && this.brandName.equals(((RoundLamp) obj).brandName));
    }

    /*
     * L'hashcode viene calcolato usando gli stessi campi usati per definire
     * l'uguaglianza
     */
    @Override
    public int hashCode() {
        return hash(name, brandName);
    }

}
