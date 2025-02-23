package lk.javainstitute.petbuddy.Domain;

import java.security.PrivateKey;

public class CategroyModel {

    private  int Id;
    private String Name;
    private  String Picture;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPicture() {
        return Picture;
    }

    public void setPicture(String picture) {
        Picture = picture;
    }

    public CategroyModel (){

    }

}
