/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package beans;

import java.io.InputStream;

/**
 *
 * @author HP
 */
public class Document {

    private int document_id, project_id;
    private String document_name, document_nameSys, document_path, document_type, uploaded_at;
    private InputStream documentContent; // or InputStream if large

    public Document() {
    }

    public void setDocument_id(int document_id) {
        this.document_id = document_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public void setDocument_nameSys(String document_nameSys) {
        this.document_nameSys = document_nameSys;
    }

    public void setDocument_path(String document_path) {
        this.document_path = document_path;
    }

    public void setDocument_type(String document_type) {
        this.document_type = document_type;
    }

    public void setUploaded_at(String uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public void setDocumentContent(InputStream documentContent) {
        this.documentContent = documentContent;
    }

    public int getDocument_id() {
        return document_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public String getDocument_name() {
        return document_name;
    }

    public String getDocument_nameSys() {
        return document_nameSys;
    }

    public String getDocument_path() {
        return document_path;
    }

    public String getDocument_type() {
        return document_type;
    }

    public String getUploaded_at() {
        return uploaded_at;
    }

    public InputStream getDocumentContent() {
        return documentContent;
    }

//    public void setDocType(String parameter) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
//    }
}
