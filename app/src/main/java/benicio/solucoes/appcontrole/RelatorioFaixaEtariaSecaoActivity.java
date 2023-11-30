package benicio.solucoes.appcontrole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import benicio.solucoes.appcontrole.databinding.ActivityRelatorioFaixaEtariaSecaoBinding;
import benicio.solucoes.appcontrole.databinding.ActivityRelatorioGeralBinding;
import benicio.solucoes.appcontrole.model.FamiliaModel;
import benicio.solucoes.appcontrole.model.PessoaModel;
import benicio.solucoes.appcontrole.util.PessoaUtil;

public class RelatorioFaixaEtariaSecaoActivity extends AppCompatActivity {

    ActivityRelatorioFaixaEtariaSecaoBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRelatorioFaixaEtariaSecaoBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Relatório");

        configurarGraficoFaixaEtaria();
        configurarGraficoSecao();
    }

    private void configurarGraficoFaixaEtaria(){
        List<PessoaModel> listaPessoas = new ArrayList<>();

        if (PessoaUtil.returnPessoa(this) != null){
            listaPessoas.addAll(PessoaUtil.returnPessoa(this));
        }


        Map<String, Integer> contagemIdade = new HashMap<>();

        for ( PessoaModel pessoa : listaPessoas){
            contagemIdade.put(pessoa.getIdade(), contagemIdade.getOrDefault(pessoa.getIdade(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : contagemIdade.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
//            con.append(nome + ": " + quantidade).append("\n");
        }

        PieChart pieChart = mainBinding.pieChartFaixaEtaria;

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : contagemIdade.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            entries.add(new PieEntry(quantidade, nome));
            colors.add(getCorAleatoria());
        }
        PieDataSet dataSet = new PieDataSet(entries, "Quantidade de Idades Distintas");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);

        Description description = new Description();
        description.setText("Contagem de Quantidade de Idades");
        pieChart.setDescription(description);

        pieChart.setEntryLabelColor(Color.BLACK); // Cor do texto dentro das fatias
        pieChart.setData(pieData);

        // Atualizar o gráfico
        pieChart.invalidate();
    }

    private void configurarGraficoSecao(){
        List<PessoaModel> listaPessoas = new ArrayList<>();

        if (PessoaUtil.returnPessoa(this) != null){
            listaPessoas.addAll(PessoaUtil.returnPessoa(this));
        }


        Map<String, Integer> contagemIdade = new HashMap<>();

        for ( PessoaModel pessoa : listaPessoas){
            contagemIdade.put(pessoa.getSecao(), contagemIdade.getOrDefault(pessoa.getSecao(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : contagemIdade.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
//            con.append(nome + ": " + quantidade).append("\n");
        }

        PieChart pieChart = mainBinding.pieChartSecao;

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : contagemIdade.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            entries.add(new PieEntry(quantidade, nome));
            colors.add(getCorAleatoria());
        }
        PieDataSet dataSet = new PieDataSet(entries, "Quantidade de Seções Distintas");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);

        Description description = new Description();
        description.setText("Contagem de Quantidade de Seções");
        pieChart.setDescription(description);

        pieChart.setEntryLabelColor(Color.BLACK); // Cor do texto dentro das fatias
        pieChart.setData(pieData);

        // Atualizar o gráfico
        pieChart.invalidate();
    }

    private int getCorAleatoria() {
        return Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}