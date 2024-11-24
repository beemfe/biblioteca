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
import br.edu.fateczl.biblioteca.model.Exemplar;
import br.edu.fateczl.biblioteca.model.Livro;
import br.edu.fateczl.biblioteca.model.Revista;
import java.util.List;

public class ExemplarAdapter extends RecyclerView.Adapter<ExemplarAdapter.ExemplarViewHolder> {
    private List<Exemplar> exemplares;
    private OnExemplarClickListener listener;

    public interface OnExemplarClickListener {
        void onExemplarClick(Exemplar exemplar);
    }

    public ExemplarAdapter(List<Exemplar> exemplares, OnExemplarClickListener listener) {
        this.exemplares = exemplares;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExemplarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exemplar, parent, false);
        return new ExemplarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExemplarViewHolder holder, int position) {
        Exemplar exemplar = exemplares.get(position);
        holder.tvCodigo.setText(String.valueOf(exemplar.getCodigo()));
        holder.tvNome.setText(exemplar.getNome());
        holder.tvQtdPaginas.setText(String.valueOf(exemplar.getQtdPaginas()));

        if (exemplar instanceof Livro) {
            Livro livro = (Livro) exemplar;
            holder.tvTipo.setText("Livro");
            holder.tvIdentificador.setText("ISBN: " + livro.getISBN());
            holder.tvExtra.setText("Edição: " + livro.getEdicao());
        } else if (exemplar instanceof Revista) {
            Revista revista = (Revista) exemplar;
            holder.tvTipo.setText("Revista");
            holder.tvIdentificador.setText("ISSN: " + revista.getISSN());
            holder.tvExtra.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onExemplarClick(exemplar);
            }
        });
    }

    @Override
    public int getItemCount() {
        return exemplares.size();
    }

    public void updateData(List<Exemplar> newExemplares) {
        this.exemplares = newExemplares;
        notifyDataSetChanged();
    }

    static class ExemplarViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigo;
        TextView tvNome;
        TextView tvQtdPaginas;
        TextView tvTipo;
        TextView tvIdentificador;
        TextView tvExtra;

        ExemplarViewHolder(View view) {
            super(view);
            tvCodigo = view.findViewById(R.id.tvCodigo);
            tvNome = view.findViewById(R.id.tvNome);
            tvQtdPaginas = view.findViewById(R.id.tvQtdPaginas);
            tvTipo = view.findViewById(R.id.tvTipo);
            tvIdentificador = view.findViewById(R.id.tvIdentificador);
            tvExtra = view.findViewById(R.id.tvExtra);
        }
    }
}