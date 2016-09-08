package com.homesky.homecloud.observer;

import com.homesky.homecloud_lib.notification.ActionResultSubject;
import com.homesky.homecloud_lib.notification.Observer;

public abstract class ActionResultObserver implements Observer {
    private ActionResultSubject mSubject;

    public ActionResultObserver(ActionResultSubject subject){
        mSubject = subject;
    }

    public ActionResultSubject getSubject() {
        return mSubject;
    }

    @Override
    public abstract void update();
}
