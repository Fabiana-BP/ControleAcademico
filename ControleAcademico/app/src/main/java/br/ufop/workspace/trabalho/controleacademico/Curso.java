/*
* @author Fabiana Barreto Pereira
* */
package br.ufop.workspace.trabalho.controleacademico;

import java.io.Serializable;

/**
 * Classe responsável para recuperar e modificar dados do Curso do aluno
 */
public class Curso implements Serializable {
    private long idCurso;
    private String nomeCurso;
    private String universidade;
    private int anoInicio;

    public Curso(){

    }

    public Curso(String nomeCurso,String universidade,int anoInicio){
        this.nomeCurso=nomeCurso;
        this.universidade=universidade;
        this.anoInicio=anoInicio;
    }

    public long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(long idCurso) {
        this.idCurso = idCurso;
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getUniversidade() {
        return universidade;
    }

    public void setUniversidade(String universidade) {
        this.universidade = universidade;
    }

    public int getAnoInicio() {
        return anoInicio;
    }

    public void setAnoInicio(int anoInicio) {
        this.anoInicio = anoInicio;
    }
}
