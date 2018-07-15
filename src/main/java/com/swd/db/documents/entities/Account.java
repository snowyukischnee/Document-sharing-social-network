package com.swd.db.documents.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class Account extends AccountBaseClass {
    protected String name;
    protected Date dob;
    protected boolean gender;

    public Account(ObjectId _id, String email, String password, List<String> roles, Date dateCreated, boolean enabled, String name, Date dob, boolean gender) {
        super(_id, email, password, roles, dateCreated, enabled);
        this.name = name;
        this.dob = dob;
        this.gender = gender;
    }

    @Override
    public Document ToDocument() {
        Document doc = new Document();
        doc.append("_id", _id);
        doc.append("email", email);
        doc.append("password", password);
        doc.append("roles", roles);
        doc.append("dateCreated", dateCreated);
        doc.append("enabled", enabled);
        doc.append("name", name);
        doc.append("dob", dob);
        doc.append("gender", gender);
        return doc;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Date getDob() { return dob; }

    public void setDob(Date dob) { this.dob = dob; }

    public boolean isGender() { return gender; }

    public void setGender(boolean gender) { this.gender = gender; }
}
