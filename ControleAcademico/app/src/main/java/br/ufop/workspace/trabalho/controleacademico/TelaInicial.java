/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Classe para gerar tela inicial do aplicativo (tela_inicial)
 */
public class TelaInicial extends AppCompatActivity {
    private TextView tvMsg,tvNomeCurso,tvUniversidade;
    private String nomeUsuario;
    private OperacoesBD operacoesBD;
    private Button btDisciplinas, btTarefas, btNotasMetas,btNotas;
    private Calendar calendar;
    private static int frequency = 5; //vai tocar uma vez no dia
    private AlarmManager alarmManager;
    private List<Tarefa> tarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_inicial);
        String nome=getIntent().getStringExtra("USUARIO");
        nomeUsuario=nome.split(" ")[0];//pegar apenas primeiro nome do usuário

        tvMsg=(TextView)findViewById(R.id.tvMsgUsuario);
        tvNomeCurso=(TextView)findViewById(R.id.tvCursoInicio);
        tvUniversidade=(TextView) findViewById(R.id.tvUniversidadeInicio);
        tvMsg.setText(nomeUsuario);
        operacoesBD=new OperacoesBD(this);
        Curso curso=operacoesBD.exibirCurso();
        String nomeCurso=curso.getNomeCurso();
        tvNomeCurso.setText(nomeCurso);
        String universidade=curso.getUniversidade();
        tvUniversidade.setText(universidade);

        btDisciplinas=(Button)findViewById(R.id.btDisciplinasInicio);
        btTarefas=(Button)findViewById(R.id.btInicioTarefas);
        btNotas=findViewById(R.id.btInicioNotas);
        btNotasMetas=findViewById(R.id.btMetasInicio);
        addButtonListeners();
        //Set a DateFormat to define how the date info will be shown
        tarefas=operacoesBD.exibirTarefasProximas();
        //disparar notificação para todas as tarefas um dia antes da data de entrega
        for(Tarefa t:tarefas){
            configuracaoAlarme(t.getDataAlarme(),t.getDescricao());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if(Notificacao.mNotificationManager
                != null) {
            Notificacao.mNotificationManager
                    .cancel(0);
        }*/
    }

    /**
     * Método para criar menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
       finishAffinity();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case(R.id.itMenuAtualizarCurso):
                Intent it=new Intent(TelaInicial.this,TelaAtualizarCurso.class);
                startActivity(it);
                return true;
            case(R.id.sair):
                it = new Intent(getApplicationContext(), TelaLogin.class);
                it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               it.putExtra("SAIR", true);
               startActivity(it);
                finishAffinity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    /**
     * Método para adicionar ação aos botões
     */
    public void addButtonListeners(){
        btDisciplinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(TelaInicial.this,TelaDisciplina.class);
                startActivity(it);
            }
        });
        btTarefas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(TelaInicial.this,TelaTarefas.class);
                it.putExtra("ligadaDisciplina",false);
                startActivity(it);
            }
        });

        btNotasMetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent it=new Intent(TelaInicial.this,TelaGraficoMeta.class);
                startActivity(it);
            }
        });

        btNotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(TelaInicial.this,TelaGraficoNotasDisciplina.class);
                startActivity(it);
            }
        });
    }

    /**
     * Método para configurar alarme para um dia antes da data de entrega da tarefa
     * @param dataDisparar
     * @param descricao
     */
    public void configuracaoAlarme( String dataDisparar,String descricao) {
        Intent it = new Intent(this,
                Notificacao.class);
        boolean alarmeAtivo=(PendingIntent.getBroadcast(this,0,it,PendingIntent.FLAG_NO_CREATE)==null);
        if(alarmeAtivo) {//se for nulo
            Log.i("Script","Novo Alarme");
            PendingIntent p = PendingIntent.
                    getActivity(this,
                            0,
                            it, 0);
            calendar = Calendar.getInstance();
            System.out.println("dataDisparar: " + dataDisparar);
            int ano = Integer.parseInt(dataDisparar.substring(6, 10));
            int mes = Integer.parseInt(dataDisparar.substring(3, 5));
            int dia = Integer.parseInt(dataDisparar.substring(0, 2));
            calendar.set(ano, (mes-1), dia, 16, 00);
            System.out.println("hora disparar alarme: "+calendar.getTime());
            long time = calendar.getTimeInMillis();
            long timeDifference = time -
                    System.currentTimeMillis();
            System.out.println("time= " + time);
            System.out.println("time corrente = " + System.currentTimeMillis());
            alarmManager = (AlarmManager)
                    this.getSystemService(Context.ALARM_SERVICE);

            if (timeDifference <= 0) {
                return;
            }
//        int timesPerDay = 1;
//        if(! textTimesPerDay.getText().toString().equals(""))
//            timesPerDay = Integer.parseInt(textTimesPerDay.getText().toString());

            alarmManager.set(AlarmManager.RTC_WAKEUP, time,
                    p);
           /* Toast.makeText(this, "Notification will be launched within " +
                    timeDifference / 1000 + " seconds", Toast.LENGTH_SHORT).show();*/
        }else{
            Log.i("Script","Alarme já ativo");
        }
    }


}
