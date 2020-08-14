/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe para atualizar o curso
 */
public class TelaAtualizarCurso extends AppCompatActivity {
    private EditText etNomeCurso,etUniversidade,etAnoInicio;
    private Button btAtualizar;
    private OperacoesBD operacoesBD;
    private TextView tvMsgInicial,tvMsgComplementar;
    private Curso curso=new Curso();
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.tela_cadastro_curso);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tvMsgInicial=(TextView)findViewById(R.id.tvMSGBemVindo);
        tvMsgComplementar=(TextView)findViewById(R.id.tvMSGComplementar);

        etNomeCurso=(EditText)findViewById(R.id.etNomeCurso);
        etUniversidade=(EditText)findViewById(R.id.etUniversidade);
        etAnoInicio=(EditText)findViewById(R.id.etAnoInicio);
        btAtualizar=(Button)findViewById(R.id.btAcessar);
        btAtualizar.setText("Atualizar");
        operacoesBD=new OperacoesBD(this);
        carregarPagina();
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

    @Override
    protected void onResume() {
        super.onResume();
        carregarPagina();
    }

    /**
     * Método para carregar página com os dados atualizados do curso
     */
    public void carregarPagina(){
         tvMsgInicial.setText("");
         tvMsgComplementar.setText("");
        curso=operacoesBD.exibirCurso();
        etNomeCurso.setText(curso.getNomeCurso());
        etUniversidade.setText(curso.getUniversidade());
        etAnoInicio.setText(String.valueOf(curso.getAnoInicio()));
    }

    /**
     * Método para adicionar ação aos botões
     */
    public void addButtonListener(){
        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeCurso="",universidade="";
                int anoInicio=0;
                nomeCurso=etNomeCurso.getText().toString();
                universidade=etUniversidade.getText().toString();
                try{
                    anoInicio=Integer.parseInt(etAnoInicio.getText().toString());
                    if(anoInicio<2000){
                        etAnoInicio.setText("");
                        anoInicio=0;
                    }
                }catch (NumberFormatException e){
                    etAnoInicio.setText("");
                    anoInicio=0;
                }
                if(!nomeCurso.equals("") && !universidade.equals("") && anoInicio!=0){
                    Curso c=new Curso(nomeCurso,universidade,anoInicio);
                    c.setIdCurso(curso.getIdCurso());
                    System.out.println("atu"+operacoesBD.atualizarCurso(c));
                    Toast.makeText(TelaAtualizarCurso.this,"Curso atualizado com sucesso!",Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(TelaAtualizarCurso.this,"Por favor, insira valores válidos",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
