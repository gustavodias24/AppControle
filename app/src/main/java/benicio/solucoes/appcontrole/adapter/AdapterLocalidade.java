package benicio.solucoes.appcontrole.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.solucoes.appcontrole.R;
import benicio.solucoes.appcontrole.model.LocalidadeModel;

public class AdapterLocalidade extends RecyclerView.Adapter<AdapterLocalidade.MyViewHolder> {

    List<LocalidadeModel> lista;
    Context c;

    public AdapterLocalidade(List<LocalidadeModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.generic_info_layout, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LocalidadeModel localidade = lista.get(position);

        holder.info.setText(
                localidade.toString()
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView info;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.text_info_generic);
        }
    }
}
