/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "biblioteca.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE aluno (" +
                "ra INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "email TEXT)");

        db.execSQL("CREATE TABLE exemplar (" +
                "codigo INTEGER PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "qtdPaginas INTEGER, " +
                "tipo TEXT NOT NULL, " +
                "isbn TEXT, " +
                "edicao INTEGER, " +
                "issn TEXT)");

        db.execSQL("CREATE TABLE aluguel (" +
                "ra_aluno INTEGER, " +
                "codigo_exemplar INTEGER, " +
                "data_retirada TEXT NOT NULL, " +
                "data_devolucao TEXT, " +
                "PRIMARY KEY (ra_aluno, codigo_exemplar), " +
                "FOREIGN KEY (ra_aluno) REFERENCES aluno(ra), " +
                "FOREIGN KEY (codigo_exemplar) REFERENCES exemplar(codigo))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS aluguel");
        db.execSQL("DROP TABLE IF EXISTS exemplar");
        db.execSQL("DROP TABLE IF EXISTS aluno");
        onCreate(db);
    }
}