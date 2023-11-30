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
import benicio.solucoes.appcontrole.model.DoacaoModel;

public class AdapterDoacao extends RecyclerView.Adapter<AdapterDoacao.MyViewHolder>{
    List<DoacaoModel> lista;
    Context c;

    public AdapterDoacao(List<DoacaoModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterDoacao.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.generic_info_layout, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            DoacaoModel doacaoModel = lista.get(position);

            holder.info.setText(doacaoModel.toString());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView info;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.text_info_generic);
        }
    }
}
