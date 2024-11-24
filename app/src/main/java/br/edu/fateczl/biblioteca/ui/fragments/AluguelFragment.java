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
import br.edu.fateczl.biblioteca.dao.AluguelDAO;
import br.edu.fateczl.biblioteca.dao.AlunoDAO;
import br.edu.fateczl.biblioteca.dao.ExemplarDAO;
import br.edu.fateczl.biblioteca.model.Aluguel;
import br.edu.fateczl.biblioteca.model.Aluno;
import br.edu.fateczl.biblioteca.model.Exemplar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AluguelFragment extends Fragment {
    private EditText edtRA;
    private EditText edtCodigo;
    private Button btnAlugar;
    private Button btnDevolver;
    private Button btnConsultar;
    private Button btnListar;
    private TextView tvListaAlugueis;
    private AluguelDAO aluguelDAO;
    private AlunoDAO alunoDAO;
    private ExemplarDAO exemplarDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aluguel, container, false);
        inicializarComponentes(view);
        configurarListeners();
        atualizarLista();
        return view;
    }

    private void inicializarComponentes(View view) {
        edtRA = view.findViewById(R.id.edtRA);
        edtCodigo = view.findViewById(R.id.edtCodigo);
        btnAlugar = view.findViewById(R.id.btnAlugar);
        btnDevolver = view.findViewById(R.id.btnDevolver);
        btnConsultar = view.findViewById(R.id.btnConsultar);
        btnListar = view.findViewById(R.id.btnListar);
        tvListaAlugueis = view.findViewById(R.id.tvLista);
        aluguelDAO = new AluguelDAO(getContext());
        alunoDAO = new AlunoDAO(getContext());
        exemplarDAO = new ExemplarDAO(getContext());
    }

    private void configurarListeners() {
        btnAlugar.setOnClickListener(v -> realizarAluguel());
        btnDevolver.setOnClickListener(v -> realizarDevolucao());
        btnConsultar.setOnClickListener(v -> consultarAluguel());
        btnListar.setOnClickListener(v -> atualizarLista());
    }

    private void realizarAluguel() {
        try {
            Aluno aluno = new Aluno();
            aluno.setRA(Integer.parseInt(edtRA.getText().toString()));
            aluno = alunoDAO.consultar(aluno);
            if (aluno == null) {
                Toast.makeText(getContext(), "Aluno não encontrado", Toast.LENGTH_SHORT).show();
                return;
            }
            Exemplar exemplar = new Exemplar() {};
            exemplar.setCodigo(Integer.parseInt(edtCodigo.getText().toString()));
            exemplar = exemplarDAO.consultar(exemplar);
            if (exemplar == null) {
                Toast.makeText(getContext(), "Exemplar não encontrado", Toast.LENGTH_SHORT).show();
                return;
            }
            Aluguel aluguel = new Aluguel();
            aluguel.setAluno(aluno);
            aluguel.setExemplar(exemplar);
            aluguel.setDataRetirada(LocalDate.now());
            String resultado = aluguelDAO.inserir(aluguel);
            Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
            limparCampos();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao realizar aluguel", Toast.LENGTH_SHORT).show();
        }
    }

    private void realizarDevolucao() {
        try {
            Aluguel aluguel = getAluguelFromInputs();
            aluguel = aluguelDAO.consultar(aluguel);
            if (aluguel != null) {
                aluguel.setDataDevolucao(LocalDate.now());
                String resultado = aluguelDAO.atualizar(aluguel);
                Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
                limparCampos();
                atualizarLista();
            } else {
                Toast.makeText(getContext(), "Aluguel não encontrado", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao realizar devolução", Toast.LENGTH_SHORT).show();
        }
    }

    private void consultarAluguel() {
        try {
            Aluguel aluguel = getAluguelFromInputs();
            aluguel = aluguelDAO.consultar(aluguel);
            if (aluguel != null) {
                edtRA.setText(String.valueOf(aluguel.getAluno().getRA()));
                edtCodigo.setText(String.valueOf(aluguel.getExemplar().getCodigo()));
            } else {
                Toast.makeText(getContext(), "Aluguel não encontrado", Toast.LENGTH_SHORT).show();
                limparCampos();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Erro ao consultar aluguel", Toast.LENGTH_SHORT).show();
        }
    }

    private Aluguel getAluguelFromInputs() {
        Aluguel aluguel = new Aluguel();
        Aluno aluno = new Aluno();
        aluno.setRA(Integer.parseInt(edtRA.getText().toString()));
        Exemplar exemplar = new Exemplar() {};
        exemplar.setCodigo(Integer.parseInt(edtCodigo.getText().toString()));
        aluguel.setAluno(aluno);
        aluguel.setExemplar(exemplar);
        return aluguel;
    }

    private void limparCampos() {
        edtRA.setText("");
        edtCodigo.setText("");
    }

    private void atualizarLista() {
        List<Aluguel> alugueis = aluguelDAO.listar();
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Aluguel aluguel : alugueis) {
            sb.append("RA: ").append(aluguel.getAluno().getRA())
                    .append(" - Exemplar: ").append(aluguel.getExemplar().getCodigo())
                    .append(" - Retirada: ").append(aluguel.getDataRetirada().format(formatter))
                    .append(" - Devolução: ").append(aluguel.getDataDevolucao() != null ? aluguel.getDataDevolucao().format(formatter) : "Pendente")
                    .append("\n");
        }
        tvListaAlugueis.setText(sb.toString());
    }
}
