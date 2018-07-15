package com.swd.db.documents.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;

public class AccountBaseClass extends MongoEntityBaseClass {
    protected String email;
    protected String password; // hashed
    protected List<String> roles;
    protected Date dateCreated;
    protected boolean enabled;

    public AccountBaseClass(ObjectId _id, String email, String password, List<String> roles, Date dateCreated, boolean enabled) {
        super(_id);
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.dateCreated = dateCreated;
        this.enabled = enabled;
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
        return doc;
    }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public List<String> getRoles() { return roles; }

    public void setRoles(List<String> roles) { this.roles = roles; }

    public Date getDateCreated() { return dateCreated; }

    public void setDateCreated(Date dateCreated) { this.dateCreated = dateCreated; }

    public boolean isEnabled() { return enabled; }

    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
