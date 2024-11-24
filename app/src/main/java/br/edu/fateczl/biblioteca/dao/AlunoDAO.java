/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.fateczl.biblioteca.database.DatabaseHelper;
import br.edu.fateczl.biblioteca.model.Aluno;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO implements ICRUDDao<Aluno> {
    private SQLiteDatabase db;
    private static final String TABLE = "aluno";

    public AlunoDAO(Context context) {
        DatabaseHelper dh = new DatabaseHelper(context);
        db = dh.getWritableDatabase();
    }

    @Override
    public String inserir(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("ra", aluno.getRA());
        values.put("nome", aluno.getNome());
        values.put("email", aluno.getEmail());

        long id = db.insert(TABLE, null, values);
        return id != -1 ? "Aluno inserido com sucesso" : "Erro ao inserir aluno";
    }

    @Override
    public String atualizar(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nome", aluno.getNome());
        values.put("email", aluno.getEmail());

        String[] whereArgs = {String.valueOf(aluno.getRA())};
        int count = db.update(TABLE, values, "ra = ?", whereArgs);
        return count > 0 ? "Aluno atualizado com sucesso" : "Erro ao atualizar aluno";
    }

    @Override
    public String excluir(Aluno aluno) {
        String[] whereArgs = {String.valueOf(aluno.getRA())};
        int count = db.delete(TABLE, "ra = ?", whereArgs);
        return count > 0 ? "Aluno excluído com sucesso" : "Erro ao excluir aluno";
    }

    @Override
    public Aluno consultar(Aluno aluno) {
        String[] colunas = {"ra", "nome", "email"};
        String[] whereArgs = {String.valueOf(aluno.getRA())};
        Cursor cursor = db.query(TABLE, colunas, "ra = ?", whereArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                Aluno a = new Aluno();
                int raIndex = cursor.getColumnIndexOrThrow("ra");
                int nomeIndex = cursor.getColumnIndexOrThrow("nome");
                int emailIndex = cursor.getColumnIndexOrThrow("email");

                a.setRA(cursor.getInt(raIndex));
                a.setNome(cursor.getString(nomeIndex));
                a.setEmail(cursor.getString(emailIndex));
                return a;
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
    public List<Aluno> listar() {
        List<Aluno> alunos = new ArrayList<>();
        String[] colunas = {"ra", "nome", "email"};
        Cursor cursor = db.query(TABLE, colunas, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int raIndex = cursor.getColumnIndexOrThrow("ra");
                int nomeIndex = cursor.getColumnIndexOrThrow("nome");
                int emailIndex = cursor.getColumnIndexOrThrow("email");

                do {
                    Aluno a = new Aluno();
                    a.setRA(cursor.getInt(raIndex));
                    a.setNome(cursor.getString(nomeIndex));
                    a.setEmail(cursor.getString(emailIndex));
                    alunos.add(a);
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
        return alunos;
    }
}