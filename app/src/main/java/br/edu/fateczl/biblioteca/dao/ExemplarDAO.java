/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.fateczl.biblioteca.database.DatabaseHelper;
import br.edu.fateczl.biblioteca.model.Exemplar;
import br.edu.fateczl.biblioteca.model.Livro;
import br.edu.fateczl.biblioteca.model.Revista;
import java.util.ArrayList;
import java.util.List;

public class ExemplarDAO implements ICRUDDao<Exemplar> {
    private SQLiteDatabase db;
    private static final String TABLE = "exemplar";

    public ExemplarDAO(Context context) {
        DatabaseHelper dh = new DatabaseHelper(context);
        db = dh.getWritableDatabase();
    }

    @Override
    public String inserir(Exemplar exemplar) {
        ContentValues values = new ContentValues();
        values.put("codigo", exemplar.getCodigo());
        values.put("nome", exemplar.getNome());
        values.put("qtdPaginas", exemplar.getQtdPaginas());

        if (exemplar instanceof Livro) {
            values.put("tipo", "livro");
            values.put("isbn", ((Livro) exemplar).getISBN());
            values.put("edicao", ((Livro) exemplar).getEdicao());
        } else {
            values.put("tipo", "revista");
            values.put("issn", ((Revista) exemplar).getISSN());
        }

        long id = db.insert(TABLE, null, values);
        return id != -1 ? "Exemplar inserido com sucesso" : "Erro ao inserir exemplar";
    }

    @Override
    public String atualizar(Exemplar exemplar) {
        ContentValues values = new ContentValues();
        values.put("nome", exemplar.getNome());
        values.put("qtdPaginas", exemplar.getQtdPaginas());

        if (exemplar instanceof Livro) {
            values.put("isbn", ((Livro) exemplar).getISBN());
            values.put("edicao", ((Livro) exemplar).getEdicao());
        } else {
            values.put("issn", ((Revista) exemplar).getISSN());
        }

        String[] whereArgs = {String.valueOf(exemplar.getCodigo())};
        int count = db.update(TABLE, values, "codigo = ?", whereArgs);
        return count > 0 ? "Exemplar atualizado com sucesso" : "Erro ao atualizar exemplar";
    }

    @Override
    public String excluir(Exemplar exemplar) {
        String[] whereArgs = {String.valueOf(exemplar.getCodigo())};
        int count = db.delete(TABLE, "codigo = ?", whereArgs);
        return count > 0 ? "Exemplar excluído com sucesso" : "Erro ao excluir exemplar";
    }

    @Override
    public Exemplar consultar(Exemplar exemplar) {
        String[] colunas = {"codigo", "nome", "qtdPaginas", "tipo", "isbn", "edicao", "issn"};
        String[] whereArgs = {String.valueOf(exemplar.getCodigo())};
        Cursor cursor = db.query(TABLE, colunas, "codigo = ?", whereArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int tipoIndex = cursor.getColumnIndexOrThrow("tipo");
                int codigoIndex = cursor.getColumnIndexOrThrow("codigo");
                int nomeIndex = cursor.getColumnIndexOrThrow("nome");
                int qtdPaginasIndex = cursor.getColumnIndexOrThrow("qtdPaginas");

                String tipo = cursor.getString(tipoIndex);
                if ("livro".equals(tipo)) {
                    Livro l = new Livro();
                    l.setCodigo(cursor.getInt(codigoIndex));
                    l.setNome(cursor.getString(nomeIndex));
                    l.setQtdPaginas(cursor.getInt(qtdPaginasIndex));
                    l.setISBN(cursor.getString(cursor.getColumnIndexOrThrow("isbn")));
                    l.setEdicao(cursor.getInt(cursor.getColumnIndexOrThrow("edicao")));
                    return l;
                } else {
                    Revista r = new Revista();
                    r.setCodigo(cursor.getInt(codigoIndex));
                    r.setNome(cursor.getString(nomeIndex));
                    r.setQtdPaginas(cursor.getInt(qtdPaginasIndex));
                    r.setISSN(cursor.getString(cursor.getColumnIndexOrThrow("issn")));
                    return r;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    @Override
    public List<Exemplar> listar() {
        List<Exemplar> exemplares = new ArrayList<>();
        String[] colunas = {"codigo", "nome", "qtdPaginas", "tipo", "isbn", "edicao", "issn"};
        Cursor cursor = db.query(TABLE, colunas, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int tipoIndex = cursor.getColumnIndexOrThrow("tipo");
                int codigoIndex = cursor.getColumnIndexOrThrow("codigo");
                int nomeIndex = cursor.getColumnIndexOrThrow("nome");
                int qtdPaginasIndex = cursor.getColumnIndexOrThrow("qtdPaginas");

                do {
                    String tipo = cursor.getString(tipoIndex);
                    if ("livro".equals(tipo)) {
                        Livro l = new Livro();
                        l.setCodigo(cursor.getInt(codigoIndex));
                        l.setNome(cursor.getString(nomeIndex));
                        l.setQtdPaginas(cursor.getInt(qtdPaginasIndex));
                        l.setISBN(cursor.getString(cursor.getColumnIndexOrThrow("isbn")));
                        l.setEdicao(cursor.getInt(cursor.getColumnIndexOrThrow("edicao")));
                        exemplares.add(l);
                    } else {
                        Revista r = new Revista();
                        r.setCodigo(cursor.getInt(codigoIndex));
                        r.setNome(cursor.getString(nomeIndex));
                        r.setQtdPaginas(cursor.getInt(qtdPaginasIndex));
                        r.setISSN(cursor.getString(cursor.getColumnIndexOrThrow("issn")));
                        exemplares.add(r);
                    }
                } while (cursor.moveToNext());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return exemplares;
    }
}