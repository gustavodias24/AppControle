package benicio.solucoes.appcontrole.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.solucoes.appcontrole.PessoaActivity;
import benicio.solucoes.appcontrole.R;
import benicio.solucoes.appcontrole.model.FamiliaModel;

public class AdapterFamilia extends RecyclerView.Adapter<AdapterFamilia.MyViewHolder> {

    List<FamiliaModel> lista;
    Context c;

    public AdapterFamilia(List<FamiliaModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterFamilia.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.generic_info_layout, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FamiliaModel familiaModel = lista.get(position);
        holder.info.setText(familiaModel.toString());

        holder.itemView.getRootView().setOnClickListener( view -> {
            Intent i = new Intent(c, PessoaActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("idFamilia", familiaModel.getId());
            c.startActivity(i);
        });
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
