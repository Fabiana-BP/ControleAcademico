/*
* @author Fabiana Barreto Pereira
* */
package br.ufop.workspace.trabalho.controleacademico;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
**Classe responsável pela conexão ao banco SQLite
*/
public class Conexao extends SQLiteOpenHelper {
    private static final String name="banco.db";//nome do banco de dados
    private static final int version=1;//versão do banco
    public Conexao(Context context) {
        super(context, name,null, version);
    }



    /*
    **Método para criação de tabelas
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table if not exists curso(id integer primary Key autoincrement, " +
                " nomeCurso text not null, anoInicio integer not null, universidade text not null)");


        db.execSQL("create table if not exists disciplina(id integer primary key autoincrement, " +
                " nome text not null,semestre text not null,faltas integer,limitesFaltas integer not null," +
                "meta integer not null,andamento text not null,idCurso integer not null," +
                "foreign key (idCurso) references curso(id))");

        db.execSQL("create table if not exists tarefa(id integer primary key autoincrement, " +
                "idDisciplina integer not null, descricao text, valor real not null, nota real," +
                "dataEntrega integer not null, tipo text not null, prioridade real not null," +
                "foreign key (idDisciplina) references disciplina(id))");


    }

    /*
    **Método para exclusão de tabelas
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE tarefa");
        db.execSQL("DROP TABLE disciplina");
        db.execSQL("DROP TABLE semestre");
        db.execSQL("DROP TABLE curso");
        onCreate(db);
    }
}
