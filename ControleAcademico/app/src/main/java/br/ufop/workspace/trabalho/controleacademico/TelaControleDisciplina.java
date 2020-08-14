/**
 * @author Fabiana Barreto Pereira
 */

package br.ufop.workspace.trabalho.controleacademico;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Classe para criar e controlar tela de controle de disciplina (tela_controle_disciplina)
 */
public class TelaControleDisciplina extends AppCompatActivity {
    private OperacoesBD operacoesBD;
    private ArrayList<Disciplina> disciplinas=new ArrayList<>();
    private TextView tvNomeDisciplina,tvStatus,tvSemestre,tvFaltas,tvLimiteFaltas,tvMeta,tvNotaTotal;
    private Button bGraficoPrioridade,bTarefas,bGraficoTipo;
    private ImageButton bMais,bMenos;
    private int faltas=0;
    private Disciplina escolhida;
    private long idSelect;

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        idSelect=getIntent().getLongExtra("ID",0); //referente a linha selecionada
        long positionSelected=getIntent().getLongExtra("POSITION",0);
        setContentView(R.layout.tela_controle_disciplina);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        System.out.println("id "+idSelect+" p "+positionSelected);
        tvNomeDisciplina=(TextView) findViewById(R.id.tvNomeDisciplinaControle);
        tvStatus=(TextView)findViewById(R.id.tvStatusControle);
        tvSemestre=(TextView)findViewById(R.id.tvSemestreControle);
        tvFaltas=(TextView) findViewById(R.id.tvFaltasExistentesControle);
        tvLimiteFaltas=(TextView)findViewById(R.id.tvLimiteFaltasControle);
        tvMeta=(TextView)findViewById(R.id.tvMetaControle);
        tvNotaTotal=(TextView)findViewById(R.id.tvNotaTotalControle);
        bMais=(ImageButton)findViewById(R.id.btAcrescentarFalta) ;
        bMenos=(ImageButton)findViewById(R.id.btDiminuirFalta);
        bTarefas=(Button) findViewById(R.id.btTarefas);
        escolhida=new Disciplina();
        operacoesBD=new OperacoesBD(this);
        carregaPagina();
        faltas=Integer.parseInt(tvFaltas.getText().toString());
        bGraficoPrioridade=findViewById(R.id.btGraficoPrioridade);
        bGraficoTipo=findViewById(R.id.btGraficoTipo);
        addButtonListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        carregaPagina();
    }

    /**
     * Classe para retornar para activity anterior
     * @return super.onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * Classe para adicinar ação aos botões
     */
    public void addButtonListener(){
        bMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                faltas++;
                if(faltas==escolhida.getLimiteFaltas()){
                    tvFaltas.setTextColor(Color.MAGENTA);
                }else if(faltas>escolhida.getLimiteFaltas()){
                    tvFaltas.setTextColor(Color.RED);
                }else if(faltas<escolhida.getLimiteFaltas() && faltas>=(escolhida.getLimiteFaltas()-4)){//exibir mesnsagem que as faltas estão aproximando do limite
                    tvFaltas.setTextColor(Color.MAGENTA);
                    Toast.makeText(TelaControleDisciplina.this,"Alerta: Você só pode ter mais "+(escolhida.getLimiteFaltas()-faltas)+" falta(s)!",Toast.LENGTH_SHORT).show();
                }else{
                    tvFaltas.setTextColor(Color.GRAY);
                }
                operacoesBD.atualizarFaltas(faltas,escolhida.getIdDisciplina());
                tvFaltas.setText(String.valueOf(faltas));

            }
        });

        bMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(faltas>0) {

                    faltas--;
                    if(faltas==escolhida.getLimiteFaltas()){
                        tvFaltas.setTextColor(Color.MAGENTA);
                    }else if(faltas>escolhida.getLimiteFaltas()){
                        tvFaltas.setTextColor(Color.RED);
                    }else if(faltas<escolhida.getLimiteFaltas() && faltas>=(escolhida.getLimiteFaltas()-4)){//exibir mesnsagem que as faltas estão aproximando do limite
                        tvFaltas.setTextColor(Color.MAGENTA);
                        Toast.makeText(TelaControleDisciplina.this,"Alerta: Você só pode ter mais "+(escolhida.getLimiteFaltas()-faltas)+" falta(s)!",Toast.LENGTH_SHORT).show();
                    }else{
                        tvFaltas.setTextColor(Color.GRAY);
                    }
                    operacoesBD.atualizarFaltas(faltas, escolhida.getIdDisciplina());
                    tvFaltas.setText(String.valueOf(faltas));

                }
            }
        });

        bTarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(TelaControleDisciplina.this,TelaTarefas.class);
                it.putExtra("idDisciplina",escolhida.getIdDisciplina());
                it.putExtra("ligadaDisciplina",true);
                startActivity(it);
            }
        });

        bGraficoPrioridade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(TelaControleDisciplina.this, TelaGraficoPrioridadeDisciplina.class);
                it.putExtra("idDisciplina",escolhida.getIdDisciplina());
                startActivity(it);
            }
        });

        bGraficoTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(TelaControleDisciplina.this, TelaGraficoTipoDisciplina.class);
                it.putExtra("idDisciplina",escolhida.getIdDisciplina());
                startActivity(it);
            }
        });
    }


    /**
     * Classe para carregar tela com dados atualizados do banco de dados
     */
    public void carregaPagina(){
        operacoesBD=new OperacoesBD(this);
        disciplinas= (ArrayList<Disciplina>) operacoesBD.exibirDisciplinas();
        String[] semestres= {"1° semestre","2° semestre"};
       String semestre="";
        for(int i=0;i<disciplinas.size();i++){
            if(idSelect==i){
                escolhida=disciplinas.get(i);
                break;
            }
        }
        for(int i=0;i<semestres.length;i++){
            if(escolhida.getSemestre().equals(semestres[i])){
                semestre=semestres[i];
                break;
            }
        }
        System.out.println("disciplina- "+escolhida.getNome()+" semestre - "+escolhida.getSemestre()+" faltas-"+escolhida.getFaltas()+" meta "+
                escolhida.getMeta()+" andamento- "+escolhida.getAndamento());

        tvNomeDisciplina.setText(escolhida.getNome());
        tvStatus.setText(escolhida.getAndamento());
        tvSemestre.setText(String.valueOf(semestre));
        tvFaltas.setText(String.valueOf(escolhida.getFaltas()));
        tvLimiteFaltas.setText(String.valueOf(escolhida.getLimiteFaltas()));
        tvMeta.setText(String.valueOf(escolhida.getMeta()));
        double notaTotal=operacoesBD.exibirNotaTotalDisciplina(escolhida.getIdDisciplina());
        tvNotaTotal.setText(String.valueOf(notaTotal));
        if(notaTotal>=escolhida.getMeta()){
            tvNotaTotal.setTextColor(Color.BLUE);
        }else{
            tvNotaTotal.setTextColor(Color.GRAY);
        }
        if(escolhida.getFaltas()==escolhida.getLimiteFaltas()){
            tvFaltas.setTextColor(Color.MAGENTA);
        }else if(escolhida.getFaltas()>escolhida.getLimiteFaltas()){
            tvFaltas.setTextColor(Color.RED);
        }else if(escolhida.getFaltas()<escolhida.getLimiteFaltas() && escolhida.getFaltas()>=(escolhida.getLimiteFaltas()-4)){//exibir mesnsagem que as faltas estão aproximando do limite
            tvFaltas.setTextColor(Color.MAGENTA);
            Toast.makeText(this,"Alerta: Você só pode ter mais "+(escolhida.getLimiteFaltas()-escolhida.getFaltas())+" falta(s)!",Toast.LENGTH_SHORT).show();
        }else{
            tvFaltas.setTextColor(Color.GRAY);
        }
    }
}
