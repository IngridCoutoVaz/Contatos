package br.edu.ifsp.scl.pdm.contatos.model;

import java.io.Serializable;

public class Contato implements Serializable {

    private String nome;
    private String email;
    private String telefone;
    private boolean telefoneComercial;
    private String telefoneCelular;
    private String site;

    public Contato() {
    }

    public Contato(String nome, String email, String telefone, boolean telefoneComercial, String telefoneCelular, String site) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.telefoneComercial = telefoneComercial;
        this.telefoneCelular = telefoneCelular;
        this.site = site;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isTelefoneComercial() {
        return telefoneComercial;
    }

    public void setTelefoneComercial(boolean telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "Contato{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", telefoneComercial=" + telefoneComercial +
                ", telefoneCelular='" + telefoneCelular + '\'' +
                ", site='" + site + '\'' +
                '}';
    }
}
