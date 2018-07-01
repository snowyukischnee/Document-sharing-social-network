package com.swd.db.documents.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class Comment extends MongoEntityBaseClass {

    protected Date dateCreated;
    protected String content;
    protected boolean enabled;

    public Comment(ObjectId _id, Date dateCreated, String content, boolean enabled) {
        super(_id);
        this.dateCreated = dateCreated;
        this.content = content;
        this.enabled = enabled;
    }

    @Override
    public Document ToDocument() {
        Document doc = new Document();
        doc.append("_id", _id);
        doc.append("dateCreated", dateCreated);
        doc.append("content", content);
        doc.append("enabled", enabled);
        return doc;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
