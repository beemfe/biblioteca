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
import br.edu.fateczl.biblioteca.model.Revista;
import java.util.List;
import java.util.stream.Collectors;

public class RevistaFragment extends Fragment {
    private EditText edtCodigo;
    private EditText edtNome;
    private EditText edtQtdPaginas;
    private EditText edtISSN;
    private Button btnInserir;
    private Button btnAtualizar;
    private Button btnExcluir;
    private Button btnConsultar;
    private Button btnListar;
    private TextView tvListaRevistas;
    private ExemplarDAO exemplarDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_revista, container, false);
        inicializarComponentes(view);
        configurarListeners();
        atualizarLista();
        return view;
    }

    private void inicializarComponentes(View view) {
        edtCodigo = view.findViewById(R.id.edtCodigo);
        edtNome = view.findViewById(R.id.edtNome);
        edtQtdPaginas = view.findViewById(R.id.edtQtdPaginas);
        edtISSN = view.findViewById(R.id.edtISSN);
        btnInserir = view.findViewById(R.id.btnInserir);
        btnAtualizar = view.findViewById(R.id.btnAtualizar);
        btnExcluir = view.findViewById(R.id.btnExcluir);
        btnConsultar = view.findViewById(R.id.btnConsultar);
        btnListar = view.findViewById(R.id.btnListar);
        tvListaRevistas = view.findViewById(R.id.tvLista);
        exemplarDAO = new ExemplarDAO(getContext());
    }

    private void configurarListeners() {
        btnInserir.setOnClickListener(v -> inserirRevista());
        btnAtualizar.setOnClickListener(v -> atualizarRevista());
        btnExcluir.setOnClickListener(v -> excluirRevista());
        btnConsultar.setOnClickListener(v -> consultarRevista());
        btnListar.setOnClickListener(v -> atualizarLista());
    }

    private void inserirRevista() {
        try {
            Revista revista = getRevistaFromInputs();
            String resultado = exemplarDAO.inserir(revista);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao inserir revista", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarRevista() {
        try {
            Revista revista = getRevistaFromInputs();
            String resultado = exemplarDAO.atualizar(revista);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
            atualizarLista();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao atualizar revista", Toast.LENGTH_SHORT).show();
        }
    }

    private void excluirRevista() {
        try {
            Revista revista = getRevistaFromInputs();
            String resultado = exemplarDAO.excluir(revista);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
            atualizarLista();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao excluir revista", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarRevista() {
        try {
            Revista revista = new Revista();
            revista.setCodigo(Integer.parseInt(edtCodigo.getText().toString()));
            revista = (Revista) exemplarDAO.consultar(revista);
            if (revista != null) {
                edtNome.setText(revista.getNome());
                edtQtdPaginas.setText(String.valueOf(revista.getQtdPaginas()));
                edtISSN.setText(revista.getISSN());
            } else {
                Toast.makeText(getContext(), "Revista não encontrada", Toast.LENGTH_SHORT).show();
                limparCampos();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao consultar revista", Toast.LENGTH_SHORT).show();
        }
    }

    private Revista getRevistaFromInputs() {
        Revista revista = new Revista();
        revista.setCodigo(Integer.parseInt(edtCodigo.getText().toString()));
        revista.setNome(edtNome.getText().toString());
        revista.setQtdPaginas(Integer.parseInt(edtQtdPaginas.getText().toString()));
        revista.setISSN(edtISSN.getText().toString());
        return revista;
    }

    private void limparCampos() {
        edtCodigo.setText("");
        edtNome.setText("");
        edtQtdPaginas.setText("");
        edtISSN.setText("");
    }

    private void atualizarLista() {
        List<Revista> revistas = exemplarDAO.listar().stream()
                .filter(e -> e instanceof Revista)
                .map(e -> (Revista) e)
                .collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (Revista revista : revistas) {
            sb.append("Código: ").append(revista.getCodigo())
                    .append(" - Nome: ").append(revista.getNome())
                    .append(" - ISSN: ").append(revista.getISSN())
                    .append("\n");
        }
        tvListaRevistas.setText(sb.toString());
    }
}