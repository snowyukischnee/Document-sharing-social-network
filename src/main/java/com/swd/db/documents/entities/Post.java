package com.swd.db.documents.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class Post extends MongoEntityBaseClass {

    protected String description;
    protected Date dateCreated;
    protected boolean enabled;

    public Post(ObjectId _id, String description, Date dateCreated, boolean enabled) {
        super(_id);
        this.description = description;
        this.dateCreated = dateCreated;
        this.enabled = enabled;
    }

    @Override
    public Document ToDocument() {
        Document doc = new Document();
        doc.append("_id", _id);
        doc.append("description", description);
        doc.append("dateCreated", dateCreated);
        doc.append("enabled", enabled);
        return doc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
