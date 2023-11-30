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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import benicio.solucoes.appcontrole.adapter.AdapterFamilia;
import benicio.solucoes.appcontrole.adapter.AdapterLocalidade;
import benicio.solucoes.appcontrole.databinding.ActivityFamiliaBinding;
import benicio.solucoes.appcontrole.databinding.ActivityLocalidadeBinding;
import benicio.solucoes.appcontrole.databinding.DialogCadastroFamiliaLayoutBinding;
import benicio.solucoes.appcontrole.databinding.DialogCadastroLocalidadeLayoutBinding;
import benicio.solucoes.appcontrole.model.FamiliaModel;
import benicio.solucoes.appcontrole.model.LocalidadeModel;
import benicio.solucoes.appcontrole.util.FamiliaUtil;
import benicio.solucoes.appcontrole.util.LocalidadeUtil;

public class FamiliaActivity extends AppCompatActivity {

    ActivityFamiliaBinding mainBinding;
    RecyclerView r;
    AdapterFamilia adapter;
    List<FamiliaModel> lista = new ArrayList<>();
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityFamiliaBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cadastro família");

        configurarDialog();
        configurarRecycler();
        mainBinding.fabAdd.setOnClickListener( view -> d.show());
    }

    private void configurarRecycler() {
        r = mainBinding.recyclerFamilia;
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        if ( FamiliaUtil.returnFamilia(this) != null){
            lista.addAll(FamiliaUtil.returnFamilia(this));
            mainBinding.textInfo.setVisibility(View.GONE);
        }
        adapter = new AdapterFamilia(lista, this);
        r.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void configurarDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        DialogCadastroFamiliaLayoutBinding dialogBinding = DialogCadastroFamiliaLayoutBinding.inflate(getLayoutInflater());

        AutoCompleteTextView autoCompleteTextView = dialogBinding.autoCompleteTextView;

        List<String> nomeLocalidades = new ArrayList<>();
        if ( LocalidadeUtil.returnLocalidade(this) != null){

            for ( LocalidadeModel localidadeModel : LocalidadeUtil.returnLocalidade(this) ){
                nomeLocalidades.add(localidadeModel.getNome());
            }
        }
        String[] localidades = nomeLocalidades.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, localidades);
        autoCompleteTextView.setAdapter(adapter);

        dialogBinding.autoCompleteTextView.setOnClickListener( view -> autoCompleteTextView.showDropDown());

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSiglaEstado = (String) parent.getItemAtPosition(position);
            autoCompleteTextView.setText(selectedSiglaEstado);

            Toast.makeText(getApplicationContext(), "Localidade selecionado: " + selectedSiglaEstado, Toast.LENGTH_SHORT).show();
        });

        dialogBinding.cadastrar.setOnClickListener( view -> {
            String id = UUID.randomUUID().toString();

            List<FamiliaModel> listaSave = new ArrayList<>();

            if (FamiliaUtil.returnFamilia(this) != null){
                listaSave.addAll(FamiliaUtil.returnFamilia(this));
            }
            String idLocalidade  = "";

            if ( LocalidadeUtil.returnLocalidade(this) != null){
                for ( LocalidadeModel localidadeModel : LocalidadeUtil.returnLocalidade(this) ){
                    if ( localidadeModel.getNome().equals(dialogBinding.autoCompleteTextView.getText().toString())){
                        idLocalidade = localidadeModel.getId();
                    }
                }
            }

            listaSave.add(new FamiliaModel(
                    id,
                    idLocalidade,
                    dialogBinding.edtNome.getText().toString(),
                    dialogBinding.autoCompleteTextView.getText().toString()
            ));

            FamiliaUtil.savedFamilia(listaSave, this);

            Toast.makeText(this, "Cadastro com sucesso!", Toast.LENGTH_SHORT).show();
            dialogBinding.edtNome.setText("");
            dialogBinding.autoCompleteTextView.setText("");
            d.dismiss();
            mainBinding.textInfo.setVisibility(View.GONE);

            lista.clear();
            lista.addAll(listaSave);
            adapter.notifyDataSetChanged();

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