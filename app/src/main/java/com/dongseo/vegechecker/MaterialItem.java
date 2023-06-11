package com.dongseo.vegechecker;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MaterialItem implements Serializable {
    String material;
    String productName;
    boolean isMeat;

    public MaterialItem(String material, String productName, boolean isMeat){
        this.material = material;
        this.productName = productName;
        this.isMeat = isMeat;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean getIsMeat(){
        return isMeat;
    }
    public void setIsMeat(boolean isMeat){
        this.isMeat = isMeat;
    }
}
