/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.model;

public abstract class Exemplar {
    private int codigo;
    private String nome;
    private int qtdPaginas;

    public Exemplar() {
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdPaginas() {
        return qtdPaginas;
    }

    public void setQtdPaginas(int qtdPaginas) {
        this.qtdPaginas = qtdPaginas;
    }
}