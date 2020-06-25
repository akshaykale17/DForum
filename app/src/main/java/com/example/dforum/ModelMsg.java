package com.example.dforum;

public class ModelMsg {
    private String name;
    private String body;

    private ModelMsg(){}

    private ModelMsg(String body,String name) {
        this.name = name;
        this.body = body;
    }





    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
