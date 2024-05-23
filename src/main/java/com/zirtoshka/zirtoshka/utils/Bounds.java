package com.zirtoshka.zirtoshka.utils;
//TODO: fix bounds
public enum Bounds {
    X_BOUNDS(-5 ,3 , true),
    Y_BOUNDS(-5,3, false),
    R_BOUNDS(2,5, true);
    private final double left;
    private final double right;
    private final boolean inclusive;

    Bounds(double left, double right, boolean inclusive){
        this.left = left;
        this.right = right;
        this.inclusive = inclusive;
    }

    public double getLeft() {
        return left;
    }

    public double getRight() {
        return right;
    }

    public boolean isInclusive() {
        return inclusive;
    }
}
