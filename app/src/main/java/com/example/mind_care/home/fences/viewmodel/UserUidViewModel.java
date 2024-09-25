package com.example.mind_care.home.fences.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mind_care.SingleLiveEvent;
import com.example.mind_care.home.fences.repository.UserUidRepository;

public class UserUidViewModel extends ViewModel implements UserUidRepository.validated {
    private final UserUidRepository repository;
    private final MutableLiveData<String> userUidLiveData = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> isValidLiveData = new SingleLiveEvent<>();

    public UserUidViewModel() {
        repository = new UserUidRepository();
    }

    public MutableLiveData<String> getUserUidLiveData() {
        return userUidLiveData;
    }

    public SingleLiveEvent<Boolean> getIsValidLiveData() {
        return isValidLiveData;
    }


    public void setLinkedAccount(String Uid) {
        new Thread(() -> repository.setLinkedAccount(Uid)).start();
    }


    public void validateUid(String Uid) {
        new Thread(() -> repository.validateUid(Uid, this)).start();
    }

    @Override
    public void isValid(boolean isValid) {
        isValidLiveData.setValue(isValid);
    }
}
