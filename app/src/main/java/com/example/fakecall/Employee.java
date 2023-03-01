package com.example.fakecall;

import java.io.Serializable;

public class Employee implements Serializable {
    private  boolean ischecked = false ;
    protected  String name ;

    public boolean isIschecked() {
        return ischecked;
    }

    public String getName() {
        return name;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public void setName(String name) {
        this.name = name;
    }
}
