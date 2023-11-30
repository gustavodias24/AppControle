package benicio.solucoes.appcontrole;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import benicio.solucoes.appcontrole.databinding.ActivityRelatorioFazendaBairroBinding;
import benicio.solucoes.appcontrole.databinding.ActivityRelatorioGeralBinding;
import benicio.solucoes.appcontrole.model.DoacaoModel;
import benicio.solucoes.appcontrole.model.FamiliaModel;
import benicio.solucoes.appcontrole.model.LocalidadeModel;
import benicio.solucoes.appcontrole.util.DoacaoUtil;
import benicio.solucoes.appcontrole.util.FamiliaUtil;
import benicio.solucoes.appcontrole.util.LocalidadeUtil;

public class RelatorioFazendaBairroActivity extends AppCompatActivity {

    ActivityRelatorioFazendaBairroBinding mainBinding;
    List<String> listaLocalidade = new ArrayList<>();

    String idLocalidade = "";

    StringBuilder relatorioGeralFamiliaDoacao = new StringBuilder();
    StringBuilder contagemBuilder = new StringBuilder();
    int qtdFamilias = 0;
    int qtdDoacoes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityRelatorioFazendaBairroBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Relatório fazenda/bairro");

        Spinner spinnerEstados = mainBinding.spinnerEstados;

        listaLocalidade.add("Selecione uma opção");

        if (LocalidadeUtil.returnLocalidade(this) != null){
            for (LocalidadeModel localidadeModel : LocalidadeUtil.returnLocalidade(this)){
                listaLocalidade.add(localidadeModel.getNome());
            }
        }

        // Crie um ArrayAdapter usando o ArrayList de estados
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaLocalidade
        );

        // Especifique o layout para usar quando a lista de opções aparecer
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar o adapter ao spinner
        spinnerEstados.setAdapter(adapter);

        // Adicionar um listener ao spinner para lidar com a seleção
        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if( position != 0){
                    String familiaSelecionada = listaLocalidade.get(position);
                    fazerRelatorioDoacoesFamilia(familiaSelecionada);
                    Toast.makeText(RelatorioFazendaBairroActivity.this, "Opção selecionada: " + familiaSelecionada, Toast.LENGTH_SHORT).show();

                    mainBinding.textInfos.setText("Visão geral:\n" + relatorioGeralFamiliaDoacao.toString() + "\n" + contagemBuilder);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Método obrigatório, mas não necessário para este exemplo
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fazerRelatorioDoacoesFamilia(String localidadeNome){

        relatorioGeralFamiliaDoacao.setLength(0);
        qtdFamilias = 0;
        qtdDoacoes = 0;

        if ( LocalidadeUtil.returnLocalidade(this) != null){
            for (LocalidadeModel localidadeModel : LocalidadeUtil.returnLocalidade(this)){
                if ( localidadeModel.getNome().equals(localidadeNome) ){
                    idLocalidade = localidadeModel.getId();
                    break;
                }
            }
        }


        if (FamiliaUtil.returnFamilia(this) != null){
            for(FamiliaModel familiaModel: FamiliaUtil.returnFamilia(this)){
                if ( familiaModel.getIdLocalidade().equals(idLocalidade)){
                    qtdFamilias += 1;
                }
            }
        }

        if (DoacaoUtil.returnDoacao(this) != null){
            fazerRelatorioDoacoes(DoacaoUtil.returnDoacao(this));
            for (DoacaoModel doacaoModel: DoacaoUtil.returnDoacao(this)){
                if ( doacaoModel.getIdLocalidade().equals(idLocalidade)){
                    qtdDoacoes += 1;
                }
            }
        }

        PieChart pieChart = mainBinding.pieChartFamiliaDoacoes;

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        entries.add(new PieEntry(qtdDoacoes, "Doações"));
        entries.add(new PieEntry(qtdFamilias, "Famílias"));

        colors.add(Color.BLUE); // Cor para Exames
        colors.add(Color.GREEN); // Cor para Cestas Básicas

        PieDataSet dataSet = new PieDataSet(entries, "Relação família/doação");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);

        Description description = new Description();
        description.setText("Distribuição de doação");
        pieChart.setDescription(description);

        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setData(pieData);

        // Atualizar o gráfico
        pieChart.invalidate();

        relatorioGeralFamiliaDoacao.append("Famílias: ").append(qtdFamilias).append("\n")
                .append("Doações: ").append(qtdDoacoes);
    }

    private void fazerRelatorioDoacoes(List<DoacaoModel> listaDoacao){

        Map<String, Integer> contagemDoacoes = new HashMap<>();

        for ( DoacaoModel doacao : listaDoacao){
            if ( doacao.getIdLocalidade().equals(idLocalidade)){
                contagemDoacoes.put(doacao.getNomeFamilia(), contagemDoacoes.getOrDefault(doacao.getNomeFamilia(), 0) + 1);
            }
        }

        contagemBuilder = new StringBuilder();
        contagemBuilder.append("\n").append("Lista de famílias:\n");
        for (Map.Entry<String, Integer> entry : contagemDoacoes.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            contagemBuilder.append(nome + ": " + quantidade).append("\n");
        }

        PieChart pieChart = mainBinding.pieChartDoacoes;

        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : contagemDoacoes.entrySet()) {
            String nome = entry.getKey();
            int quantidade = entry.getValue();
            entries.add(new PieEntry(quantidade, nome));
            colors.add(getCorAleatoria());
        }
        PieDataSet dataSet = new PieDataSet(entries, "Quantidade de famílias");
        dataSet.setColors(colors);
        dataSet.setValueTextColor(Color.WHITE);

        PieData pieData = new PieData(dataSet);

        Description description = new Description();
        description.setText("Contagem de Doações");
        pieChart.setDescription(description);

        pieChart.setEntryLabelColor(Color.BLACK); // Cor do texto dentro das fatias
        pieChart.setData(pieData);

        // Atualizar o gráfico
        pieChart.invalidate();
    }

    private int getCorAleatoria() {
        return Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }
}