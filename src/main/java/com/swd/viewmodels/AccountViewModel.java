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

    public AccountViewModel(ObjectId _id, String name, String email, Date dob, boolean gender) {
        this._id = _id.toHexString();
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
    }

    public AccountViewModel(ObjectId _id) throws Exception {
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
        if (doc == null) throw new Exception();
        this._id = doc.getObjectId("_id").toHexString();
        this.name = doc.getString("name");
        this.email = doc.getString("email");
        this.dob = doc.getDate("dob");
        this.gender = doc.getBoolean("gender");
    }
}
