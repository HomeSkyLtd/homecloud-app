package com.homesky.homecloud_lib.notification;

import android.util.Log;

import com.homesky.homecloud_lib.model.notification.ActionResultNotification;

import java.util.HashSet;
import java.util.Set;

public class ActionResultSubject implements Subject{
    private static final String TAG = "ActionResultSubj";

    private Set<Observer> mObservers = new HashSet<>();
    private ActionResultNotification mActionResult = null;

    @Override
    public void registerObserver(Observer o) {
        mObservers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        mObservers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o : mObservers)
            o.update();
    }

    public void setActionResult(String actionResultJSON){
        mActionResult = ActionResultNotification.from(actionResultJSON);
    }

    public ActionResultNotification getActionResult() {
        return mActionResult;
    }
}
