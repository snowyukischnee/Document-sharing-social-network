package com.swd.viewmodels;

import org.bson.types.ObjectId;

import java.util.Date;

public class AccountViewModel {
    public ObjectId _id;
    public String name;
    public String email;
    public Date dob;
    public boolean gender;

    public AccountViewModel() { }

    public AccountViewModel(ObjectId _id, String name, String email, Date dob, boolean gender) {
        this._id = _id;
        this.name = name;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
    }
}
