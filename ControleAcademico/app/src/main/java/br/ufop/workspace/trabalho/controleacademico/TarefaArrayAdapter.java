/**
 * @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Classe para criar um ArrayAdapter personalizado para tarefas
 */
public class TarefaArrayAdapter extends ArrayAdapter<Tarefa> {
    private final int resource;
    private final Context context;
    private List<Tarefa> tarefas;

    public TarefaArrayAdapter(Context context, List<Tarefa> objects) {
        super(context,R.layout.item_lista_tarefa, objects);
        this.context=context;
        this.tarefas=objects;
        resource=R.layout.item_lista_tarefa;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View tarefaView; //Novo Layout para cada item da tela
        //"Infla a view se a view que veio como parâmetro é nula"
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            tarefaView= inflater.inflate(resource, parent, false);
        }
        else{
            tarefaView = convertView;
        }
        //Obtem o Text View do arquivo de layout item_lista_disciplina
        TextView tvItemListaTarefa =(TextView) tarefaView.findViewById(R.id.tvItemListaTarefa);
        tvItemListaTarefa.setText(tarefas.get(position).getDescricao());

        TextView tvDataEntrega=(TextView) tarefaView.findViewById(R.id.tvDataEntregaListaTarefa);
        String textoDataEntrega="data de entrega: "+tarefas.get(position).getDataEntrega();
        tvDataEntrega.setText(textoDataEntrega);

        //Obtem o ImageView do arquivo
        ImageView img = (ImageView) tarefaView.findViewById(R.id.imageTarefa);
        img.setImageResource(R.drawable.tarefa);


        return tarefaView;
    }
}
