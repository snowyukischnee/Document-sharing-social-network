package com.swd.viewmodels;

import com.swd.db.documents.models.MongoDaoBaseClass;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class AccountViewModel {
    public String _id;
    public String name;
    public String email;
    public Date dob;
    public boolean gender;

    public AccountViewModel() { }

    public AccountViewModel(ObjectId _id) throws NullPointerException {
        MongoDaoBaseClass<com.swd.db.documents.entities.Account> accdao = new MongoDaoBaseClass<>("account");
        Document doc = accdao.Find(new com.swd.db.documents.entities.Account(
                _id,
                null,
                null,
                null,
                null,
                true,
                null,
                null,
                false)
        );
        if (doc == null) throw new NullPointerException();
        this._id = doc.getObjectId("_id").toHexString();
        this.name = doc.getString("name");
        this.email = doc.getString("email");
        this.dob = doc.getDate("dob");
        this.gender = doc.getBoolean("gender");
    }
}
