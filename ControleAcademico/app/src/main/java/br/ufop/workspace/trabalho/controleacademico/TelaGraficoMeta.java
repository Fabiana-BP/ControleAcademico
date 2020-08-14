/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Classe para criar e controlar a tela com gráfico de Notas x metas (tela_grafico_meta)
 */
public class TelaGraficoMeta extends DemoBase implements OnSeekBarChangeListener,
        OnChartValueSelectedListener {

    private BarChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    private BottomNavigationView bottomNavigationView;
    private OperacoesBD operacoesBD;
    private Map<String,Double> NotasDisciplinas =new HashMap();
    private HashMap<String,Double> metasDisciplinas =new HashMap();
    private LinkedList<String> nomesDisciplinas=new LinkedList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tela_grafico_meta);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        operacoesBD=new OperacoesBD(this);
        NotasDisciplinas=operacoesBD.exibirNotaTotalPorDisciplina();
        metasDisciplinas=operacoesBD.exibirMetaTotalPorDisciplina();
        setTitle("Meta x Notas");

        tvX = findViewById(R.id.tvXMax);
        tvX.setTextSize(10);
        tvY = findViewById(R.id.tvYMax);

        seekBarX = findViewById(R.id.seekBar1);
        seekBarX.setMax(metasDisciplinas.size()-1);
        seekBarX.setOnSeekBarChangeListener(this);

        seekBarY = findViewById(R.id.seekBar2);
        seekBarY.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        mv.setChartView(chart); // For bounds control
        chart.setMarker(mv); // Set the marker to the chart

        seekBarX.setProgress(1);
        seekBarY.setProgress(1);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(tfLight);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = chart.getXAxis();

        xAxis.setTypeface(tfLight);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int i = 1;
                for (Map.Entry<String, Double> v : NotasDisciplinas.entrySet()) {
                    if (value == i) {
                        return nomesDisciplinas.get(i-1);
                    }
                    i++;
                }

                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f);// this replaces setStartAtZero(true)

        chart.getAxisRight().setEnabled(false);



    }


    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void onBackPressed() {
        super.onBackPressed(); finish();
    }


    /**
     * Método para gerar gráfico de barras
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //dados
        metasDisciplinas =operacoesBD.exibirMetaTotalPorDisciplina();
        NotasDisciplinas =operacoesBD.exibirNotaTotalPorDisciplina();
        for(Map.Entry<String,Double> v:metasDisciplinas.entrySet()){
            nomesDisciplinas.add(v.getKey());
        }
        System.out.println("metasDisciplinas "+ metasDisciplinas);
        System.out.println("NotasDisciplinas"+ NotasDisciplinas);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
// (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"

        int groupCount = seekBarX.getProgress() + 1;
        int startDisciplina = 1; //tipos
        int endTipo ;
        endTipo= startDisciplina + groupCount;


        tvX.setText("Disciplinas");//tipos
        tvY.setText(String.valueOf(seekBarY.getProgress()));

        ArrayList<BarEntry> totalMetas = new ArrayList<>();//Total Metas
        ArrayList<BarEntry> totalNotas = new ArrayList<>(); //Total Notas


        for (int i = startDisciplina; i < endTipo; i++) { //i=tipo
           double nota=0;
           double meta = 0;
            System.out.println("nomesDisciplinas - "+nomesDisciplinas);
            String disciplina="";
            if(nomesDisciplinas.size()>=i) {
                disciplina = nomesDisciplinas.get(i - 1);

                System.out.println("disciplina - " + disciplina);
                if (NotasDisciplinas.containsKey(disciplina)) {
                    nota = NotasDisciplinas.get(disciplina);
                }
                if (metasDisciplinas.containsKey(disciplina)) {
                    meta = metasDisciplinas.get(disciplina);
                }
                System.out.println("meta- " + meta + " nota- " + nota);
                totalMetas.add(new BarEntry(i, (float) (meta)));
                totalNotas.add(new BarEntry(i, (float) (nota)));
            }
        }

        BarDataSet set1, set2;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set1.setValues(totalMetas);
            set2.setValues(totalNotas);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();

        } else {
            // create 4 DataSets
            set1 = new BarDataSet(totalMetas, "Total Prioridade");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(totalNotas, "Total Notas");
            set2.setColor(Color.rgb(164, 228, 251));


            BarData data = new BarData(set1,set2);
            data.setValueFormatter(new LargeValueFormatter());
            data.setValueTypeface(tfLight);


            chart.setData(data);
        }

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(startDisciplina);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(startDisciplina + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);

       // chart.groupBars(startTipo, 1, barSpace);

        chart.groupBars(startDisciplina, groupSpace, barSpace); // perform the "explicit" grouping
        chart.invalidate(); // refresh


    }

    /**
     * Método para gerar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionToggleValues: {
                for (IBarDataSet set : chart.getData().getDataSets())
                    set.setDrawValues(!set.isDrawValuesEnabled());

                chart.invalidate();
                break;
            }


            case R.id.actionToggleBarBorders: {
                for (IBarDataSet set : chart.getData().getDataSets())
                    ((BarDataSet) set).setBarBorderWidth(set.getBarBorderWidth() == 1.f ? 0.f : 1.f);

                chart.invalidate();
                break;
            }

            case R.id.actionSave: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    saveToGallery();
                } else {
                    requestStoragePermission(chart);
                }
                break;
            }

            case R.id.animateXY: {
                chart.animateXY(2000, 2000);
                break;
            }
        }
        return true;
    }

    /**
     * Método para salvar gráfico na galeria do smatphone
     */
    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "TelaGraficoMeta");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }
}
