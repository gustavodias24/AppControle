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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import benicio.solucoes.appcontrole.adapter.AdapterLocalidade;
import benicio.solucoes.appcontrole.adapter.AdapterPessoa;
import benicio.solucoes.appcontrole.databinding.ActivityLocalidadeBinding;
import benicio.solucoes.appcontrole.databinding.ActivityPessoaBinding;
import benicio.solucoes.appcontrole.databinding.DialogCadastroLocalidadeLayoutBinding;
import benicio.solucoes.appcontrole.databinding.DialogCadastroPessoaLayoutBinding;
import benicio.solucoes.appcontrole.model.LocalidadeModel;
import benicio.solucoes.appcontrole.model.PessoaModel;
import benicio.solucoes.appcontrole.util.LocalidadeUtil;
import benicio.solucoes.appcontrole.util.PessoaUtil;

public class PessoaActivity extends AppCompatActivity {

    ActivityPessoaBinding mainBinding;
    RecyclerView r;
    AdapterPessoa adapter;
    List<PessoaModel> lista = new ArrayList<>();

    Bundle b;
    String idFamilia;
    Dialog d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityPessoaBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Cadastro pessoa");

        b = getIntent().getExtras();
        idFamilia = b.getString("idFamilia", "");

        Log.d("familiaID", "onCreate: " + idFamilia);

        configurarDialog();
        configurarRecycler();
        mainBinding.fabAdd.setOnClickListener( view -> d.show());
    }

    private void configurarRecycler() {
        r = mainBinding.recyclerPessoa;
        r.setLayoutManager(new LinearLayoutManager(this));
        r.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        r.setHasFixedSize(true);

        listarPessoaDeFamilia();

        adapter = new AdapterPessoa(lista, this);
        r.setAdapter(adapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void configurarDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        DialogCadastroPessoaLayoutBinding dialogBinding = DialogCadastroPessoaLayoutBinding.inflate(getLayoutInflater());

        dialogBinding.cadastrar.setOnClickListener( view -> {
            String id = UUID.randomUUID().toString();

            List<PessoaModel> listaSave = new ArrayList<>();

            if (PessoaUtil.returnPessoa(this) != null){
                listaSave.addAll(PessoaUtil.returnPessoa(this));
            }

            listaSave.add(
                    new PessoaModel(
                            id,
                            idFamilia,
                            dialogBinding.edtNome.getText().toString(),
                            dialogBinding.edtNasc.getText().toString(),
                            dialogBinding.edtUnif.getText().toString(),
                            dialogBinding.edtMuni.getText().toString(),
                            dialogBinding.edtZona.getText().toString(),
                            dialogBinding.edtSecao.getText().toString(),
                            dialogBinding.edtNumero.getText().toString(),
                            dialogBinding.edtDataEmissao.getText().toString(),
                            dialogBinding.edtIdade.getText().toString()
                            )
            );

            PessoaUtil.savedPessoa(listaSave, this);

            Toast.makeText(this, "Cadastro com sucesso!", Toast.LENGTH_SHORT).show();
            dialogBinding.edtNome.setText("");
            d.dismiss();
            mainBinding.textInfo.setVisibility(View.GONE);

            listarPessoaDeFamilia();
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

    private void listarPessoaDeFamilia(){
        lista.clear();
        if ( PessoaUtil.returnPessoa(this) != null){
            for ( PessoaModel pessoaModel : PessoaUtil.returnPessoa(this)){
                if( pessoaModel.getIdFamilia().equals(idFamilia)){
                    lista.add(pessoaModel);
                    mainBinding.textInfo.setVisibility(View.GONE);
                }
            }
        }
    }
}