package com.example.pda.testerfid;

/**
 * Created by PDA on 28/09/2017.
 */

public class ListaRFID {

    public ListaRFID(){

    }

    public ListaRFID(int idLista, String RFID, String descricao){
        this.setIdLista(idLista);
        this.setRFID(RFID);
        this.setDescricao(descricao);
    }

    private int idLista;
    private String RFID;
    private String Descricao;

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }
}
