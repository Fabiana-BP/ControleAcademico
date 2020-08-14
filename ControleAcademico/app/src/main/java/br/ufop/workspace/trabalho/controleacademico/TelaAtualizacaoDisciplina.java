/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;


;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe associada a activity tela_atualizacao_disciplina
 */
public class TelaAtualizacaoDisciplina extends AppCompatActivity {
    private Spinner spSemestres;
    private EditText etMeta,etLimiteFaltas;
    private String[] semestres={"1° semestre","2° semestre"};
    private Button btAtualizarDisciplina;
    private OperacoesBD operacoesBD;
    private String nomeDisciplina;
    private double metaDisciplina;
    private String semestreDisciplina;
    private int limiteFaltasDisciplina;
    private String statusDisciplina="";
    private long idDisciplina;
    private Spinner spStatus;
    private String[] nomesDisciplinas={"CEA036","CEA423","CSI030","CSI145","CSI427","CSI491","CSI032"
            ,"CSI443","CSI460","CSI488","ENP144","CEA307","CSI424","CSI429","CSI466","ENP473","CSI437",
            "CSI440","CSI485","ENP150","ENP153","CSI426","CSI442","CSI457","CSI476","CSI486","CSI419","CSI433",
            "CSI450","CSI477","CSI735","CSI439","CSI498","ENP126","ENP493","CSI462","CSI463","CSI499","CSI401",
            "CSI001","CSI501","CSI508","CSI473"}; //código das disciplinas do DCSI
    private AutoCompleteTextView etNomeDisciplina;

    /**
     * Método para criar a activity tela_atualizacao_disciplina
     * @param bundle
     */
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.tela_atualizacao_disciplina);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btAtualizarDisciplina=(Button)findViewById(R.id.btAtualizarDisciplina);
        operacoesBD=new OperacoesBD(this);
        nomeDisciplina=getIntent().getStringExtra("nomeDisciplina");
        metaDisciplina=getIntent().getDoubleExtra("metaDisciplina",0);
        semestreDisciplina=getIntent().getStringExtra("semestreDisciplina");
        limiteFaltasDisciplina=getIntent().getIntExtra("limiteFaltasDisciplina",0);
        idDisciplina=getIntent().getLongExtra("id",-1);
        statusDisciplina=getIntent().getStringExtra("statusDisciplina");

        //referente a linha selecionada
        etNomeDisciplina=(AutoCompleteTextView) findViewById(R.id.edNomeDisciplinaAtualizar);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,nomesDisciplinas);
        etNomeDisciplina.setAdapter(adapter);

        etMeta=(EditText) findViewById(R.id.edMetaAtualizar);
        etLimiteFaltas=(EditText)findViewById(R.id.edLimiteFaltasAtualizar);
        etNomeDisciplina.setText(nomeDisciplina);
        etMeta.setText(String.valueOf(metaDisciplina));
        etLimiteFaltas.setText(String.valueOf(limiteFaltasDisciplina));
        //spinner para semestres
        spSemestres=findViewById(R.id.spListaSemestresAtualizar);
        System.out.println("semestre "+semestreDisciplina);
        int posicao=-1;
        for(int i=0;i<semestres.length;i++){
            if(semestres[i].equals(semestreDisciplina)){
                posicao=i;
                System.out.println("entrou if");
            }
        }
        ArrayAdapter arrayAdapterSemestre=new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,semestres);
        spSemestres.setAdapter(arrayAdapterSemestre);

        spSemestres.setSelection(posicao);
        //end spinner
        //spinner para status
        spStatus=findViewById(R.id.spStatusAtualizar);
        String[]status={"Em andamento", "Finalizada"};
        ArrayAdapter adapterStatus=new ArrayAdapter(this,android.R.layout.simple_spinner_item,status);
        spStatus.setAdapter(adapterStatus);
        posicao=-1;
        for(int i=0;i<status.length;i++){
            if(status[i].equals(statusDisciplina)){
                posicao=i;
                System.out.println("entrou if");
            }
        }
        spStatus.setSelection(posicao);
        addButtonListeners();

        }


    /**
     * Método para retornar para activity anterior
      * @return super.onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * Método para adicionar ação aos botões
     */
    public void addButtonListeners(){

         btAtualizarDisciplina.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                 System.out.println("semestre - "+spSemestres.getSelectedItemPosition());
                 String semestre = "";

                     semestre=String.valueOf(spSemestres.getSelectedItem());


                 double meta=0;
                 int cont=0;
                 try {
                     meta = Double.parseDouble(etMeta.getText().toString());
                     if(meta>100 || meta<0){
                         etMeta.setText("");
                         cont=1;
                     }
                 }catch(NumberFormatException e){
                     etMeta.setText("");
                     cont=1;
                 }
                 String status=String.valueOf(spStatus.getSelectedItem());
                 //atualizar dados no banco
                 if(idDisciplina>-1) {
                     if(cont==0 && !etLimiteFaltas.getText().toString().equals("")) {
                         if (operacoesBD.atualizarDisciplina(etNomeDisciplina.getText().toString(), semestre, Integer.parseInt(etLimiteFaltas.getText().toString()),
                                 meta,status, idDisciplina) != -1) {
                             System.out.println("Tudo certo");
                             Toast.makeText(TelaAtualizacaoDisciplina.this, "Disciplina atualizada com sucesso", Toast.LENGTH_SHORT).show();
                             finish();
                         } else {
                             Toast.makeText(TelaAtualizacaoDisciplina.this, "Ocorreu um erro, tente novamente", Toast.LENGTH_SHORT).show();
                             finish();
                         }
                     }else{
                         Toast.makeText(TelaAtualizacaoDisciplina.this, "Por favor, insira valores válidos", Toast.LENGTH_SHORT).show();
                     }
                 }

             }
         });
    }

}
