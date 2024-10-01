package com.example.mind_care.home.contacts.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.mind_care.database.AppDatabase;
import com.example.mind_care.database.contacts.ContactDao;
import com.example.mind_care.database.contacts.ContactEntity;

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
        new Thread(() -> {
            ArrayList<ContactEntity> temp = (ArrayList<ContactEntity>) contactDao.getAllContacts();
            contactsLivedata.postValue(temp);
        }).start();
    }

    public void updateContact(String contactName, String contactNumber, String contactImageSource, int contactId){
        new Thread(() -> {
            ContactEntity entity = contactDao.getContactById(contactId);
            Log.i("INFO", String.valueOf(entity.getContactName()));
            entity.setContactName(contactName);
            entity.setContactNumber(contactNumber);
            entity.setImageUri(contactImageSource);
            Log.i("INFO", contactName);
            contactDao.updateContact(entity);
            getAllContacts();
        }).start();
    }

    public void deleteContact(int contactId){
        new Thread(()->{
            contactDao.deleteContact(contactDao.getContactById(contactId));
            getAllContacts();
        }).start();
    }
}
