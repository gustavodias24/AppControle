package benicio.solucoes.appcontrole;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import benicio.solucoes.appcontrole.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        mainBinding.catFazendaBairro.setOnClickListener(view -> startActivity(new Intent(this, LocalidadeActivity.class)));
        mainBinding.cadFamilia.setOnClickListener(view -> startActivity(new Intent(this, FamiliaActivity.class)));
        mainBinding.cadDoacao.setOnClickListener(view -> startActivity(new Intent(this, SelecionarFamiliaDoacaoActivity.class)));
        mainBinding.relGel.setOnClickListener(view -> startActivity(new Intent(this, RelatorioGeralActivity.class)));
        mainBinding.relFazendoBairro.setOnClickListener(view -> startActivity(new Intent(this, RelatorioFazendaBairroActivity.class)));
        mainBinding.relEtariaSecao.setOnClickListener(view -> startActivity(new Intent(this, RelatorioFaixaEtariaSecaoActivity.class)));
    }
}