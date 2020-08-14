/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;


import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Classe para criar e controlar tela de disciplinas (tela_disciplinas)
 */
public class TelaDisciplina extends AppCompatActivity {
    private OperacoesBD operacoesBD;
    private ArrayList<Disciplina> disciplinas;
    private ImageButton fab;
    private ListView lvDisciplinas;
    private TextView tvListaVazia;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.tela_disciplinas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        operacoesBD=new OperacoesBD(this);
        disciplinas= (ArrayList<Disciplina>) operacoesBD.exibirDisciplinas();
        lvDisciplinas=(ListView) findViewById(R.id.lvDisciplinas);
        ArrayAdapter adapter= new DisciplinaArrayAdapter(this,disciplinas);
        lvDisciplinas.setAdapter(adapter);
        fab=(ImageButton)findViewById(R.id.fabAdicionarDisciplina);
        tvListaVazia=(TextView)findViewById(R.id.listaDisciplinaVazia);

        if (disciplinas.isEmpty()) {
            tvListaVazia.setText("Lista Vazia");
        } else {
            tvListaVazia.setText("");
        }
        registerForContextMenu(lvDisciplinas);
        addClickListener();

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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        disciplinas= (ArrayList<Disciplina>) operacoesBD.exibirDisciplinas();
        ArrayAdapter adapter= new DisciplinaArrayAdapter(this,disciplinas);
        super.onResume();
        lvDisciplinas.setAdapter(adapter);
        if (disciplinas.isEmpty()) {
            tvListaVazia.setText("Lista Vazia");
        } else {
            tvListaVazia.setText("");
        }
    }


    /**
     * Classe para adicionar ações nos botões
     */
    public void addClickListener(){
        //botao flutuante
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(TelaDisciplina.this,TelaCadastroDisciplina.class);
                startActivity(it);
            }
        });
        //escolher uma disciplina da lista
        lvDisciplinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // se for a partir de uma activity
                Intent it=new Intent(TelaDisciplina.this,TelaControleDisciplina.class);
                // set no intent o id ou a position do item selecionado
                it.putExtra("ID", id);
                it.putExtra("POSITION", position);
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
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo info){
        super.onCreateContextMenu(menu,v,info);
        getMenuInflater().inflate(R.menu.editar_disciplina,menu);

    }

    /**
     * Método para indicar o que fazer caso selecione um item no menu suspenso da disciplina (editar ou excluir)
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        Disciplina disciplinaSelecionada =
                (Disciplina) lvDisciplinas.getItemAtPosition(info.position);
        switch (item.getItemId()){
            case R.id.itMenuEditarDisciplina:
                Intent it=new Intent(TelaDisciplina.this,TelaAtualizacaoDisciplina.class);
                it.putExtra("nomeDisciplina",disciplinaSelecionada.getNome());
                it.putExtra("semestreDisciplina",disciplinaSelecionada.getSemestre());
                it.putExtra("metaDisciplina",disciplinaSelecionada.getMeta());
                it.putExtra("limiteFaltasDisciplina",disciplinaSelecionada.getLimiteFaltas());
                it.putExtra("id",disciplinaSelecionada.getIdDisciplina());
                it.putExtra("statusDisciplina",disciplinaSelecionada.getAndamento());
                System.out.println("semestre 1 tela -"+disciplinaSelecionada.getSemestre());
                startActivity(it);
                return true;
            case R.id.itMenuExcluirDisciplina:
                if(!operacoesBD.exibirTarefasDeDisciplina(disciplinaSelecionada.getIdDisciplina()).isEmpty()){
                    Toast.makeText(this,"Há tarefas associadas a essa disciplina",Toast.LENGTH_SHORT).show();
                }else {
                    if (operacoesBD.excluirDisciplina(disciplinaSelecionada.getIdDisciplina()) >= 0) {
                        Toast.makeText(this, "Disciplina excluída com sucesso", Toast.LENGTH_LONG).show();
                        disciplinas = (ArrayList<Disciplina>) operacoesBD.exibirDisciplinas();
                        lvDisciplinas = (ListView) findViewById(R.id.lvDisciplinas);
                        ArrayAdapter adapter = new DisciplinaArrayAdapter(this, disciplinas);
                        lvDisciplinas.setAdapter(adapter);

                    } else {
                        Toast.makeText(this, "Erro ao excluir a disciplina", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grafico_geral,menu);
        return true;
    }

    /**
     * Método para abrir novas telas dependendo do item selecionado
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case(R.id.action_notas):
                Intent it=new Intent(TelaDisciplina.this,TelaGraficoNotasDisciplina.class);
                startActivity(it);
                return true;
            case(R.id.action_notas_metas):
                it = new Intent(TelaDisciplina.this,TelaGraficoMeta.class);
                startActivity(it);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

}
