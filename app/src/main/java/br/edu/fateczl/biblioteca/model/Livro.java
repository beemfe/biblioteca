/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.model;

public class Livro extends Exemplar {
    private String ISBN;
    private int edicao;

    public Livro() {
        super();
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getEdicao() {
        return edicao;
    }

    public void setEdicao(int edicao) {
        this.edicao = edicao;
    }
}
