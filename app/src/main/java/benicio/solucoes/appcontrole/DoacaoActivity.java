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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import benicio.solucoes.appcontrole.adapter.AdapterDoacao;
import benicio.solucoes.appcontrole.databinding.ActivityDoacaoBinding;
import benicio.solucoes.appcontrole.databinding.DialogCadastroDoacaoLayoutBinding;
import benicio.solucoes.appcontrole.model.DoacaoModel;
import benicio.solucoes.appcontrole.model.FamiliaModel;
import benicio.solucoes.appcontrole.model.LocalidadeModel;
import benicio.solucoes.appcontrole.util.DoacaoUtil;
import benicio.solucoes.appcontrole.util.FamiliaUtil;
import benicio.solucoes.appcontrole.util.LocalidadeUtil;

public class DoacaoActivity extends AppCompatActivity {

    ActivityDoacaoBinding mainBinding;
    RecyclerView r;
    AdapterDoacao adapterDoacao;
    List<DoacaoModel> lista = new ArrayList<>();
    List<DoacaoModel> listaCompleta = new ArrayList<>();
    Dialog d;
    Bundle b;

    String nomeFamilia = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityDoacaoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cadastro de doação");

        b = getIntent().getExtras();
        nomeFamilia = b.getString("nomeFamilia", "");

        configurarDialog();
        configurarRecycler();
        mainBinding.fabAdd.setOnClickListener( view -> d.show());

    }

    private void configurarRecycler() {
        r = mainBinding.recyclerDoacao;
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);
        if ( DoacaoUtil.returnDoacao(this) != null){
            for ( DoacaoModel doacaoModel : DoacaoUtil.returnDoacao(this)){
                listaCompleta.add(doacaoModel);
                if (doacaoModel.getNomeFamilia().equals(nomeFamilia)){
                    lista.add(doacaoModel);
                }
            }
            mainBinding.textInfo.setVisibility(View.GONE);
        }
        adapterDoacao = new AdapterDoacao(lista, this);
        r.setAdapter(adapterDoacao);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void configurarDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        DialogCadastroDoacaoLayoutBinding dialogBinding = DialogCadastroDoacaoLayoutBinding.inflate(getLayoutInflater());

        AutoCompleteTextView autoCompleteTextView = dialogBinding.autoCompleteTextView;

        List<String> nomeFamilias = new ArrayList<>();
        if ( FamiliaUtil.returnFamilia(this) != null){

            for ( FamiliaModel familiaModel : FamiliaUtil.returnFamilia(this) ){
                nomeFamilias.add(familiaModel.getNome());
            }
        }
        String[] familias = nomeFamilias.toArray(new String[0]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, familias);
        autoCompleteTextView.setAdapter(adapter);

        dialogBinding.autoCompleteTextView.setOnClickListener( view -> autoCompleteTextView.showDropDown());

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSiglaEstado = (String) parent.getItemAtPosition(position);
            autoCompleteTextView.setText(selectedSiglaEstado);

            Toast.makeText(getApplicationContext(), "Família selecionada: " + selectedSiglaEstado, Toast.LENGTH_SHORT).show();
        });

        autoCompleteTextView.setText(nomeFamilia);

        Date dataAtual = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        String dataHoraFormatada = formato.format(dataAtual);

        dialogBinding.edtData.setText(dataHoraFormatada);

        dialogBinding.cadastrar.setOnClickListener( view -> {


            String id = UUID.randomUUID().toString();

            List<DoacaoModel> listaSave = new ArrayList<>();

            if (DoacaoUtil.returnDoacao(this) != null){
                listaSave.addAll(DoacaoUtil.returnDoacao(this));
            }

            String idFamilia  = "";
            String idLocalidade = "";


            if ( FamiliaUtil.returnFamilia(this) != null){
                for ( FamiliaModel familiaModel :FamiliaUtil.returnFamilia(this) ){
                    if ( familiaModel.getNome().equals(dialogBinding.autoCompleteTextView.getText().toString())){
                        idFamilia = familiaModel.getId();
                        idLocalidade = familiaModel.getIdLocalidade();
                    }
                }
            }
            int quantidade = 0;
            try{
                quantidade = Integer.parseInt(dialogBinding.edtQuantidade.getText().toString());
            }catch (Exception e){}

            listaSave.add(new DoacaoModel(
                    id,
                    idFamilia,
                    idLocalidade,
                    dialogBinding.edtData.getText().toString(),
                    dialogBinding.edtObs.getText().toString(),
                    dialogBinding.radioButton.isChecked() ? "Exame" : "Cesta básica",
                    dialogBinding.autoCompleteTextView.getText().toString(),
                    quantidade
                    ));

            DoacaoUtil.savedDoacao(listaSave, this);

            Toast.makeText(this, "Cadastro com sucesso!", Toast.LENGTH_SHORT).show();
            dialogBinding.edtObs.setText("");
            dialogBinding.autoCompleteTextView.setText("");

            d.dismiss();
            mainBinding.textInfo.setVisibility(View.GONE);

            lista.clear();
            for ( DoacaoModel doacaoModel : DoacaoUtil.returnDoacao(this)){
                if (doacaoModel.getNomeFamilia().equals(nomeFamilia)){
                    lista.add(doacaoModel);
                }
            }
            adapterDoacao.notifyDataSetChanged();

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