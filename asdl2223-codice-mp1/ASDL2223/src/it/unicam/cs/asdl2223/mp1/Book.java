/**
 *
 */
package it.unicam.cs.asdl2223.mp1;

import static java.util.Objects.hash;

/**
 * Un oggetto di questa classe rappresenta un libro. Oltre le informazioni
 * tipiche ci sono la lunghezza, la larghezza e il peso. Queste servono per
 * implementare i metodi dell'interfaccia ShelfItem. Un libro è identificato dal
 * suo codice ISBN.
 *
 * @author Luca Tesei (template)
 *         Christian Bonsignore christian.bonsignore@studenti.unicam.it (implementazione)
 *
 */
public class Book implements ShelfItem {
    private final String author;

    private final String title;

    private final int year;

    private final String publisher;

    private final String iSBN;

    private final double length;

    private final double width;

    private final double weight;

    /**
     * @param author
     *                      stringa contenente l'autore
     * @param title
     *                      stringa contente il titolo
     * @param year
     *                      anno di pubblicazione
     * @param publisher
     *                      stringa con il nome dell'editore
     * @param iSBN
     *                      codice univoco del libro
     * @param length
     *                      lunghezza in cm
     * @param width
     *                      larghezza in cm
     * @param weight
     *                      peso in grammi
     */
    public Book(String author, String title, int year, String publisher, String iSBN, double length, double width, double weight) {
        this.author = author;
        this.title = title;

        if (year >= 0) this.year = year;
        else throw new IllegalArgumentException("Il valore è negativo.");

        this.publisher = publisher;

        if (iSBN == null) {
            throw new NullPointerException("Il codice ISBN di un libro non può essere");
        }
        this.iSBN = iSBN;

        if (length >= 0) this.length = length;
        else throw new IllegalArgumentException("Il valore è negativo.");

        if (width >= 0) this.width = width;
        else throw new IllegalArgumentException("Il valore è negativo.");

        if (weight >= 0) this.weight = weight;
        else throw new IllegalArgumentException("Il valore è negativo.");
    }

    @Override
    public double getLength() {
        return this.length;
    }

    @Override
    public double getWidth() {
        return this.width;
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @return the publisher
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * @return the iSBN
     */
    public String getiSBN() {
        return iSBN;
    }

    /*
     * Due libri sono uguali se e solo se hanno lo stesso codice ISBN.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            throw new NullPointerException("L'oggetto passato è nullo.");
        }
        if (this == obj) return true;
        if (!(obj instanceof Book)) return false;
        return this.iSBN.equals(((Book) obj).iSBN);
    }

    /*
     * Hashcode definito in base all'ISBN, per compatibilità con equals.
     */
    @Override
    public int hashCode() {
        return hash(this.iSBN);
    }
}
