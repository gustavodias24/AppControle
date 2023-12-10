package benicio.solucoes.appcontrole.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import benicio.solucoes.appcontrole.R;
import benicio.solucoes.appcontrole.model.PessoaModel;

public class AdapterPessoa extends RecyclerView.Adapter<AdapterPessoa.MyViewHolder>{
    List<PessoaModel> lista;
    Context c;

    public AdapterPessoa(List<PessoaModel> lista, Context c) {
        this.lista = lista;
        this.c = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterPessoa.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.generic_info_layout, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PessoaModel pessoa = lista.get(position);

        holder.info.setText(pessoa.toString());

        if ( pessoa.getFotoString() != null){
            holder.profile_image_exibido.setVisibility(View.VISIBLE);
        }

        Picasso.get().load(Uri.parse(pessoa.getFotoString())).into(
                holder.profile_image_exibido
        );
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView info;
        ImageView profile_image_exibido;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.text_info_generic);
            profile_image_exibido = itemView.findViewById(R.id.profile_image_exibido);
        }
    }
}
