package br.edu.ifsp.scl.pdm.contatos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import br.edu.ifsp.scl.pdm.contatos.model.Contato;
import br.edu.ifsp.scl.pdm.contatos.model.databinding.ViewContatoBinding;

public class ContatosAdapter extends ArrayAdapter<Contato> {
        public ContatosAdapter(Context contexto, int layout, ArrayList<Contato> contatosList){
            super(contexto, layout, contatosList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewContatoBinding viewContatoBinding;
            ContatoViewHolder contatoViewHolder;

            //Se é necerrásio inflar uma nova célula
            if(convertView == null){
                // Instancia a classe de View Biding que infla uma nova célula
                viewContatoBinding = ViewContatoBinding.inflate(LayoutInflater.from(getContext()));

                //Atribui a nova célula a View que será devolvida preenchida para a ListView
                convertView = viewContatoBinding.getRoot();

                // Pega e guarda referências para as Views Internas
                contatoViewHolder = new ContatoViewHolder();
                contatoViewHolder.nomeContatoTv = viewContatoBinding.nomeContatoTv;
                contatoViewHolder.emailContatoTv = viewContatoBinding.emailContatoTv;

                //Associa a View da célula ao Holder que referencia suas Views Internas
                convertView.setTag(contatoViewHolder);
            }

            // Pega o Holder associado a célula
            contatoViewHolder = (ContatoViewHolder) convertView.getTag();

            //Atualizar os valores dos TextViews
            Contato contato = getItem(position);
            contatoViewHolder.nomeContatoTv.setText(contato.getNome());
            contatoViewHolder.emailContatoTv.setText(contato.getEmail());

            return convertView;
        }

        private class ContatoViewHolder{
            public TextView nomeContatoTv;
            public TextView emailContatoTv;
        }
}
