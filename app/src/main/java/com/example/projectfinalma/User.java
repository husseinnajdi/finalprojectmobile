package com.example.projectfinalma;

public class User {
    private  String username,password;
    private int id;

public User(String username,String password){
    this.username=username;
    this.password=password;
}

   public int getId() {
    return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
