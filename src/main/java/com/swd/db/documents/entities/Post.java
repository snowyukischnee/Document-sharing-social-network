package com.swd.db.documents.entities;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class Post extends MongoEntityBaseClass {

    protected String title;
    protected String description;
    protected Date publicationDate;
    protected Date dateCreated;
    protected boolean enabled;

    public Post(ObjectId _id, String title, String description, Date publicationDate, Date dateCreated, boolean enabled) {
        super(_id);
        this.title = title;
        this.description = description;
        this.publicationDate = publicationDate;
        this.dateCreated = dateCreated;
        this.enabled = enabled;
    }

    @Override
    public Document ToDocument() {
        Document doc = new Document();
        doc.append("_id", _id);
        doc.append("title", title);
        doc.append("description", description);
        doc.append("publicationDate", publicationDate);
        doc.append("dateCreated", dateCreated);
        doc.append("enabled", enabled);
        return doc;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public Date getPublicationDate() { return publicationDate; }

    public void setPublicationDate(Date publicationDate) { this.publicationDate = publicationDate; }

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
