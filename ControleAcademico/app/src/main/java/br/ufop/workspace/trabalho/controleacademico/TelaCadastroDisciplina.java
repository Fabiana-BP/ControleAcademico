/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;



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

/**
 * Classe para criar e controlar tela de cadastro de disciplinas (tela_cadastro_disciplina)
 */
public class TelaCadastroDisciplina extends AppCompatActivity {
    private OperacoesBD operacoesBD;
    private Spinner psSemestres;//Buscar semestres no bd
    private String[] listaSemestres={"1° semestre","2° semestre"};
    private String[] nomesDisciplinas={"CEA036","CEA423","CSI030","CSI145","CSI427","CSI491","CSI032"
            ,"CSI443","CSI460","CSI488","ENP144","CEA307","CSI424","CSI429","CSI466","ENP473","CSI437",
    "CSI440","CSI485","ENP150","ENP153","CSI426","CSI442","CSI457","CSI476","CSI486","CSI419","CSI433",
    "CSI450","CSI477","CSI735","CSI439","CSI498","ENP126","ENP493","CSI462","CSI463","CSI499","CSI401",
            "CSI001","CSI501","CSI508","CSI473"};
    AutoCompleteTextView tvnomeDisciplina;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.tela_cadastro_disciplina);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        operacoesBD=new OperacoesBD(this);
        //spinner

        psSemestres=findViewById(R.id.spListaSemestres);
        ArrayAdapter spSemestreAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,listaSemestres);
        psSemestres.setAdapter(spSemestreAdapter);
        //end spinner
        tvnomeDisciplina=(AutoCompleteTextView) findViewById(R.id.edNomeDisciplina);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_dropdown_item_1line,nomesDisciplinas);
        tvnomeDisciplina.setAdapter(adapter);
        addButtonListener();


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
    public void addButtonListener(){
        Button bCadastrarDisciplina=(Button) findViewById(R.id.btCadastrarDisciplina);
        bCadastrarDisciplina.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
                cadastroDisciplina();

            }
        });

    }

    /**
     * Classe para cadastrar uma disciplina
     */
    public void cadastroDisciplina(){


        EditText etLimiteFaltas=(EditText)findViewById(R.id.edLimiteFaltas);
        EditText etMeta=(EditText)findViewById(R.id.edMeta);

        String nome=tvnomeDisciplina.getText().toString();
        String semestre="";
        int limiteFaltas=0,cont=0;
        double meta=0;
        System.out.println("id semestre selecionado "+psSemestres.getSelectedItemId());
        semestre=String.valueOf(psSemestres.getSelectedItem());
        try {
            limiteFaltas = Integer.parseInt(etLimiteFaltas.getText().toString());
            if(limiteFaltas<1){
                etLimiteFaltas.setText("");
                cont=1;
            }
        }catch(NumberFormatException e){
            etLimiteFaltas.setText("");
            cont=1;
        }
        try {
            meta = Double.parseDouble(etMeta.getText().toString());
        }catch (NumberFormatException e){
            etMeta.setText("");
            cont=1;
        }
        if(meta>0 && meta<=100 && cont==0 && !semestre.equals("")) {
            Disciplina d=new Disciplina(nome, semestre, limiteFaltas, meta);

          Long id= operacoesBD.inserirDisciplina(d);//inserir disciplina no banco
           Toast.makeText(this,"Disciplina inserida com sucesso",Toast.LENGTH_LONG).show(); //mensagem simples
            //abrir lista disciplinas cadastradas

            tvnomeDisciplina.setText("");
            etLimiteFaltas.setText("");
            etMeta.setText("");
            tvnomeDisciplina.requestFocus(); //o cursor volta pra ele
            finish();

        }else{
            Toast.makeText(this,"" +
                    "Por favor, insira valores válidos",Toast.LENGTH_LONG).show(); //mensagem simples

            //tvnomeDisciplina.requestFocus(); //o cursor volta pra ele
        }
    }
}
