package com.example.homify;

public class Utente {
    private String nome_cognome;
    private String email;

    public Utente(String nome_cognome, String email) {
        this.nome_cognome = nome_cognome;
        this.email = email;
    }

    public Utente() {
    }

    public String getNome_cognome() {
        return nome_cognome;
    }

    public void setNome_cognome(String nome_cognome) {
        this.nome_cognome = nome_cognome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
