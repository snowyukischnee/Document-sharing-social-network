package com.swd.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;

public class Account implements EntityImpl {

    protected ObjectId _id;
    protected String username;
    protected String password; // hashed
    protected List<String> roles;
    protected boolean enabled;

    public Account(ObjectId _id, String username, String password, List<String> roles, boolean enabled) {
        this._id = _id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
    }

    @Override
    public Document ToDocument() {
        Document doc = new Document();
        doc.append("username", username);
        doc.append("password", password);
        doc.append("roles", roles);
        doc.append("enabled", enabled);
        return doc;
    }

    public ObjectId get_id() { return _id; }

    public void set_id(ObjectId _id) { this._id = _id; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public List<String> getRoles() { return roles; }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
