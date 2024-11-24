/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import br.edu.fateczl.biblioteca.R;
import br.edu.fateczl.biblioteca.dao.ExemplarDAO;
import br.edu.fateczl.biblioteca.model.Livro;
import java.util.List;
import java.util.stream.Collectors;

public class LivroFragment extends Fragment {
    private EditText edtCodigo;
    private EditText edtNome;
    private EditText edtQtdPaginas;
    private EditText edtISBN;
    private EditText edtEdicao;
    private Button btnInserir;
    private Button btnAtualizar;
    private Button btnExcluir;
    private Button btnConsultar;
    private TextView tvListaLivros;
    private ExemplarDAO exemplarDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_livro, container, false);
        inicializarComponentes(view);
        configurarListeners();
        atualizarLista();
        return view;
    }

    private void inicializarComponentes(View view) {
        edtCodigo = view.findViewById(R.id.edtCodigo);
        edtNome = view.findViewById(R.id.edtNome);
        edtQtdPaginas = view.findViewById(R.id.edtQtdPaginas);
        edtISBN = view.findViewById(R.id.edtISBN);
        edtEdicao = view.findViewById(R.id.edtEdicao);
        btnInserir = view.findViewById(R.id.btnInserir);
        btnAtualizar = view.findViewById(R.id.btnAtualizar);
        btnExcluir = view.findViewById(R.id.btnExcluir);
        btnConsultar = view.findViewById(R.id.btnConsultar);
        tvListaLivros = view.findViewById(R.id.tvListaLivros);
        exemplarDAO = new ExemplarDAO(getContext());
    }

    private void configurarListeners() {
        btnInserir.setOnClickListener(v -> inserirLivro());
        btnAtualizar.setOnClickListener(v -> atualizarLivro());
        btnExcluir.setOnClickListener(v -> excluirLivro());
        btnConsultar.setOnClickListener(v -> consultarLivro());
        tvListaLivros.setOnClickListener(v -> atualizarLista());
    }

    private void inserirLivro() {
        try {
            Livro livro = getLivroFromInputs();
            String resultado = exemplarDAO.inserir(livro);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao inserir livro", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarLivro() {
        try {
            Livro livro = getLivroFromInputs();
            String resultado = exemplarDAO.atualizar(livro);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao atualizar livro", Toast.LENGTH_SHORT).show();
        }
    }

    private void excluirLivro() {
        try {
            Livro livro = getLivroFromInputs();
            String resultado = exemplarDAO.excluir(livro);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
            atualizarLista();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao excluir livro", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarLivro() {
        try {
            Livro livro = new Livro();
            livro.setCodigo(Integer.parseInt(edtCodigo.getText().toString()));
            livro = (Livro) exemplarDAO.consultar(livro);
            if (livro != null) {
                edtNome.setText(livro.getNome());
                edtQtdPaginas.setText(String.valueOf(livro.getQtdPaginas()));
                edtISBN.setText(livro.getISBN());
                edtEdicao.setText(String.valueOf(livro.getEdicao()));
            } else {
                Toast.makeText(getContext(), "Livro não encontrado", Toast.LENGTH_SHORT).show();
                limparCampos();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao consultar livro", Toast.LENGTH_SHORT).show();
        }
    }

    private Livro getLivroFromInputs() {
        Livro livro = new Livro();
        livro.setCodigo(Integer.parseInt(edtCodigo.getText().toString()));
        livro.setNome(edtNome.getText().toString());
        livro.setQtdPaginas(Integer.parseInt(edtQtdPaginas.getText().toString()));
        livro.setISBN(edtISBN.getText().toString());
        livro.setEdicao(Integer.parseInt(edtEdicao.getText().toString()));
        return livro;
    }

    private void limparCampos() {
        edtCodigo.setText("");
        edtNome.setText("");
        edtQtdPaginas.setText("");
        edtISBN.setText("");
        edtEdicao.setText("");
    }

    private void atualizarLista() {
        List<Livro> livros = exemplarDAO.listar().stream()
                .filter(e -> e instanceof Livro)
                .map(e -> (Livro) e)
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (Livro livro : livros) {
            sb.append("Código: ").append(livro.getCodigo())
                    .append(" - Nome: ").append(livro.getNome())
                    .append(" - ISBN: ").append(livro.getISBN())
                    .append(" - Edição: ").append(livro.getEdicao())
                    .append("\n");
        }
        tvListaLivros.setText(sb.toString());
    }
}