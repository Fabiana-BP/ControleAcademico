/*
* @author Fabiana Barreto Pereira
* */
package br.ufop.workspace.trabalho.controleacademico;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


/**
 * Classe com métodos auxiliares para atualização de datas
 */
public class metodosUteis {
    /*
    * @return String com ano corrente
     */
    public static String anoCorrente(){
        Calendar cal = GregorianCalendar.getInstance();
        return cal.get(Calendar.YEAR)+"";
    }

    /*
     * @return String com data atual
     */
    public static String dataAtual(){
        Calendar cal = GregorianCalendar.getInstance();
        String ano=String.valueOf(cal.get(Calendar.YEAR));
        String mes=String.valueOf(cal.get(Calendar.MONTH));
        if(mes.toCharArray().length==1){
            mes="0"+mes;
        }
        String dia=String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        if(dia.toCharArray().length==1){
            dia="0"+dia;
        }

        return ano+mes+dia;
    }

    /*
    * @return String com o dia seguinte
     */
    public static String dataAmanha(){
        Calendar cal = GregorianCalendar.getInstance();

        cal.add(Calendar.DATE,+1);
        SimpleDateFormat formato=new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String data= formato.format(cal.getTime());

        System.out.println("data amanha "+data);
        String[]dataAux=data.split("/");
        String ano=dataAux[2];
        String mes=dataAux[1];
        if(mes.toCharArray().length==1){
            mes="0"+mes;
        }
        String dia=dataAux[0];;
        if(dia.toCharArray().length==1){
            dia="0"+dia;
        }

        return ano+mes+dia;
    }


}
