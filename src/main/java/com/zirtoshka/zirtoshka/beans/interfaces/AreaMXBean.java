package com.zirtoshka.zirtoshka.beans.interfaces;

import com.zirtoshka.zirtoshka.db.HitResult;

public interface AreaMXBean {
    void increaseCounterAll();
    void increaseCounterInTheArea();

    int getCounterAll();

    int getCounterInTheArea();

    void inArea(HitResult hitResult);

}