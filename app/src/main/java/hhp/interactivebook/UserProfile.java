package hhp.interactivebook;

import android.text.Editable;

import java.io.Serializable;

/**
 * Created by hhphat on 7/1/2015.
 */
public class UserProfile implements Serializable {
    private static final long serialVersionUID = 46543445;
    private String name;
    private String fb;
    private String email;
    private int points;
    private int[] pointOfLevels;
    public UserProfile(String name,String email, String fb){
        setName(name);
        setEmail(email);
        setFb(fb);
        points = 0;
    }


    public void raisePoint(int points){
        this.points += points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        int num=0;
        pointOfLevels = new int [] {5,20,50,100};

        while (points>=pointOfLevels[num]){
            num++;
        }
        return num;
    }
    public boolean isLevelUp(){
        int num=0;
        pointOfLevels = new int [] {5,20,50,100};
        while (num<4 ){
            if ( points==pointOfLevels[num]) return true;
            num++;
        }
        return false;
    }
    public int getPoints() {
        return points;
    }


    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
