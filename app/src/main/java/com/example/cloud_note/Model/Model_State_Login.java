package com.example.cloud_note.Model;

public class Model_State_Login {
    private int idUer;
    private String jwt;

    public Model_State_Login() {
    }

    public Model_State_Login(int idUer, String jwt) {
        this.idUer = idUer;
        this.jwt = jwt;
    }

    public int getIdUer() {
        return idUer;
    }

    public void setIdUer(int idUer) {
        this.idUer = idUer;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
