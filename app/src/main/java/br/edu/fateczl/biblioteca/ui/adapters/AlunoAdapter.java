/*
 *@author: Felipe Bernardes Cisilo
 */
package br.edu.fateczl.biblioteca.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import br.edu.fateczl.biblioteca.R;
import br.edu.fateczl.biblioteca.model.Aluno;
import java.util.List;

public class AlunoAdapter extends RecyclerView.Adapter<AlunoAdapter.AlunoViewHolder> {
    private List<Aluno> alunos;
    private OnAlunoClickListener listener;

    public interface OnAlunoClickListener {
        void onAlunoClick(Aluno aluno);
    }

    public AlunoAdapter(List<Aluno> alunos, OnAlunoClickListener listener) {
        this.alunos = alunos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlunoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aluno, parent, false);
        return new AlunoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlunoViewHolder holder, int position) {
        Aluno aluno = alunos.get(position);
        holder.tvRA.setText(String.valueOf(aluno.getRA()));
        holder.tvNome.setText(aluno.getNome());
        holder.tvEmail.setText(aluno.getEmail());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAlunoClick(aluno);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public void updateData(List<Aluno> newAlunos) {
        this.alunos = newAlunos;
        notifyDataSetChanged();
    }

    static class AlunoViewHolder extends RecyclerView.ViewHolder {
        TextView tvRA;
        TextView tvNome;
        TextView tvEmail;

        AlunoViewHolder(View view) {
            super(view);
            tvRA = view.findViewById(R.id.tvRA);
            tvNome = view.findViewById(R.id.tvNome);
            tvEmail = view.findViewById(R.id.tvEmail);
        }
    }
}