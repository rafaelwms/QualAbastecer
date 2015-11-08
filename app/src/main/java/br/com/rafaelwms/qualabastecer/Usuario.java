package br.com.rafaelwms.qualabastecer;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Rafael on 03/11/2015.
 */
public class Usuario implements Serializable{

    private int id;
    private String email;
    private String login;
    private String senha;
    private Calendar dataCriacao;
    private Calendar dataAlteracao;

    public Usuario(String email, String login, String senha) {
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.dataCriacao = Calendar.getInstance();
    }

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Calendar getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Calendar dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Calendar getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Calendar dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
