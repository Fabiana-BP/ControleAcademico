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
 * Classe para criar e controlar tela de cadastrar um curso (tela_cadastro_curso)
 */
public class TelaCadastroCurso extends AppCompatActivity {
    private EditText etNomeCurso,etUniversidade,etAnoInicio;
    private TextView tvMsgInicial,tvMsgComplementar;
    private Button btAcessar;
    private OperacoesBD operacoesBD;
    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        setContentView(R.layout.tela_cadastro_curso);
        //tvMsgInicial=(TextView)findViewById(R.id.tvMSGBemVindo);
        //tvMsgComplementar=(TextView)findViewById(R.id.tvMSGComplementar);
        //tvMsgInicial.setVisibility(View.VISIBLE);
        //tvMsgComplementar.setVisibility(View.VISIBLE);
        etNomeCurso=(EditText)findViewById(R.id.etNomeCurso);
        etUniversidade=(EditText)findViewById(R.id.etUniversidade);
        etAnoInicio=(EditText)findViewById(R.id.etAnoInicio);
        btAcessar=(Button)findViewById(R.id.btAcessar);
        btAcessar.setText("Acessar");
        operacoesBD=new OperacoesBD(this);
        addButtonListener();
    }

    /**
     * Método para adicionar ação aos botões
     */
    public void addButtonListener(){
        btAcessar.setOnClickListener(new View.OnClickListener() {
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
                    Curso curso=new Curso(nomeCurso,universidade,anoInicio);
                    operacoesBD.inserirCurso(curso);
                    Toast.makeText(TelaCadastroCurso.this,"Curso cadastrado com sucesso!",Toast.LENGTH_LONG).show();
                    etAnoInicio.setText("");
                    etUniversidade.setText("");
                    etNomeCurso.setText("");
                    Intent it=new Intent(TelaCadastroCurso.this,TelaInicial.class);
                    startActivity(it);
                }else{
                    etAnoInicio.setText("");
                    etUniversidade.setText("");
                    etNomeCurso.setText("");
                    Toast.makeText(TelaCadastroCurso.this,"Por favor, insira valores válidos",Toast.LENGTH_LONG);
                }
            }
        });
    }
}
