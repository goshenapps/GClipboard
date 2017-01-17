package com.goshenapps.clipboard.MFM;


public class Fire {

    // Getter and Setter model for recycler view items
    private String clip;
    private String timestamp;
    private String key;




    public Fire(){

    }



    public Fire (String clip, String timestamp, String key) {

        this.clip = clip;
        this.timestamp = timestamp;
        this.key = key;



    }

    public String getClip() {
        return clip;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getKey() {
        return key;
    }






}
