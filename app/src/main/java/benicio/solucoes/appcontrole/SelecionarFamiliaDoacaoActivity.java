package benicio.solucoes.appcontrole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import benicio.solucoes.appcontrole.adapter.AdapterFamilia;
import benicio.solucoes.appcontrole.databinding.ActivityFamiliaBinding;
import benicio.solucoes.appcontrole.databinding.ActivitySelecionarFamiliaDoacaoBinding;
import benicio.solucoes.appcontrole.model.FamiliaModel;
import benicio.solucoes.appcontrole.util.FamiliaUtil;

public class SelecionarFamiliaDoacaoActivity extends AppCompatActivity {

    ActivitySelecionarFamiliaDoacaoBinding mainBinding;
    RecyclerView r;
    AdapterFamilia adapter;
    List<FamiliaModel> lista = new ArrayList<>();
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivitySelecionarFamiliaDoacaoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Selecionar família");

        configurarRecycler();
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
        adapter = new AdapterFamilia(lista, this, true);
        r.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}