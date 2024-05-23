package com.zirtoshka.zirtoshka.beans;

import com.zirtoshka.zirtoshka.beans.interfaces.AreaMXBean;
import com.zirtoshka.zirtoshka.db.HitResult;
import lombok.Getter;
import lombok.Setter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.io.Serializable;

@Named("area")
@ApplicationScoped
public class Area extends NotificationBroadcasterSupport implements Serializable, AreaMXBean {
    private int counterAll = 0;
    private int counterInTheArea = 0;

    private int sequenceNumber = 1;

    @Override
    public void inArea(HitResult hitResult) {
        System.out.printf("%f, %f", hitResult.getX(), hitResult.getY());
        if (hitResult.getX() < -5 || hitResult.getX() > 5 || hitResult.getY() < -5 || hitResult.getY() > 5) {
            final Notification notification = new Notification("Ну, ты не попал!", this, sequenceNumber++, "Ну, ты не попал!");
            sendNotification(notification);
        }
    }

    @Override
    public int getCounterAll() {
        return counterAll;
    }

    @Override
    public int getCounterInTheArea() {
        return counterInTheArea;
    }

    @Override
    public void increaseCounterAll() {
        counterAll++;
    }

    @Override
    public void increaseCounterInTheArea() {
        counterInTheArea++;
    }
}
