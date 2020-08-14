/*
* @author Fabiana Barreto Pereira
* */

package br.ufop.workspace.trabalho.controleacademico;

import java.io.Serializable;

/*
* Classe responsável para recuperar e modificar dados das disciplinas do curso
 */
public class Disciplina implements Serializable {
    private long idDisciplina;
    private String nome;
    private String semestre;
    private int faltas;
    private int limiteFaltas;
    private double meta;
    private String andamento;
    private long idCurso;

    public Disciplina() {
        nome = "";
        semestre = "";
        faltas = 0;
        limiteFaltas = 0;
        meta = 0;
        andamento = "Em andamento";
        idCurso = 0;
        idDisciplina=0;
    }

    public Disciplina(String nome, String semestre, int limiteFaltas, double meta) {
        this.setNome(nome);
        this.setSemestre(semestre);
        this.setLimiteFaltas(limiteFaltas);
        this.setMeta(meta);
        this.setFaltas(0);
        this.setAndamento("Em andamento");
        this.setIdDisciplina(0);
        this.setIdCurso(0);
    }

    @Override
    public String toString() {
        return nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }

    public int getLimiteFaltas() {
        return limiteFaltas;
    }

    public void setLimiteFaltas(int limiteFaltas) {
        this.limiteFaltas = limiteFaltas;
    }

    public double getMeta() {
        return meta;
    }

    public void setMeta(double meta) {
        this.meta = meta;
    }

    public String getAndamento() {
        return andamento;
    }

    public void setAndamento(String andamento) {
        this.andamento = andamento;
    }

    public long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(long idCurso) {
        this.idCurso = idCurso;
    }

    public void setIdDisciplina(long idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public long getIdDisciplina(){return idDisciplina;}



}
