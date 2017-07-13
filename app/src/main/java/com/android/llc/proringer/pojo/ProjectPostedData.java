package com.android.llc.proringer.pojo;

/**
 * Created by manishsethia on 12/07/17.
 */

public class ProjectPostedData {
    boolean statusActive=false;
    boolean accepted=false;
    boolean messages=false;

    public ProjectPostedData(boolean statusActive, boolean accepted, boolean messages) {
        this.statusActive = statusActive;
        this.accepted = accepted;
        this.messages = messages;
    }

    public boolean isStatusActive() {
        return statusActive;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public boolean isMessages() {
        return messages;
    }
}
