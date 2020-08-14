/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe para criar e controlar tela de tarefas (tela_tarefas)
 */
public class TelaTarefas extends AppCompatActivity {
    private ListView lvTarefas;
    private ImageButton ftCadastrarTarefa;
    private List<Tarefa>tarefas=new ArrayList<>();
    private List<Disciplina> lDisciplinas=new ArrayList<>();
    private OperacoesBD operacoesBD;
    private TextView tvListaVazia;
    private long idDisciplina;
    private boolean controleTela;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controleTela=getIntent().getBooleanExtra("ligadaDisciplina",false);
        idDisciplina=getIntent().getLongExtra("idDisciplina",0); //referente a disciplina selecionada
        int notificacao=getIntent().getIntExtra("notificacao",-1);
        if(notificacao==1){
            NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(0);
        }
        setContentView(R.layout.tela_tarefas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        lvTarefas=(ListView)findViewById(R.id.lvTarefas);
        ftCadastrarTarefa=(ImageButton)findViewById(R.id.fabAdicionarTarefa);
        operacoesBD=new OperacoesBD(this);
        tvListaVazia=(TextView) findViewById(R.id.listaTarefasVazia);
        lDisciplinas=operacoesBD.exibirDisciplinas();
        try {
            if(controleTela) {//vem da telaControleDisciplina
                tarefas = operacoesBD.exibirTarefasDeDisciplina(idDisciplina);
                ArrayAdapter adapter = new TarefaArrayAdapter(this, tarefas);
                lvTarefas.setAdapter(adapter);
                if (tarefas.isEmpty()) {
                    tvListaVazia.setText("Lista Vazia");
                } else {
                    tvListaVazia.setText("");
                }
            }else{//vem da telaInicial
                tarefas = operacoesBD.exibirTarefas();
                for(Tarefa a:tarefas){
                    System.out.println(a.getIdTarefa()+" - "+a.getDescricao()+" - "+a.getDataEntrega());
                }
                ArrayAdapter adapter = new TodasTarefasArrayAdapter(this, tarefas,lDisciplinas);
                lvTarefas.setAdapter(adapter);
                if (tarefas.isEmpty()) {
                    tvListaVazia.setText("Lista Vazia");
                } else {
                    tvListaVazia.setText("");
                }
            }

        }catch(Exception exception){
            tvListaVazia.setText("Lista Vazia");
        }
        registerForContextMenu(lvTarefas);
        addClickListener();
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
    @Override
    protected void onResume() {
        super.onResume();
        if(controleTela) {//vem da telaControleTarefas
            tarefas = operacoesBD.exibirTarefasDeDisciplina(idDisciplina);
            ArrayAdapter adapter = new TarefaArrayAdapter(this, tarefas);
            lvTarefas.setAdapter(adapter);
            if (tarefas.isEmpty()) {
                tvListaVazia.setText("Lista Vazia");
            } else {
                tvListaVazia.setText("");
            }
        }else{ //vem da telaInicial
            tarefas = operacoesBD.exibirTarefas();
            ArrayAdapter adapter = new TodasTarefasArrayAdapter(this, tarefas,lDisciplinas);
            lvTarefas.setAdapter(adapter);
            if (tarefas.isEmpty()) {
                tvListaVazia.setText("Lista Vazia");
            } else {
                tvListaVazia.setText("");
            }
        }
    }

    /**
     * Método para adicionar ação aos botões
     */
    public void addClickListener(){
        //abrir tela cadastrar tarefa
        ftCadastrarTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(TelaTarefas.this,TelaCadastroTarefa.class);
                if(controleTela){//vem da telaControleDisciplina
                    it.putExtra("disciplinaEspecifica",true); //tarefas de uma disciplina específica
                    it.putExtra("idDisciplina",idDisciplina);
                }else {//vem da TelaInicial
                    it.putExtra("disciplinaEspecifica", false); //tarefas de uma disciplina específica
                }
                startActivity(it);
            }
        });
        //abrir tela atualizar tarefa
        lvTarefas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it=new Intent(TelaTarefas.this,TelaAtualizacaoTarefa.class);

                if(controleTela) {//vem da TelaControleTarefas
                    it.putExtra("disciplinaEspecifica", true); //tarefas de uma disciplina específica
                    it.putExtra("idDisciplina", idDisciplina);
                    it.putExtra("POSITION",position);
                    it.putExtra("ID",id);
                }else{//vem da Tela Inicial
                    it.putExtra("disciplinaEspecifica", false); //tarefas de uma disciplina específica
                    it.putExtra("POSITION",position);
                    it.putExtra("ID",id);
                }
                startActivity(it);
            }
        });
    }

    /**
     * Método para criar menu
     * @param menu
     * @param v
     * @param info
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info){
        super.onCreateContextMenu(menu,v,info);
        getMenuInflater().inflate(R.menu.excluir_tarefa,menu);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        Tarefa tarefaSelecionada =
                (Tarefa) lvTarefas.getItemAtPosition(info.position);
        switch (item.getItemId()){
            case R.id.itMenuExcluirTarefa:
                if(operacoesBD.excluirTarefa(tarefaSelecionada.getIdTarefa())>=0) {
                    Toast.makeText(this, "Tarefa excluída com sucesso", Toast.LENGTH_LONG).show();
                    if(controleTela) {
                        tarefas = operacoesBD.exibirTarefasDeDisciplina(idDisciplina);
                        ArrayAdapter adapter = new TarefaArrayAdapter(this, tarefas);

                        lvTarefas.setAdapter(adapter);
                    }else{
                        tarefas = operacoesBD.exibirTarefas();
                        ArrayAdapter adapter = new TodasTarefasArrayAdapter(this, tarefas,lDisciplinas);

                        lvTarefas.setAdapter(adapter);
                    }
                    if(tarefas.isEmpty()){
                        tvListaVazia.setText("Lista Vazia");
                    }else{
                        tvListaVazia.setText("");
                    }

                }else{
                    Toast.makeText(this,"Erro ao excluir a tarefa",Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onContextItemSelected(item);

        }
    }
}
