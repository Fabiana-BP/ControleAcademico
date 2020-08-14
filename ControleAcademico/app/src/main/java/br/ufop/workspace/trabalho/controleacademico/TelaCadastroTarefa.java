
/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Classe para criar e controlar tela de cadastro de tarefas (tela_cadastro_tarefa)
 */
public class TelaCadastroTarefa extends AppCompatActivity {
    private OperacoesBD operacoesBD;
    private Spinner psDisciplinas,psTipos;//Buscar disciplinas no bd
    private List<Disciplina> listadisciplinas=new ArrayList<>();
    private List<String>listaTiposTarefas=new ArrayList<>();
    private EditText etDescricao,etValor,etPrioridade,etOutroTipo,etDataEntrega;
    private Button btCadastrar;
    private boolean disciplinaEspecifica;
    private long idDisciplina;
    private List<Disciplina> listaTodasDisciplinas=new ArrayList<>();
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        disciplinaEspecifica=getIntent().getBooleanExtra("disciplinaEspecifica",false); //controle de filtragem de disciplina
        idDisciplina=getIntent().getLongExtra("idDisciplina",0);
        setContentView(R.layout.tela_cadastro_tarefa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        operacoesBD=new OperacoesBD(this);
        //spinner disciplinas

        listaTodasDisciplinas=operacoesBD.exibirDisciplinas();
        if(disciplinaEspecifica==false) {
            listadisciplinas = listaTodasDisciplinas;
        }else{
            for(int i=0;i<listaTodasDisciplinas.size();i++){
                if(listaTodasDisciplinas.get(i).getIdDisciplina()==idDisciplina){
                    listadisciplinas.add(listaTodasDisciplinas.get(i));
                    break;
                }
            }

        }
        psDisciplinas=findViewById(R.id.spDisciplinaTarefa);
        ArrayAdapter<Disciplina> adapter=new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listadisciplinas);
        psDisciplinas.setAdapter(adapter);
        //end spinner

        //spinner tipos de tarefas
        listaTiposTarefas.add("Prova");
        listaTiposTarefas.add("Trabalho");
        listaTiposTarefas.add("Seminário");
        listaTiposTarefas.add("Outro");
        psTipos=findViewById(R.id.spTiposTarefas);
        ArrayAdapter<String> adapterTipos=new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaTiposTarefas);
        psTipos.setAdapter(adapterTipos);
        //end spinner

        etDescricao=(EditText) findViewById(R.id.edDescricaoTarefa);
        etValor=(EditText) findViewById(R.id.edValorTarefas);
        etPrioridade=(EditText)findViewById(R.id.edprioridadeTarefa);
        etOutroTipo=(EditText)findViewById(R.id.edOutrosTiposTarefa);
        etDataEntrega=(EditText)findViewById(R.id.edDataEntrega);
        btCadastrar=(Button) findViewById(R.id.btCadastrarTarefa);
        //Mascaras
        SimpleMaskFormatter formato=new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw=new MaskTextWatcher(etDataEntrega,formato);
        etDataEntrega.addTextChangedListener(mtw);
        etDataEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int ano=cal.get(Calendar.YEAR);
                int mes=cal.get(Calendar.MONTH);
                int dia=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog =new DatePickerDialog(TelaCadastroTarefa.this,android.R.style.Theme_DeviceDefault_Light, dateSetListener,ano,mes,dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                mes++;
                String d="",m="";
                if(dia<=9){
                    d="0"+dia;
                }else{
                    d=dia+"";
                }
                if(mes<10){
                    m="0"+mes;
                }else{
                    m=mes+"";
                }
                String data=d+"/"+m+"/"+ano;

                etDataEntrega.setText(String.valueOf(data));
            }
        };

        addButtonListener();

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
    @Override
    protected void onResume() {
        super.onResume();
        if(disciplinaEspecifica==false) {
            listadisciplinas = listaTodasDisciplinas;
        }else{
            for(int i=0;i<listaTodasDisciplinas.size();i++){
                if(listaTodasDisciplinas.get(i).getIdDisciplina()==idDisciplina){
                    listadisciplinas.add(listaTodasDisciplinas.get(i));
                    break;
                }
            }

        }
    }

    /**
     * Classe para adicinar ação aos botões
     */
    public void addButtonListener(){
        //ao selecionar Outro, aparecerá um campo para digitar o tipo
        psTipos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==3){
                    etOutroTipo.setVisibility(View.VISIBLE);
                    etOutroTipo.setEnabled(true);
                }else{
                    etOutroTipo.setVisibility(View.INVISIBLE);
                    etOutroTipo.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                etOutroTipo.setVisibility(View.INVISIBLE);
                etOutroTipo.setEnabled(false);
            }
        });

        btCadastrar.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
                cadastroTarefa();


           }
        });
    }

    /**
     * Classe para cadastrar uma tarefa
     */
    public void cadastroTarefa(){
        String tipo="",descricao="",dataEntrega="";
        double valor=0,prioridade=0;
        int cont=0;
        if(psTipos.getSelectedItemId()==3){
            tipo="Outro - "+etOutroTipo.getText().toString();
        }else{
            tipo=psTipos.getSelectedItem().toString();
        }

        descricao=etDescricao.getText().toString();

        dataEntrega=etDataEntrega.getText().toString();
        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        boolean verificaDataEntrega =false;
        try {
            sdf.parse(dataEntrega);
            verificaDataEntrega=true;
        }catch (ParseException e){
            verificaDataEntrega=false;;
        }
        //convertendo data de entrega para tipo float
        String dataEntregaFloat = "";
        if(verificaDataEntrega==false){
            etDataEntrega.setText("");
            cont=1;
        }else {
            try {
                dataEntregaFloat = dataEntrega.substring(6, 10);
                dataEntregaFloat += dataEntrega.substring(3, 5);
                dataEntregaFloat += dataEntrega.substring(0, 2);

            } catch (Exception e) {
                etDataEntrega.setText("");
                cont = 1;
            }
        }
        try{
           valor=Double.parseDouble(etValor.getText().toString());
           if(valor<0){
               cont=1;
               etValor.setText("");
           }
        }catch(NumberFormatException e){
            cont=1;
            etValor.setText("");
        }

        try{
            prioridade=Double.parseDouble(etPrioridade.getText().toString());
        }catch(NumberFormatException e){
            cont=1;
            etPrioridade.setText("");
        }
        //recuperando id da disciplina
        long idDisciplina=0;
        int aux=0;
        for(int i=0;i<listadisciplinas.size();i++){
            if(psDisciplinas.getSelectedItemId()==i){
                aux=1;
                idDisciplina=listadisciplinas.get(i).getIdDisciplina();
            }
        }

        if(prioridade>=0 && prioridade<=valor && cont==0 && tipo!="" && descricao!="" && dataEntrega!="" && aux==1){
            Tarefa tarefa=new Tarefa(idDisciplina,descricao,dataEntregaFloat,tipo,prioridade,valor);
            long id=operacoesBD.inserirTarefa(tarefa);
            if(id>=1){
                Toast.makeText(this,"Tarefa cadastrada com sucesso!",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Erro. Por favor, tente mais tarde.",Toast.LENGTH_SHORT).show();
            }
            etDescricao.setText("");
            etValor.setText("");
            etPrioridade.setText("");
            etOutroTipo.setText("");
            etDataEntrega.setText("");
            psDisciplinas.setSelection(0);
            psTipos.setSelection(0);
            finish();
        }else{
            Toast.makeText(this,"Por favor, insira valores válidos.",Toast.LENGTH_SHORT).show();

        }

    }
}
