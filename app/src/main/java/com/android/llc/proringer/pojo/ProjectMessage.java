package com.android.llc.proringer.pojo;

/**
 * Created by su on 7/29/17.
 */

public class ProjectMessage {
    String tagName="";

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
