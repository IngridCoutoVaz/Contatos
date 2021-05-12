package br.edu.ifsp.scl.pdm.contatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import br.edu.ifsp.scl.pdm.contatos.model.Contato;
import br.edu.ifsp.scl.pdm.contatos.model.R;
import br.edu.ifsp.scl.pdm.contatos.model.databinding.ActivityContatoBinding;

public class ContatoActivity extends AppCompatActivity {

    private ActivityContatoBinding activityContatoBinding;
    private Contato contato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);

        activityContatoBinding = activityContatoBinding.inflate(getLayoutInflater());
        setContentView(activityContatoBinding.getRoot());
    }

    public void isCheckedAddCellPhone(View view) {
        if(activityContatoBinding.addTelefoneCb.isChecked())
            activityContatoBinding.celularEt.setVisibility(View.VISIBLE);
        else{
            activityContatoBinding.celularEt.setVisibility(View.GONE);
            activityContatoBinding.celularEt.setText("");
        }
    }

    public void onClickButton(View view) {
        contato = new Contato(
                activityContatoBinding.nomeEt.getText().toString(),
                activityContatoBinding.emailEt.getText().toString(),
                activityContatoBinding.telefoneEt.getText().toString(),
                activityContatoBinding.tipoTelefoneRg.isSelected(),
                activityContatoBinding.celularEt.getText().toString(),
                activityContatoBinding.sitePessoalEt.getText().toString()
        );
        switch (view.getId()){
            case R.id.ligarBt:
                ligacao();
                break;
            case R.id.enviarEmailBt:
                enviarEmail();
                break;
            case R.id.exportarPDFBt:
                break;
            case R.id.sitePessoalBt:
                abrirNavegador();
                break;
        }
    }


    private void ligacao(){
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + activityContatoBinding.telefoneEt.getText().toString()));
        startActivity(ligarIntent);
    }

    private void abrirNavegador(){
        Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
        abrirNavegadorIntent.setData(Uri.parse("https://" + activityContatoBinding.sitePessoalEt.getText().toString()));
        startActivity(abrirNavegadorIntent);
    }

    private void enviarEmail(){
        Intent enviarEmailIntent = new Intent(Intent.ACTION_SENDTO);
        enviarEmailIntent.setData(Uri.parse("mailto:"));
        enviarEmailIntent.putExtra(Intent.EXTRA_EMAIL,new String[]{ activityContatoBinding.emailEt.getText().toString() });
        enviarEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contato");
        enviarEmailIntent.putExtra(Intent.EXTRA_TEXT, "Teste");
        startActivity(enviarEmailIntent);
    }

}