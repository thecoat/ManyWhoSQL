package com.manywho.services.sql.exceptions;

public class DataBaseTypeNotSupported extends Exception {
    public DataBaseTypeNotSupported(String typeNotSupported){
        super("database type " + typeNotSupported + " not supported");
    }
}
