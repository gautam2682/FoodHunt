package com.example.gautam.foodhunt.Modal;

/**
 * Created by gautam on 14/12/16.
 */

public class ProductVersion {
    private String p_id;
    private String p_name;
    private String p_info;
    private String p_sold;
    private String p_image;
    private boolean isliked;
    private String p_star;
    private String noi;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getNoi() {
        return noi;
    }

    public void setNoi(String noi) {
        this.noi = noi;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_info() {
        return p_info;
    }

    public void setP_info(String p_info) {
        this.p_info = p_info;
    }

    public String getP_sold() {
        return p_sold;
    }

    public void setP_sold(String p_sold) {
        this.p_sold = p_sold;
    }

    public boolean isliked() {
        return isliked;
    }

    public void setIsliked(boolean isliked) {
        this.isliked = isliked;
    }

    public String getP_image() {
        return p_image;
    }

    public void setP_image(String p_image) {
        this.p_image = p_image;
    }

    public String getP_star() {
        return p_star;
    }

    public void setP_star(String p_star) {
        this.p_star = p_star;
    }
}
