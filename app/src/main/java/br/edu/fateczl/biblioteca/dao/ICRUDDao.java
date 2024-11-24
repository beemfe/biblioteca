/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.dao;

import java.util.List;

public interface ICRUDDao<T> {
    public String inserir(T obj);
    public String atualizar(T obj);
    public String excluir(T obj);
    public T consultar(T obj);
    public List<T> listar();
}