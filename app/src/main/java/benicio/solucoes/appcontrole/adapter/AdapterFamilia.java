package benicio.solucoes.appcontrole.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.solucoes.appcontrole.DoacaoActivity;
import benicio.solucoes.appcontrole.PessoaActivity;
import benicio.solucoes.appcontrole.R;
import benicio.solucoes.appcontrole.model.FamiliaModel;

public class AdapterFamilia extends RecyclerView.Adapter<AdapterFamilia.MyViewHolder> {

    List<FamiliaModel> lista;
    Context c;

    boolean isSelecao;

    public AdapterFamilia(List<FamiliaModel> lista, Context c,boolean isSelecao) {
        this.lista = lista;
        this.c = c;
        this.isSelecao = isSelecao;
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

        holder.itemView.getRootView().setClickable(false);

        if ( isSelecao ){
            holder.folderImage.setVisibility(View.VISIBLE);
            holder.btn_cadastro_pessoa.setText("Ver doações");
        }

        holder.btn_cadastro_pessoa.setVisibility(View.VISIBLE);
        holder.btn_cadastro_pessoa.setOnClickListener( view -> {
            if ( isSelecao ){

                Intent i = new Intent(c, DoacaoActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("idFamilia", familiaModel.getId());
                i.putExtra("nomeFamilia", familiaModel.getNome());

                c.startActivity(i);
            }else{
                Intent i = new Intent(c, PessoaActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("idFamilia", familiaModel.getId());
                c.startActivity(i);
            }

        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView info;
        ImageView folderImage;
        Button btn_cadastro_pessoa;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.text_info_generic);
            folderImage = itemView.findViewById(R.id.iconeFolder);
            btn_cadastro_pessoa = itemView.findViewById(R.id.btn_cadastro_pessoa);
        }
    }
}
