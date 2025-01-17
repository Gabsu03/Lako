package com.example.lako.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedViewModel extends ViewModel {

    private final MutableLiveData<List<Map<String, Object>>> notificationsLiveData = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Map<String, Object>>> getNotifications() {
        return notificationsLiveData;
    }

    public void addNotification(Map<String, Object> notification) {
        List<Map<String, Object>> currentNotifications = notificationsLiveData.getValue();
        if (currentNotifications != null) {
            currentNotifications.add(notification);
            notificationsLiveData.setValue(currentNotifications);
        }
    }
}

