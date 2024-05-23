package com.zirtoshka.zirtoshka.beans;

import com.zirtoshka.zirtoshka.beans.interfaces.SquareMXBean;

import java.io.Serializable;

public class Square implements SquareMXBean, Serializable {

    double squareValue = 0;

    @Override
    public void getSquare(double r) {
        squareValue = r * r * (1 / 2 + 1 / 8 + Math.PI / 4);
    }

    @Override
    public double getSquareValue() {
        return squareValue;
    }
}
