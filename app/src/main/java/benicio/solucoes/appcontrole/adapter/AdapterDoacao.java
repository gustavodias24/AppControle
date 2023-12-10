package benicio.solucoes.appcontrole.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.solucoes.appcontrole.R;
import benicio.solucoes.appcontrole.databinding.AttStatusLayoutBinding;
import benicio.solucoes.appcontrole.model.DoacaoModel;
import benicio.solucoes.appcontrole.util.DoacaoUtil;

public class AdapterDoacao extends RecyclerView.Adapter<AdapterDoacao.MyViewHolder>{
    List<DoacaoModel> lista;
    Activity c;
    Dialog d;


    public AdapterDoacao(List<DoacaoModel> lista, Activity c) {
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

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            DoacaoModel doacaoModel = lista.get(position);

            holder.info.setText(doacaoModel.toString());
            holder.status.setVisibility(View.VISIBLE);
            holder.itemView.getRootView().setOnClickListener(view -> {
                AlertDialog.Builder b = new AlertDialog.Builder(c);
                AttStatusLayoutBinding dialogBinding = AttStatusLayoutBinding.inflate(c.getLayoutInflater());

                switch (doacaoModel.getStatus()){
                    case 0:
                        dialogBinding.radioButton3.setChecked(true);
                        break;
                    case 1:
                        dialogBinding.radioButton4.setChecked(true);
                        break;
                    case 2:
                        dialogBinding.radioButton5.setChecked(true);
                        break;
                }

                dialogBinding.button.setOnClickListener( view2 -> {
                    Toast.makeText(c, "Status atualizado", Toast.LENGTH_SHORT).show();
                    if ( dialogBinding.radioButton3.isChecked()){
                        doacaoModel.setStatus(0);
                    }else if (dialogBinding.radioButton4.isChecked()){
                        doacaoModel.setStatus(1);
                    }else{
                        doacaoModel.setStatus(2);
                    }
                    DoacaoUtil.savedDoacao(lista, c);
                    this.notifyDataSetChanged();
                    d.dismiss();
                });

                b.setView(dialogBinding.getRoot());
                d = b.create();
                d.show();
            });
            switch (doacaoModel.getStatus()){
                case 0:
                    holder.status.setTextColor(Color.YELLOW);
                    holder.status.setText("Em andamento.");
                    break;
                case 1:
                    holder.status.setTextColor(Color.RED);
                    holder.status.setText("Cancelado.");
                    break;
                case 2:
                    holder.status.setTextColor(Color.GREEN);
                    holder.status.setText("Concluído.");
                    break;
            }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {

        TextView info, status;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.text_info_generic);
            status = itemView.findViewById(R.id.text_info_generic_status);
        }
    }
}
