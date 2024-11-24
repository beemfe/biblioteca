/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.model;

public class Aluno {
    private int RA;
    private String nome;
    private String email;

    public Aluno() {
    }

    public int getRA() {
        return RA;
    }

    public void setRA(int RA) {
        this.RA = RA;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}