package com.android.llc.proringer.pojo;

import org.json.JSONArray;

/**
 * Created by su on 8/8/17.
 */

public class SetGetProjectMessageDetailsData {
    String id,project_id,homeowner_id,pro_id,pro_img,pro_rating,pro_time_status,pro_user_name,pro_com_nm,message_info;
    int no_of_msg,read_status;
    JSONArray message_list;

    public String getId() {
        return id;
    }

    public int getRead_status() {
        return read_status;
    }

    public void setRead_status(int read_status) {
        this.read_status = read_status;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getHomeowner_id() {
        return homeowner_id;
    }

    public void setHomeowner_id(String homeowner_id) {
        this.homeowner_id = homeowner_id;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getPro_img() {
        return pro_img;
    }

    public void setPro_img(String pro_img) {
        this.pro_img = pro_img;
    }

    public String getPro_rating() {
        return pro_rating;
    }

    public void setPro_rating(String pro_rating) {
        this.pro_rating = pro_rating;
    }

    public String getPro_time_status() {
        return pro_time_status;
    }

    public void setPro_time_status(String pro_time_status) {
        this.pro_time_status = pro_time_status;
    }

    public String getPro_user_name() {
        return pro_user_name;
    }

    public void setPro_user_name(String pro_user_name) {
        this.pro_user_name = pro_user_name;
    }

    public String getPro_com_nm() {
        return pro_com_nm;
    }

    public void setPro_com_nm(String pro_com_nm) {
        this.pro_com_nm = pro_com_nm;
    }

    public String getMessage_info() {
        return message_info;
    }

    public void setMessage_info(String message_info) {
        this.message_info = message_info;
    }

    public int getNo_of_msg() {
        return no_of_msg;
    }

    public void setNo_of_msg(int no_of_msg) {
        this.no_of_msg = no_of_msg;
    }

    public JSONArray getMessage_list() {
        return message_list;
    }

    public void setMessage_list(JSONArray message_list) {
        this.message_list = message_list;
    }
}
