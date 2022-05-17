package it.unisa.passchain.utils;

import it.unisa.passchain.MyCallback;

import java.util.ArrayList;

public class CredentialsList {

    private ArrayList<Credential> credentials;

    public CredentialsList(){
       credentials = new ArrayList<>();
        for (int i = 0; i < MyCallback.getSizeJson(); i++)
            credentials.add(i, MyCallback.getCredential(i));
    }

    public void addCredential(Credential credential){
        MyCallback.addCredential(credential);
    }

    public int searchCredential(String name) {
        for (int i = 0; i < credentials.size(); i++)
            if (credentials.get(i).getName().compareTo(name) == 0)
                return i;
        return -1;
    }

    public void setList(int pos, Credential credential){
        MyCallback.setCredential(pos, credential);
    }

    public void remove(int pos){
        MyCallback.remove(pos);
    }

    public ArrayList<Credential> getCredentials() {
        credentials = new ArrayList<>();
        for (int i = 0; i < MyCallback.getSizeJson(); i++)
            credentials.add(i, MyCallback.getCredential(i));
        return credentials;
    }
}
