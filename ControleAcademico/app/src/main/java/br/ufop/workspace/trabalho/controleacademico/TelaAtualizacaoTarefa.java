/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Classe para gerar activity tela_atualizacao_tarefas
 */
public class TelaAtualizacaoTarefa extends AppCompatActivity {
    private OperacoesBD operacoesBD;
    private Spinner psDisciplinas,psTipos;//Buscar disciplinas no bd
    private List<Disciplina> listadisciplinas=new ArrayList<>();
    private List<String>listaTiposTarefas=new ArrayList<>();
    private EditText etDescricao,etValor,etPrioridade,etOutroTipo,etDataEntrega,etNota;
    private Button btAtualizar;
    private boolean disciplinaEspecifica;
    private long idDisciplina;
    private int positionTarefa;
    private long idTarefaAtualizar=0;
    private int idTarefa;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        disciplinaEspecifica=getIntent().getBooleanExtra("disciplinaEspecifica",false); //controle de filtragem de disciplina
        idDisciplina=getIntent().getLongExtra("idDisciplina",0);
        positionTarefa=getIntent().getIntExtra("POSITION",0);
        idTarefa=getIntent().getIntExtra("ID",0);


        setContentView(R.layout.tela_atualizacao_tarefas);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        operacoesBD=new OperacoesBD(this);
        //spinner disciplinas
        List<Disciplina> listaTodasDisciplinas=new ArrayList<>();
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
        psDisciplinas=findViewById(R.id.spDisciplinaAtualizarTarefa);
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
        psTipos=findViewById(R.id.spTiposAtualizarTarefas);
        ArrayAdapter<String> adapterTipos=new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                listaTiposTarefas);
        psTipos.setAdapter(adapterTipos);
        //end spinner

        etDescricao=(EditText) findViewById(R.id.edDescricaoAtualizarTarefa);
        etValor=(EditText) findViewById(R.id.edValorAtualizarTarefas);

        etPrioridade=(EditText)findViewById(R.id.edprioridadeAtualizarTarefa);
        etOutroTipo=(EditText)findViewById(R.id.edOutrosTiposAtualizarTarefa);
        etDataEntrega=(EditText)findViewById(R.id.edDataEntregaAtualizarTarefas);
        btAtualizar=(Button) findViewById(R.id.btAtualizarTarefa);
        etNota=(EditText)findViewById(R.id.edNotaTarefa);

        carregarPagina();

        addButtonListener();


    }

    /**
     * Método responsável para carregar a página com informações atualizadas do banco de dados
     */
    public void carregarPagina(){
        List<Tarefa> tarefas=new ArrayList<>();
            String tipo="",descricao="",dataEntrega="";
            double valor=0,prioridade=0,nota=0;
        if(disciplinaEspecifica) {//vem da telaControleTarefas
            tarefas = operacoesBD.exibirTarefasDeDisciplina(idDisciplina);
        }else {//vem da telaInicial
            tarefas = operacoesBD.exibirTarefas();
            int posSpinnerDisciplina=0,cont=0;
            for(Disciplina d:listadisciplinas){
                if(tarefas.get(positionTarefa).getIdDisciplina()==d.getIdDisciplina()){
                    psDisciplinas.setSelection(posSpinnerDisciplina);
                    break;
                }
                posSpinnerDisciplina++;
            }
        }
        System.out.println(tarefas);
            tipo=tarefas.get(positionTarefa).getTipo();
            String outroTipo="";

            if(tipo.startsWith("Outro - ")){
                outroTipo=tipo.substring(8);
                tipo="Outro";
            }

            descricao=tarefas.get(positionTarefa).getDescricao();
            dataEntrega=tarefas.get(positionTarefa).getDataEntrega();
            valor=tarefas.get(positionTarefa).getValor();
            prioridade=tarefas.get(positionTarefa).getPrioridade();
            nota=tarefas.get(positionTarefa).getNota();
            idTarefaAtualizar=tarefas.get(positionTarefa).getIdTarefa();


            //spinner tipo
            int i=0;
            for(String a:listaTiposTarefas){
                if(a.equals(tipo)){
                    psTipos.setSelection(i);
                    break;
                }
                i++;
            }
            final String[] data=dataEntrega.split("/");
            etDescricao.setText(descricao);
        etDataEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int ano=Integer.parseInt(data[2]);
                int mes=Integer.parseInt(data[1])-1;
                int dia=Integer.parseInt(data[0]);
                DatePickerDialog dialog =new DatePickerDialog(TelaAtualizacaoTarefa.this,android.R.style.Theme_DeviceDefault_Light, dateSetListener,ano,mes,dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        etDataEntrega.setText(dataEntrega);
        final double finalPrioridade = prioridade;
        final double finalNota = nota;
        dateSetListener=new DatePickerDialog.OnDateSetListener() { //datepicker
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int ano, int mes, int dia) {
                //transformando a data em string
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

                //modificar a cor da nota conforme prioridade informada
                etValor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(finalNota >= finalPrioridade){
                            etNota.setTextColor(Color.BLUE);
                        }else if(finalNota < finalPrioridade){
                            etNota.setTextColor(Color.RED);
                        }else{
                            etNota.setTextColor(Color.GRAY);
                        }
                    }
                });

                etDataEntrega.setText(String.valueOf(data));
            }
        };
        //atualizar valores
        etNota.setText(String.valueOf(nota));
        etOutroTipo.setText(outroTipo);
        etPrioridade.setText(String.valueOf(prioridade));
        etValor.setText(String.valueOf(valor));
        if(nota>=prioridade){
            etNota.setTextColor(Color.BLUE);
        }else if(nota<prioridade){
            etNota.setTextColor(Color.RED);
        }else{
            etNota.setTextColor(Color.GRAY);
        }

    }

    /**
     * Método para retornar a activity anterior
     * @return super.onSupportNavigateUp()
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * Método para adicionar ações nos botões
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

        //modificar cor da nota conforme prioridade
        etNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Double.parseDouble(etNota.getText().toString())>=Double.parseDouble(etPrioridade.getText().toString())){
                    etNota.setTextColor(Color.BLUE);
                }else if(Double.parseDouble(etNota.getText().toString())<Double.parseDouble(etPrioridade.getText().toString())){
                    etNota.setTextColor(Color.RED);
                }else{
                    etNota.setTextColor(Color.GRAY);
                }
            }
        });

        //atualizar tarefas
        btAtualizar.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View v){
                atualizacaoTarefa();

            }
        });
    }

    /**
     * Método para atualizar uma tarefa
     */
    public void atualizacaoTarefa(){
        String tipo="",descricao="",dataEntrega="";

        double valor=0,prioridade=0,nota=0;
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

        try{
            nota=Double.parseDouble(etNota.getText().toString());
        }catch(NumberFormatException e){
            cont=1;
            etNota.setText("");
        }
        //modificar cor da nota
        if(nota>=prioridade){
            etNota.setTextColor(Color.BLUE);
        }else if(nota<prioridade){
            etNota.setTextColor(Color.RED);
        }else{
            etNota.setTextColor(Color.GRAY);
        }
        //recuperando id da disciplina
        long idDisciplinaAt=0;
        int aux=0;
        /*
        for(int i=0;i<listadisciplinas.size();i++){
            if(psDisciplinas.getSelectedItemId()==i){
                aux=1;
                idDisciplina=listadisciplinas.get(i).getIdDisciplina();
            }
        }*/

        for(Disciplina d:listadisciplinas){
            if(psDisciplinas.getSelectedItem().toString().equals(d.getNome())){
                aux=1;
                idDisciplinaAt=d.getIdDisciplina();
                break;
            }
        }

        //mostrar mensagem do tipo toast caso ocorra uma das condições
        if(cont==0 && prioridade>=0 && prioridade<=valor && nota>=0 && nota<=valor&&tipo!="" && !etDescricao.getText().toString().equals("") && dataEntrega!="" && aux==1){
            Tarefa tarefa=new Tarefa(idDisciplinaAt,descricao,dataEntregaFloat,tipo,prioridade,valor);
            tarefa.setNota(nota);
            tarefa.setIdTarefa(idTarefaAtualizar);
            if(disciplinaEspecifica) {
                if (operacoesBD.atualizarTarefa(tarefa) >= 1) {
                    Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erro. Por favor, tente mais tarde.", Toast.LENGTH_SHORT).show();
                }
            }else{
                if (operacoesBD.atualizarTarefaCompleta(tarefa) >= 1) {
                    Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Erro. Por favor, tente mais tarde.", Toast.LENGTH_SHORT).show();
                }
            }

            finish();
        }else{
            Toast.makeText(this,"Por favor, insira valores válidos!",Toast.LENGTH_SHORT).show();
        }


    }
}
