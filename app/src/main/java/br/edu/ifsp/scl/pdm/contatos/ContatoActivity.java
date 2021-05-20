package br.edu.ifsp.scl.pdm.contatos;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import br.edu.ifsp.scl.pdm.contatos.model.Contato;
import br.edu.ifsp.scl.pdm.contatos.model.R;
import br.edu.ifsp.scl.pdm.contatos.model.databinding.ActivityContatoBinding;


public class ContatoActivity extends AppCompatActivity {

    private ActivityContatoBinding activityContatoBinding;
    private Contato contato;
    private int posicao = -1;
    private final int PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ligando (binding) objetos com as Views
        activityContatoBinding = ActivityContatoBinding.inflate(getLayoutInflater());
        setContentView(activityContatoBinding.getRoot());

        //Libera o campo de celular
        activityContatoBinding.addTelefoneCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityContatoBinding.addTelefoneCb.isChecked()){
                    activityContatoBinding.celularEt.setVisibility(View.VISIBLE);
                }   else{
                    activityContatoBinding.celularEt.setVisibility(View.GONE);
                }
            }
        });

        // Tira o botão de PDF em caso de adição de contato
        activityContatoBinding.exportarPDFBt.setVisibility(View.GONE);

        //Adiciona um título novo
        getSupportActionBar().setSubtitle("Adição de Contato");

        // Verifica se algum Contato foi recebido
        contato = (Contato) getIntent().getSerializableExtra(Intent.EXTRA_USER);

        if(contato != null){
            //Recebendo posição
            posicao = getIntent().getIntExtra(Intent.EXTRA_INDEX, -1);

            if(posicao == -1){
                activityContatoBinding.salvarBt.setVisibility(View.GONE);
                activityContatoBinding.exportarPDFBt.setVisibility(View.VISIBLE);
                alterarAtivacaoViews(false);
                getSupportActionBar().setSubtitle("Detalhes do Contato");
            }   else {
                activityContatoBinding.exportarPDFBt.setVisibility(View.GONE);
                alterarAtivacaoViews(true);
                getSupportActionBar().setSubtitle("Edição do Contato");
            }

            // Usando os dados para preencher o contato
            activityContatoBinding.nomeEt.setText(contato.getNome());
            activityContatoBinding.telefoneEt.setText(contato.getTelefone());

            if(!contato.getTelefoneCelular().equals("")){
                activityContatoBinding.addTelefoneCb.setChecked(true);
                activityContatoBinding.celularEt.setVisibility(View.VISIBLE);
                activityContatoBinding.celularEt.setText(contato.getTelefoneCelular());
            }

            activityContatoBinding.emailEt.setText(contato.getEmail());
            activityContatoBinding.sitePessoalEt.setText(contato.getSite());

            activityContatoBinding.telefoneComercialRb.setChecked(contato.isTelefoneComercial());

        }
    }

    private void alterarAtivacaoViews(boolean ativo){
        activityContatoBinding.nomeEt.setEnabled(ativo);
        activityContatoBinding.telefoneEt.setEnabled(ativo);
        activityContatoBinding.addTelefoneCb.setEnabled(ativo);
        activityContatoBinding.celularEt.setEnabled(ativo);
        activityContatoBinding.emailEt.setEnabled(ativo);
        activityContatoBinding.sitePessoalEt.setEnabled(ativo);
        activityContatoBinding.telefoneComercialRb.setEnabled(ativo);
    }

    public void isCheckedAddCellPhone(View view) {
        if(activityContatoBinding.addTelefoneCb.isChecked())
            activityContatoBinding.celularEt.setVisibility(View.VISIBLE);
        else{
            activityContatoBinding.celularEt.setVisibility(View.GONE);
            activityContatoBinding.celularEt.setText(" ");
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
            case R.id.salvarBt:
                Intent retornoIntent =  new Intent();
                retornoIntent.putExtra(Intent.EXTRA_USER, contato);
                setResult(RESULT_OK, retornoIntent);
                finish();
                break;
            case R.id.exportarPDFBt:
                verifyWriteExternalStoragePermission();
                break;
        }
    }

    private void verifyWriteExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            }   else{
                gerarDocumentoPdf();
            }
        }   else{
            gerarDocumentoPdf();
        }
    }

    private void gerarDocumentoPdf(){
        // Pegando a altura e a largura da View Raiz para gerar a imagem que vai para o PDF
        View conteudo = activityContatoBinding.getRoot();
        int largura = conteudo.getWidth();
        int altura = conteudo.getHeight();
        String comercial = "";

        //Criando o documento PDF
        PdfDocument documentoPdf = new PdfDocument();

        //Criando a configuração de uma página e iniciando uma página a partir da configuração
        PdfDocument.PageInfo configuracaoPagina = new PdfDocument.PageInfo.Builder(largura, altura, 1).create();
        PdfDocument.Page pagina = documentoPdf.startPage(configuracaoPagina);

        //Salvando os dados sem os botões
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        pagina.getCanvas().drawText("Nome: " + contato.getNome(),10, 30, paint);
        pagina.getCanvas().drawText("Email: " + contato.getEmail(),10, 60, paint);
        if(contato.getTelefone().equals("Comercial")){
            comercial = " (Comercial)";
        }
        pagina.getCanvas().drawText("Telefone: " + contato.getTelefone() + comercial,10, 90, paint);
        pagina.getCanvas().drawText("Celular: " + contato.getTelefoneCelular(),10, 120, paint);
        pagina.getCanvas().drawText("Site: " + contato.getSite(),10, 150, paint);


        documentoPdf.finishPage(pagina);

        File diretorio = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());
        try {
            File documento = new File(diretorio, contato.getNome().replace(" ", "_")+".pdf");
            documento.createNewFile();
            documentoPdf.writeTo(new FileOutputStream(documento));
            documentoPdf.close();
        } catch (IOException ioException){
            ioException.printStackTrace();
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