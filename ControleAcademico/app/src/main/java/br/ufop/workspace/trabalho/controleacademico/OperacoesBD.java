/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static java.util.Date.*;

/**
 * Classe para operações básicas no banco de dados
 */
public class OperacoesBD {
    private Conexao conexao;
    private SQLiteDatabase banco;

    public OperacoesBD(Context context){
        conexao=new Conexao(context);
        banco=conexao.getWritableDatabase();

    }


    /**
     * Método para inserir curso no banco
     * @param curso
     * @return id do curso
     */
    public long inserirCurso(Curso curso){
        ContentValues values=new ContentValues();
        values.put("nomeCurso",curso.getNomeCurso());
        values.put("universidade",curso.getUniversidade());
        values.put("anoInicio",curso.getAnoInicio());
        return banco.insert("curso",null,values);
    }

    /*Método para atualizar curso no banco de dados
    * @param curso
    * @return -1 se não tiver sido alterado
    * */
    public int atualizarCurso(Curso curso){
        ContentValues values=new ContentValues();
        values.put("nomeCurso",curso.getNomeCurso());
        values.put("universidade",curso.getUniversidade());
        values.put("anoInicio",curso.getAnoInicio());

        String args[]={String.valueOf(curso.getIdCurso())};
        return banco.update("curso",values,"id=?",args);
    }



    /**
     * Método para inserir uma disciplina do banco de dados
     * @param disciplina
     * @return id da disciplina
     */
    public long inserirDisciplina(Disciplina disciplina){
        ContentValues values=new ContentValues();
        values.put("nome",disciplina.getNome());
        values.put("semestre",disciplina.getSemestre());
        values.put("faltas",disciplina.getFaltas());
        values.put("limitesFaltas",disciplina.getLimiteFaltas());
        values.put("meta",disciplina.getMeta());
        values.put("andamento",disciplina.getAndamento());
        values.put("idCurso",disciplina.getIdCurso());
        return banco.insert("disciplina",null,values);//nome da tabela, se aceita colunas vazias, valores
        //retorna o id da disciplina inserida
    }

    /**
     * Método para atualizar disciplina no banco
     * @return -1 se não tiver alterado
     */
    public int atualizarDisciplina(String nome,String semestre,int limiteFaltas,double meta,String status,long id){
        ContentValues values=new ContentValues();
        values.put("nome",nome);
        values.put("semestre",semestre);
        //values.put("faltas",disciplina.getFaltas());
        values.put("limitesFaltas",limiteFaltas);
        values.put("meta",meta);
        values.put("andamento",status);
        //values.put("idCurso",disciplina.getIdCurso());

        String[]args={String.valueOf(id)};

        return banco.update("disciplina",values,"id=?",args);
    }

    /**
     * Método para atualizar faltas
     * @param faltas
     * @param id
     * @return -1 se não tiver alterado
     */
    public int atualizarFaltas(int faltas,long id){
        ContentValues values=new ContentValues();
        values.put("faltas",faltas);
        String[]args={String.valueOf(id)};
        return banco.update("disciplina",values,"id=?",args);
    }

    /**
     * Método para excluir uma disciplina
     * @param id
     * @return -1 se não tiver excluido
     */
    public int excluirDisciplina(long id){
        String[]args={String.valueOf(id)};
        return banco.delete("disciplina","id=?",args);
    }

    /**
     * Método para buscar disciplinas no bd
     * @return lista de disciplinas
     */
    public List<Disciplina> exibirDisciplinas(){
        List<Disciplina> disciplinas=new ArrayList<>();
        Cursor cursor=banco.query("disciplina",new String[]{"id","nome","semestre","andamento","faltas","limitesFaltas",
        "meta","idCurso"},null,null,null,null,"andamento ASC");// query faz consulta no banco e retorna várias linhas de dados,
                                    // o cursor aponta para essas linhas, inicialmente para a primeira
        while(cursor.moveToNext()){//se consegue mover para o próximo
            Disciplina d=new Disciplina();
            d.setIdDisciplina(cursor.getLong(0));
            d.setNome(cursor.getString(1));
            d.setSemestre(cursor.getString(2));
            d.setAndamento(cursor.getString(3));
            d.setFaltas(cursor.getInt(4));
            d.setLimiteFaltas(cursor.getInt(5));
            d.setMeta(cursor.getDouble(6));
            d.setIdCurso(cursor.getLong(7));
            disciplinas.add(d);
        }
        return disciplinas;
    }




    /**
     * Método para exibir o curso
     * @return curso
     */
    public Curso exibirCurso(){
        Curso curso=new Curso();
        Cursor cursor=banco.query("curso",new String[]{"id","nomeCurso","anoInicio","universidade"},null,null,null,null,null);
        while(cursor.moveToNext()){
            curso.setIdCurso(cursor.getLong(0));
            curso.setNomeCurso(cursor.getString(1));
            curso.setAnoInicio(cursor.getInt(2));
            curso.setUniversidade(cursor.getString(3));
        }
        return curso;
    }

    /**
     * Método para inserir tarefas
     * @param tarefa
     * @return id da tarefa
     */
    public long inserirTarefa(Tarefa tarefa){
        ContentValues values=new ContentValues();
        values.put("idDisciplina",tarefa.getIdDisciplina());
        values.put("descricao",tarefa.getDescricao());
        values.put("valor",tarefa.getValor());
        values.put("dataEntrega",tarefa.getDataEntrega());
        values.put("tipo",tarefa.getTipo());
        values.put("prioridade",tarefa.getPrioridade());
        return banco.insert("tarefa",null,values);//nome da tabela, se aceita colunas vazias, valores
        //retorna o id da disciplina inserida
    }

    /**
     * Método para buscar tarefas (em ordem de data de entrega mais próxima)
     * @return lista de tarefas
     */
    public List<Tarefa> exibirTarefas(){
        String dataEntrega="";
        List <Tarefa> tarefas=new ArrayList<>();
        Cursor cursor=banco.query("tarefa",new String[]{"id","idDisciplina",
                "descricao","valor","nota","dataEntrega","tipo","prioridade"},null,null,null,null,"dataEntrega ASC");
        while(cursor.moveToNext()){
            Tarefa tarefa=new Tarefa();
            tarefa.setIdTarefa(cursor.getLong(0));
            tarefa.setIdDisciplina(cursor.getLong(1));
            tarefa.setDescricao(cursor.getString(2));
            tarefa.setValor(cursor.getDouble(3));
            tarefa.setNota(cursor.getDouble(4));
            dataEntrega=cursor.getString(5);
            tarefa.setTipo(cursor.getString(6));
            tarefa.setPrioridade(cursor.getDouble(7));
            String dia=dataEntrega.substring(6,8);
            String mes=dataEntrega.substring(4,6);
            String ano=dataEntrega.substring(0,4);
            tarefa.setDataEntrega(dia+"/"+mes+"/"+ano);
            tarefas.add(tarefa);

        }
        return tarefas;
    }

    /**
     * Método retorna lista de tarefas com data de entrega com prazo de um dia após a data atual
     * @return lista de tarefas
     */
    public List<Tarefa> exibirTarefasProximas(){
        String dataEntrega="";
        int dataControle=Integer.parseInt(metodosUteis.dataAmanha());
        System.out.println("data controle= "+dataControle);
        String[] args={String.valueOf(dataControle)};
        List <Tarefa> tarefas=new ArrayList<>();
        Cursor cursor=banco.query("tarefa",new String[]{"id","idDisciplina",
                "descricao","valor","nota","dataEntrega","tipo","prioridade"},"dataEntrega=?",args,null,null,null);
        while(cursor.moveToNext()){
            Tarefa tarefa=new Tarefa();
            tarefa.setIdTarefa(cursor.getLong(0));
            tarefa.setIdDisciplina(cursor.getLong(1));
            tarefa.setDescricao(cursor.getString(2));
            tarefa.setValor(cursor.getDouble(3));
            tarefa.setNota(cursor.getDouble(4));
            dataEntrega=cursor.getString(5);
            tarefa.setTipo(cursor.getString(6));
            tarefa.setPrioridade(cursor.getDouble(7));

            String dia=dataEntrega.substring(6,8);
            String mes=dataEntrega.substring(4,6);
            String ano=dataEntrega.substring(0,4);
            String dataEntregaAux=dia+"/"+mes+"/"+ano;
            System.out.println("dataEntregaAux: "+dataEntregaAux);
            SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date dataEnt=new Date();
            try {
                dataEnt=formato.parse(dataEntregaAux);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //calcular data do alarme
            Calendar cal = GregorianCalendar.getInstance();;
            cal.setTime(dataEnt);
            cal.add(Calendar.DATE,-1);

            String dataAlarmeAux= formato.format(cal.getTime());
            String diaAlarme=dataAlarmeAux.substring(0,2);
            String mesAlarme=dataAlarmeAux.substring(3,5);
            String anoAlarme=dataAlarmeAux.substring(6,10);

            System.out.println("dataAlarme= "+diaAlarme+"/"+mesAlarme+"/"+anoAlarme);
            tarefa.setDataAlarme(diaAlarme+"/"+mesAlarme+"/"+anoAlarme);

            tarefa.setDataEntrega(dataEntregaAux);
            tarefas.add(tarefa);

        }
        return tarefas;
    }

    /**
     * Método retorna soma das notas das tarefas de uma disciplina específica
     * @param idDisciplina
     * @return nota total
     */
    public double exibirNotaTotalDisciplina(long idDisciplina){
        String args[]={String.valueOf(idDisciplina)};
        Cursor cursor = (Cursor) banco.rawQuery("SELECT SUM(nota) FROM tarefa  WHERE idDisciplina=?",args);
        if(cursor.moveToNext()) {
            double total = cursor.getDouble(0);
            //String total = stmt.execute();
            return total;
        }else{
            return 0;
        }
    }

    /**
     * Método retorna soma das notas das tarefas por disciplina
     * @return nota total por disciplina
     */
    public HashMap<String,Double> exibirNotaTotalPorDisciplina(){
        HashMap<String,Double> notasDisciplinas=new HashMap<>();
        String args[]={"1"};
        Cursor cursor = (Cursor) banco.rawQuery("SELECT  D.nome, SUM(T.nota) FROM tarefa T, disciplina D WHERE D.id=T.idDisciplina group by D.nome order by D.nome ASC",null);
        while(cursor.moveToNext()) {
            notasDisciplinas.put(cursor.getString(0),cursor.getDouble(1));

        }
        return notasDisciplinas;
    }

    /**
     * Método retorna soma das metas das tarefas por disciplina
     * @return meta por disciplina
     */
    public HashMap<String,Double> exibirMetaTotalPorDisciplina(){
        HashMap<String,Double> metasDisciplinas=new HashMap<>();
        Cursor cursor = (Cursor) banco.query("disciplina",new String[]{"nome", "meta"},null,null,null,null,"nome ASC");
        while(cursor.moveToNext()) {
            metasDisciplinas.put(cursor.getString(0),cursor.getDouble(1));

        }
        return metasDisciplinas;
    }

    /**
     * Método retorna soma das notas de cada tipo das tarefas de  disciplina esecífica
     * @return nota total
     */
    public HashMap<String,Double> exibirNotaPorTipoCadaDisciplina(long idDisciplina){
        HashMap<String,Double> notasTipo=new HashMap<>();
        System.out.println("idDisciplina: o "+idDisciplina);
        String args[]={String.valueOf(idDisciplina)};
        Cursor cursor = (Cursor) banco.rawQuery("SELECT tipo, SUM(nota) FROM tarefa WHERE idDisciplina=? group by tipo",args);
        try{
            while(cursor.moveToNext()) {
                String tipo=cursor.getString(0);
                if(tipo.startsWith("Outro ")){
                    String aux=tipo.substring(0,5);
                    tipo=aux;
                }
                double total=cursor.getDouble(1);
                System.out.println("tipo = "+tipo+ " total: "+total);
                notasTipo.put(tipo,total);

            }
        }finally {
            if(cursor!=null)
            cursor.close();
        }

        System.out.println("notasTipo = "+notasTipo);
        return notasTipo;
    }


    /**
     * Método retorna soma das prioridades de cada tipo das tarefas de  disciplina esecífica
     * @return nota total
     */
    public HashMap<String,Double> exibirPrioridadePorTipoCadaDisciplina(long idDisciplina){
        HashMap<String,Double> tipoPrioridade=new HashMap<>();
        System.out.println("idDisciplina: o "+idDisciplina);
        String args[]={String.valueOf(idDisciplina)};
        Cursor cursor = (Cursor) banco.rawQuery("SELECT tipo,SUM(prioridade) FROM tarefa WHERE idDisciplina=? group by tipo",args);
        try{
            while(cursor.moveToNext()) {
                String tipo=cursor.getString(0);
                if(tipo.startsWith("Outro ")){
                    String aux=tipo.substring(0,5);
                    tipo=aux;
                }
                double total=cursor.getDouble(1);
                System.out.println("tipo = "+tipo+ " total: "+total);
                tipoPrioridade.put(tipo,total);

            }
        }finally {
            if(cursor!=null)
                cursor.close();
        }

        System.out.println("TipoPrioridade = "+tipoPrioridade);
        return tipoPrioridade;
    }


    /**
     * Método para exibir tarefas de uma disciplina específica
     * @param idDisciplina
     * @return lista de tarefas
     */
    public List<Tarefa> exibirTarefasDeDisciplina(long idDisciplina){

        String dataEntrega="";
        List <Tarefa> tarefas=new ArrayList<>();
        String[]args={String.valueOf(idDisciplina)};
        Cursor cursor=banco.query("tarefa",new String[]{"id","idDisciplina",
                "descricao","valor","nota","dataEntrega","tipo","prioridade"},"idDisciplina=?",args,null,null,"dataEntrega ASC");
        while(cursor.moveToNext()){
            Tarefa tarefa=new Tarefa();
            tarefa.setIdTarefa(cursor.getLong(0));
            tarefa.setIdDisciplina(cursor.getLong(1));
            tarefa.setDescricao(cursor.getString(2));
            tarefa.setValor(cursor.getDouble(3));
            tarefa.setNota(cursor.getDouble(4));
            dataEntrega=cursor.getString(5);
            tarefa.setTipo(cursor.getString(6));
            tarefa.setPrioridade(cursor.getDouble(7));
            String dia=dataEntrega.substring(6,8);
            String mes=dataEntrega.substring(4,6);
            String ano=dataEntrega.substring(0,4);
            tarefa.setDataEntrega(dia+"/"+mes+"/"+ano);
            tarefas.add(tarefa);

        }
        return tarefas;
    }

    /**
     * Método para atualizar tarefas
     * @param tarefa
     * @return -1 se não tiver alterado
     */
    public int atualizarTarefa(Tarefa tarefa){
        ContentValues values=new ContentValues();
        values.put("descricao",tarefa.getDescricao());
        values.put("valor",tarefa.getValor());
        values.put("dataEntrega",tarefa.getDataEntrega());
        values.put("tipo",tarefa.getTipo());
        values.put("prioridade",tarefa.getPrioridade());
        values.put("nota",tarefa.getNota());

        String[]args={String.valueOf(tarefa.getIdTarefa())};

        return banco.update("tarefa",values,"id=?",args);

    }

    /**
     * Método para atualizar tarefas inclusive a disciplina
     * @param tarefa
     * @return -1 se não tiver alterado
     */
    public int atualizarTarefaCompleta(Tarefa tarefa){
        ContentValues values=new ContentValues();
        values.put("descricao",tarefa.getDescricao());
        values.put("valor",tarefa.getValor());
        values.put("dataEntrega",tarefa.getDataEntrega());
        values.put("tipo",tarefa.getTipo());
        values.put("prioridade",tarefa.getPrioridade());
        values.put("nota",tarefa.getNota());
        values.put("idDisciplina",tarefa.getIdDisciplina());

        String[]args={String.valueOf(tarefa.getIdTarefa())};

        return banco.update("tarefa",values,"id=?",args);

    }

    /**
     * Método para excluir uma tarefa
     * @param idTarefa
     * @return -1 se não tiver excluido
     */
    public int excluirTarefa(long idTarefa){
        String[]args={String.valueOf(idTarefa)};
        return banco.delete("tarefa","id=?",args);
    }

}
