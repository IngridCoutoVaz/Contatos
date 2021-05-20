package br.edu.ifsp.scl.pdm.contatos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import br.edu.ifsp.scl.pdm.contatos.model.Contato;
import br.edu.ifsp.scl.pdm.contatos.model.R;
import br.edu.ifsp.scl.pdm.contatos.model.databinding.ActivityContatosBinding;


public class ContatosActivity extends AppCompatActivity {
    private ActivityContatosBinding activityContatosBinding;
    private ArrayList<Contato> contatosList;
    private ContatosAdapter contatosAdapter;
    private final int NOVO_CONTATO_REQUEST_CODE = 0;
    private final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;
    private final int EDITAR_CONTATO_REQUEST_CODE = 2;

    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContatosBinding = ActivityContatosBinding.inflate(getLayoutInflater());
        setContentView(activityContatosBinding.getRoot());

        // Instanciar o data source
        contatosList = new ArrayList<>();
        popularContatosList();

        // Instanciar o adapter
        contatosAdapter = new ContatosAdapter(
                this,
                android.R.layout.simple_list_item_1,
                contatosList
        );

        // Associando o adapter com o listView
        activityContatosBinding.contatosLv.setAdapter(contatosAdapter);

        // Registrando listView para o menu de contexto
        registerForContextMenu(activityContatosBinding.contatosLv);

        // Associar um listener de clique para o listView
        activityContatosBinding.contatosLv.setOnItemClickListener(((parent, view, position, id) -> {
            contato = contatosList.get(position);
            Intent detalhesIntent = new Intent(this, ContatoActivity.class);
            detalhesIntent.putExtra(Intent.EXTRA_USER, contato);
            startActivity(detalhesIntent);
        }));
    }

    //Teste
    private void popularContatosList(){
        contatosList.add(
                new Contato(
                        "Ingrid Vaz",
                        "ingrid@gmail.com",
                        "1699631429",
                        false,
                        " ",
                        "www.ingrid.com"
                )
        );
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contatos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.novoContatoMi) {
            Intent novoContatoIntent = new Intent(this, ContatoActivity.class);
            startActivityForResult(novoContatoIntent, NOVO_CONTATO_REQUEST_CODE);
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NOVO_CONTATO_REQUEST_CODE && resultCode == RESULT_OK) {
            Contato contato = (Contato) data.getSerializableExtra(Intent.EXTRA_USER); // modo sem criar constante
            if (contato != null) {
                contatosList.add(contato);
                contatosAdapter.notifyDataSetChanged(); // notifica o adapter a alteracao no conjunto de dados
            }
        } else if(requestCode == EDITAR_CONTATO_REQUEST_CODE && resultCode == RESULT_OK){
            //Atualiza o contato
            Contato contato = (Contato) data.getSerializableExtra(Intent.EXTRA_USER);
            int posicao = data.getIntExtra(Intent.EXTRA_INDEX, -1);

            if (contato != null && posicao != -1){
                contatosList.remove(posicao);
                contatosList.add(posicao, contato);
                contatosAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu_contato, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //Pegando o contato a partir da posição indicada
        contato = contatosAdapter.getItem(menuInfo.position);

        switch (item.getItemId()){
            case R.id.enviarEmailMi:
                Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto: "));
                enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{contato.getEmail()});
                enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, contato.getNome());
                enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, contato.toString());
                startActivity(enviarEmailIntent);
                return true;
            case R.id.ligarMi:
                verifyCallPhonePermission();
                return true;
            case R.id.acessarSiteMi:
                Intent acessarSitePessoalIntent = new Intent(Intent.ACTION_VIEW);
                acessarSitePessoalIntent.setData(Uri.parse("https://" + contato.getSite()));
                startActivity(acessarSitePessoalIntent);
                return true;
            case R.id.editarContatoMi:
                Intent editarContatoIntent = new Intent(this, ContatoActivity.class);
                editarContatoIntent.putExtra(Intent.EXTRA_USER, contato);
                editarContatoIntent.putExtra(Intent.EXTRA_INDEX, menuInfo.position);
                startActivityForResult(editarContatoIntent, EDITAR_CONTATO_REQUEST_CODE);
                return true;
            case R.id.removerContatoMi:
                contatosList.remove(menuInfo.position);
                contatosAdapter.notifyDataSetChanged();
                return true;
            default:
                return false;
        }
    }

    private void verifyCallPhonePermission() {
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel: " + contato.getTelefone()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                startActivity(ligarIntent);
            }   else{
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST_CODE);
            }
        }   else{
            startActivity(ligarIntent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE){
            if(permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "É necessário esta permissão para essa funcionalidade", Toast.LENGTH_SHORT).show();
            }
            verifyCallPhonePermission();
        }
    }
}