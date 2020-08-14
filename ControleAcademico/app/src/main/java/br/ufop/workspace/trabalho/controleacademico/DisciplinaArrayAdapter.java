/*
* @author Fabiana Barreto Pereira
 */
package br.ufop.workspace.trabalho.controleacademico;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
* Classe para criar um ArrayAdapter personalizado para disciplinas
 */
public class DisciplinaArrayAdapter extends ArrayAdapter<Disciplina> {
    private final int resource;
    private final Context context;
    private ArrayList<Disciplina> disciplinas;

    public DisciplinaArrayAdapter(Context context, ArrayList<Disciplina> objects) {
        super(context, R.layout.item_lista_disciplina, objects);
        this.context=context;
        this.disciplinas=objects;
        resource=R.layout.item_lista_disciplina;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View disciplinaView; //Novo Layout para cada item da tela
        //"Infla a view se a view que veio como parâmetro é nula"
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            disciplinaView= inflater.inflate(resource, parent, false);
        }
        else{
            disciplinaView = convertView;
        }
        //Obtem o Text View do arquivo de layout item_lista_disciplina
        TextView tvItemListaDisciplina =(TextView) disciplinaView.findViewById(R.id.tvItemListaDisciplina);
        tvItemListaDisciplina.setText(disciplinas.get(position).getNome());

        TextView tvAndamentoListaDisciplina=(TextView) disciplinaView.findViewById(R.id.tvAndamentoListaDisciplina);
        tvAndamentoListaDisciplina.setText(disciplinas.get(position).getAndamento());

        //Obtem o ImageView do arquivo
        ImageView img = (ImageView) disciplinaView.findViewById(R.id.imageDisciplina);
        img.setImageResource(R.drawable.livro);


        return disciplinaView;
    }
}
