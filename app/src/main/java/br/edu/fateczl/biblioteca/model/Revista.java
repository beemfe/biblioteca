/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.model;

public class Revista extends Exemplar {
    private String ISSN;

    public Revista() {
        super();
    }

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }
}
