package com.application.BankStatement.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User
{
    @Id
    @GeneratedValue(strategy =  GenerationType.AUTO)
    private int id;
    private String name;
    private String branch;
    private String filename;
    private String filetype;
    @Lob
    private byte[] fileData;

    public User(String name, String branch, String filename, String filetype, byte[] fileData) {
        this.name = name;
        this.branch = branch;
        this.filename = filename;
        this.filetype = filetype;
        this.fileData = fileData;
    }
    public User(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
