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
import br.edu.fateczl.biblioteca.dao.AlunoDAO;
import br.edu.fateczl.biblioteca.model.Aluno;
import java.util.List;

public class AlunoFragment extends Fragment {
    private EditText edtRA;
    private EditText edtNome;
    private EditText edtEmail;
    private Button btnInserir;
    private Button btnAtualizar;
    private Button btnExcluir;
    private Button btnConsultar;
    private Button btnListar;
    private TextView tvListaAlunos;
    private AlunoDAO alunoDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aluno, container, false);
        inicializarComponentes(view);
        configurarListeners();
        atualizarLista();
        return view;
    }

    private void inicializarComponentes(View view) {
        edtRA = view.findViewById(R.id.edtRA);
        edtNome = view.findViewById(R.id.edtNome);
        edtEmail = view.findViewById(R.id.edtEmail);
        btnInserir = view.findViewById(R.id.btnInserir);
        btnAtualizar = view.findViewById(R.id.btnAtualizar);
        btnExcluir = view.findViewById(R.id.btnExcluir);
        btnConsultar = view.findViewById(R.id.btnConsultar);
        btnListar = view.findViewById(R.id.btnListar);
        tvListaAlunos = view.findViewById(R.id.tvLista);
        alunoDAO = new AlunoDAO(getContext());
    }

    private void configurarListeners() {
        btnInserir.setOnClickListener(v -> inserirAluno());
        btnAtualizar.setOnClickListener(v -> atualizarAluno());
        btnExcluir.setOnClickListener(v -> excluirAluno());
        btnConsultar.setOnClickListener(v -> consultarAluno());
        btnListar.setOnClickListener(v -> atualizarLista());
    }

    private void inserirAluno() {
        try {
            Aluno aluno = getAlunoFromInputs();
            String resultado = alunoDAO.inserir(aluno);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao inserir aluno", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarAluno() {
        try {
            Aluno aluno = getAlunoFromInputs();
            String resultado = alunoDAO.atualizar(aluno);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
            atualizarLista();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao atualizar aluno", Toast.LENGTH_SHORT).show();
        }
    }

    private void excluirAluno() {
        try {
            Aluno aluno = getAlunoFromInputs();
            String resultado = alunoDAO.excluir(aluno);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
            atualizarLista();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao excluir aluno", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarAluno() {
        try {
            Aluno aluno = new Aluno();
            aluno.setRA(Integer.parseInt(edtRA.getText().toString()));
            aluno = alunoDAO.consultar(aluno);
            if (aluno != null) {
                edtNome.setText(aluno.getNome());
                edtEmail.setText(aluno.getEmail());
            } else {
                Toast.makeText(getContext(), "Aluno não encontrado", Toast.LENGTH_SHORT).show();
                limparCampos();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao consultar aluno", Toast.LENGTH_SHORT).show();
        }
    }

    private Aluno getAlunoFromInputs() {
        Aluno aluno = new Aluno();
        aluno.setRA(Integer.parseInt(edtRA.getText().toString()));
        aluno.setNome(edtNome.getText().toString());
        aluno.setEmail(edtEmail.getText().toString());
        return aluno;
    }

    private void limparCampos() {
        edtRA.setText("");
        edtNome.setText("");
        edtEmail.setText("");
    }

    private void atualizarLista() {
        List<Aluno> alunos = alunoDAO.listar();
        StringBuilder sb = new StringBuilder();
        for (Aluno aluno : alunos) {
            sb.append("RA: ").append(aluno.getRA())
                    .append(" - Nome: ").append(aluno.getNome())
                    .append(" - Email: ").append(aluno.getEmail())
                    .append("\n");
        }
        tvListaAlunos.setText(sb.toString());
    }
}
