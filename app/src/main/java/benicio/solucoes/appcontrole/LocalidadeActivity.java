package benicio.solucoes.appcontrole;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import benicio.solucoes.appcontrole.adapter.AdapterLocalidade;
import benicio.solucoes.appcontrole.databinding.ActivityLocalidadeBinding;
import benicio.solucoes.appcontrole.databinding.DialogCadastroLocalidadeLayoutBinding;
import benicio.solucoes.appcontrole.model.LocalidadeModel;
import benicio.solucoes.appcontrole.util.LocalidadeUtil;

public class LocalidadeActivity extends AppCompatActivity {
    ActivityLocalidadeBinding mainBinding;
    RecyclerView r;
    AdapterLocalidade adapterLocalidade;
    List<LocalidadeModel> lista = new ArrayList<>();
    Dialog d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityLocalidadeBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cadastro fazendo/bairro");

        configurarDialog();
        configurarRecycler();
        mainBinding.fabAdd.setOnClickListener( view -> d.show());
    }

    private void configurarRecycler() {
        r = mainBinding.recyclerLocalidade;
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        if ( LocalidadeUtil.returnLocalidade(this) != null){
            lista.addAll(LocalidadeUtil.returnLocalidade(this));
            mainBinding.textInfo.setVisibility(View.GONE);
        }
        adapterLocalidade = new AdapterLocalidade(lista, this);
        r.setAdapter(adapterLocalidade);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void configurarDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        DialogCadastroLocalidadeLayoutBinding dialogBinding = DialogCadastroLocalidadeLayoutBinding.inflate(getLayoutInflater());

        dialogBinding.cadastrar.setOnClickListener( view -> {
            String id = UUID.randomUUID().toString();

            List<LocalidadeModel> listaSave = new ArrayList<>();

            if (LocalidadeUtil.returnLocalidade(this) != null){
                listaSave.addAll(LocalidadeUtil.returnLocalidade(this));
            }

            listaSave.add(new LocalidadeModel(id,
                    dialogBinding.fazenda.isChecked() ? "Fazenda" : "Bairro",
                    dialogBinding.edtNome.getText().toString()));

            LocalidadeUtil.savedLocalidade(listaSave, this);

            Toast.makeText(this, "Cadastro com sucesso!", Toast.LENGTH_SHORT).show();
            dialogBinding.edtNome.setText("");
            d.dismiss();
            mainBinding.textInfo.setVisibility(View.GONE);

            lista.clear();
            lista.addAll(listaSave);
            adapterLocalidade.notifyDataSetChanged();

        });


        b.setView(dialogBinding.getRoot());
        d = b.create();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}