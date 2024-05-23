package com.zirtoshka.zirtoshka.utils;

import com.zirtoshka.zirtoshka.beans.Coordinates;

public class Validation {
    static  public boolean validate(Coordinates coordinates) {
        return validateVariable(coordinates.getX(), Bounds.X_BOUNDS) && validateVariable(coordinates.getY(), Bounds.Y_BOUNDS) && validateVariable(coordinates.getR(), Bounds.R_BOUNDS);
    }
    static public boolean validateVariable(double var, Bounds bounds){
        if (var > bounds.getLeft() && var < bounds.getRight()) return true;
        return bounds.isInclusive() && (var == bounds.getLeft() || var == bounds.getRight());
    }
}
