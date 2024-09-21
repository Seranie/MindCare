package com.example.mind_care.home.contacts.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.mind_care.database.AppDatabase;
import com.example.mind_care.database.ContactDao;
import com.example.mind_care.database.ContactEntity;

import java.util.ArrayList;

public class ContactsViewModel extends ViewModel {
    private MutableLiveData<ArrayList<ContactEntity>> contactsLivedata = new MutableLiveData<>(new ArrayList<>());
    private AppDatabase db;
    private ContactDao contactDao;


    public ContactsViewModel(Application application) {
        db = Room.databaseBuilder(application, AppDatabase.class, "contacts_database").build();
        contactDao = db.contactDao();
    }

    public LiveData<ArrayList<ContactEntity>> getContactsLiveData(){
        return contactsLivedata;
    }

    public void insertContact(String contactName, String contactNumber, String contactImageSource){
        new Thread(()-> contactDao.insertContact(new ContactEntity(contactName, contactNumber, contactImageSource))).start();
    }

    public void getAllContacts(){
        new Thread(() -> contactsLivedata.postValue((ArrayList<ContactEntity>) contactDao.getAllContacts())).start();
    }
}
