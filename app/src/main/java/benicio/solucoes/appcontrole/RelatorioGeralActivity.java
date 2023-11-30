package benicio.solucoes.appcontrole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.slider.LabelFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import benicio.solucoes.appcontrole.databinding.ActivityRelatorioGeralBinding;
import benicio.solucoes.appcontrole.model.DoacaoModel;
import benicio.solucoes.appcontrole.model.FamiliaModel;
import benicio.solucoes.appcontrole.util.DoacaoUtil;
import benicio.solucoes.appcontrole.util.FamiliaUtil;

public class RelatorioGeralActivity extends AppCompatActivity {

    ActivityRelatorioGeralBinding mainBinding;

    List<DoacaoModel> listaDoacoes = new ArrayList<>();
    int qtdDoacoes = 0;
    int qtdExames = 0;
    int qtdCestasBasicas = 0;
    List<FamiliaModel> listaFamilias = new ArrayList<>();
    int qtdFamilias = 0;
    StringBuilder contagemLocalidadeString = new StringBuilder();




    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRelatorioGeralBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Relatório geral");


        if ( DoacaoUtil.returnDoacao(this) != null){
            listaDoacoes.addAll(DoacaoUtil.returnDoacao(this));
        }

        if ( FamiliaUtil.returnFamilia(this) != null){
            listaFamilias.addAll(FamiliaUtil.returnFamilia(this));
        }

        pegarDadosDoacao();
        pegarDadosFamilia();

        mainBinding.infosGerais.setText(
                String.format("Quant. Famílias: %d\nFamília por localidade:\n %s\nQuant. Doações: %d\nQuant. Exames: %d\nQuant. Cestas básicas: %d",qtdFamilias, contagemLocalidadeString.toString() , qtdDoacoes, qtdExames, qtdCestasBasicas)
        );

    }

    private void pegarDadosFamilia(){

        qtdFamilias = listaFamilias.size();

        Map<String, Integer> contagemLocalidade = new HashMap<>();

        for ( FamiliaModel familia : listaFamilias){
            contagemLocalidade.put(familia.getNomeLocalidade(), contagemLocalidade.getOrDefault(familia.getNomeLocalidade(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : contagemLocalidade.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            contagemLocalidadeString.append(nome + ": " + quantidade).append("\n");
        }

        PieChart pieChart = mainBinding.pieChart;

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : contagemLocalidade.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            entries.add(new PieEntry(quantidade, nome));
            colors.add(getCorAleatoria());
        }
        PieDataSet dataSet = new PieDataSet(entries, "Quantidade de Localidades");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);

        Description description = new Description();
        description.setText("Contagem de Localidades");
        pieChart.setDescription(description);

        pieChart.setEntryLabelColor(Color.BLACK); // Cor do texto dentro das fatias
        pieChart.setData(pieData);

        // Atualizar o gráfico
        pieChart.invalidate();

    }

    private int getCorAleatoria() {
        return Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }
    private void pegarDadosDoacao(){

        qtdDoacoes = listaDoacoes.size();

        for ( DoacaoModel doacao : listaDoacoes){
            if( doacao.getTipo().equals("Exame")){
                qtdExames += 1;
            }else{
                qtdCestasBasicas += 1;
            }
        }

        PieChart pieChart = mainBinding.pieChartDoacao;

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        entries.add(new PieEntry(qtdExames, "Exames"));
        entries.add(new PieEntry(qtdCestasBasicas, "Cestas Básicas"));

        colors.add(Color.BLUE); // Cor para Exames
        colors.add(Color.GREEN); // Cor para Cestas Básicas

        PieDataSet dataSet = new PieDataSet(entries, "Tipos de Doações");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);

        Description description = new Description();
        description.setText("Distribuição de Tipos de Doações");
        pieChart.setDescription(description);

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setData(pieData);

        // Atualizar o gráfico
        pieChart.invalidate();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}