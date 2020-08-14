/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;

import java.io.Serializable;

/**
 * Classe responsável para criação e edição de tarefas
 */
class Tarefa implements Serializable {
    private long idDisciplina;
    private String descricao;
    private long idTarefa;
    private double valor;
    private double nota=0;
    private String dataEntrega;
    private String tipo;
    private double prioridade;//objetivo(ptos)
    private String dataAlarme;


    public Tarefa(){

    }


    public Tarefa(long idDisciplina, String descricao, String dataEntrega, String tipo, double prioridade,double valor) {
        this.idDisciplina = idDisciplina;
        this.descricao = descricao;
        this.dataEntrega = dataEntrega;
        this.tipo = tipo;
        this.prioridade = prioridade;
        this.valor=valor;
    }


    public long getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(long idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getIdTarefa() {
        return idTarefa;
    }

    public void setIdTarefa(long idTarefa) {
        this.idTarefa = idTarefa;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public String getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(String dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(double prioridade) {
        this.prioridade = prioridade;
    }

    public String toString(){
        return this.descricao;
    }


    public void setDataAlarme(String dataAlarme) {
        this.dataAlarme = dataAlarme;
    }

    public String getDataAlarme() {
        return dataAlarme;
    }
}
