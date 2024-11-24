/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.edu.fateczl.biblioteca.database.DatabaseHelper;
import br.edu.fateczl.biblioteca.model.Aluguel;
import br.edu.fateczl.biblioteca.model.Aluno;
import br.edu.fateczl.biblioteca.model.Exemplar;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AluguelDAO implements ICRUDDao<Aluguel> {
    private SQLiteDatabase db;
    private static final String TABLE = "aluguel";
    private AlunoDAO alunoDAO;
    private ExemplarDAO exemplarDAO;

    public AluguelDAO(Context context) {
        DatabaseHelper dh = new DatabaseHelper(context);
        db = dh.getWritableDatabase();
        alunoDAO = new AlunoDAO(context);
        exemplarDAO = new ExemplarDAO(context);
    }

    @Override
    public String inserir(Aluguel aluguel) {
        try {
            ContentValues values = new ContentValues();
            values.put("ra_aluno", aluguel.getAluno().getRA());
            values.put("codigo_exemplar", aluguel.getExemplar().getCodigo());
            values.put("data_retirada", aluguel.getDataRetirada().toString());
            Aluno aluno = alunoDAO.consultar(aluguel.getAluno());
            if (aluno == null) {
                return "Aluno não encontrado";
            }

            Exemplar exemplar = exemplarDAO.consultar(aluguel.getExemplar());
            if (exemplar == null) {
                return "Exemplar não encontrado";
            }

            Cursor cursor = db.query(TABLE, null,
                    "codigo_exemplar = ? AND data_devolucao IS NULL",
                    new String[]{String.valueOf(aluguel.getExemplar().getCodigo())},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                cursor.close();
                return "Exemplar já está alugado";
            }

            long id = db.insert(TABLE, null, values);
            return id != -1 ? "Aluguel realizado com sucesso" : "Erro ao realizar aluguel";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao realizar aluguel: " + e.getMessage();
        }
    }

    @Override
    public String atualizar(Aluguel aluguel) {
        ContentValues values = new ContentValues();
        values.put("data_devolucao", aluguel.getDataDevolucao().toString());

        String[] whereArgs = {
                String.valueOf(aluguel.getAluno().getRA()),
                String.valueOf(aluguel.getExemplar().getCodigo())
        };

        int count = db.update(TABLE, values, "ra_aluno = ? AND codigo_exemplar = ?", whereArgs);
        return count > 0 ? "Aluguel atualizado com sucesso" : "Erro ao atualizar aluguel";
    }

    @Override
    public String excluir(Aluguel aluguel) {
        String[] whereArgs = {
                String.valueOf(aluguel.getAluno().getRA()),
                String.valueOf(aluguel.getExemplar().getCodigo())
        };

        int count = db.delete(TABLE, "ra_aluno = ? AND codigo_exemplar = ?", whereArgs);
        return count > 0 ? "Aluguel excluído com sucesso" : "Erro ao excluir aluguel";
    }

    @Override
    public Aluguel consultar(Aluguel aluguel) {
        String[] colunas = {"ra_aluno", "codigo_exemplar", "data_retirada", "data_devolucao"};
        String[] whereArgs = {
                String.valueOf(aluguel.getAluno().getRA()),
                String.valueOf(aluguel.getExemplar().getCodigo())
        };

        Cursor cursor = db.query(TABLE, colunas, "ra_aluno = ? AND codigo_exemplar = ?",
                whereArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int raAlunoIndex = cursor.getColumnIndexOrThrow("ra_aluno");
                int codigoExemplarIndex = cursor.getColumnIndexOrThrow("codigo_exemplar");
                int dataRetiradaIndex = cursor.getColumnIndexOrThrow("data_retirada");
                int dataDevolucaoIndex = cursor.getColumnIndexOrThrow("data_devolucao");

                Aluguel a = new Aluguel();

                Aluno aluno = new Aluno();
                aluno.setRA(cursor.getInt(raAlunoIndex));
                a.setAluno(alunoDAO.consultar(aluno));

                Exemplar exemplar = new Exemplar() {};
                exemplar.setCodigo(cursor.getInt(codigoExemplarIndex));
                a.setExemplar(exemplarDAO.consultar(exemplar));

                String dataRetirada = cursor.getString(dataRetiradaIndex);
                if (dataRetirada != null) {
                    a.setDataRetirada(LocalDate.parse(dataRetirada));
                }

                String dataDevolucao = cursor.getString(dataDevolucaoIndex);
                if (dataDevolucao != null) {
                    a.setDataDevolucao(LocalDate.parse(dataDevolucao));
                }

                return a;
            } catch (IllegalArgumentException | DateTimeParseException e) {
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
    public List<Aluguel> listar() {
        List<Aluguel> alugueis = new ArrayList<>();
        String[] colunas = {"ra_aluno", "codigo_exemplar", "data_retirada", "data_devolucao"};
        Cursor cursor = db.query(TABLE, colunas, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            try {
                int raAlunoIndex = cursor.getColumnIndexOrThrow("ra_aluno");
                int codigoExemplarIndex = cursor.getColumnIndexOrThrow("codigo_exemplar");
                int dataRetiradaIndex = cursor.getColumnIndexOrThrow("data_retirada");
                int dataDevolucaoIndex = cursor.getColumnIndexOrThrow("data_devolucao");

                do {
                    Aluguel a = new Aluguel();

                    Aluno aluno = new Aluno();
                    aluno.setRA(cursor.getInt(raAlunoIndex));
                    a.setAluno(alunoDAO.consultar(aluno));

                    Exemplar exemplar = new Exemplar() {};
                    exemplar.setCodigo(cursor.getInt(codigoExemplarIndex));
                    a.setExemplar(exemplarDAO.consultar(exemplar));

                    String dataRetirada = cursor.getString(dataRetiradaIndex);
                    if (dataRetirada != null) {
                        a.setDataRetirada(LocalDate.parse(dataRetirada));
                    }

                    String dataDevolucao = cursor.getString(dataDevolucaoIndex);
                    if (dataDevolucao != null) {
                        a.setDataDevolucao(LocalDate.parse(dataDevolucao));
                    }

                    alugueis.add(a);
                } while (cursor.moveToNext());
            } catch (IllegalArgumentException | DateTimeParseException e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return alugueis;
    }
}