package com.softvilla.m_learning;

import java.util.ArrayList;

/**
 * Created by Malik on 14/10/2017.
 */

public class StateVO {
    private String title;
    public String id;
    private boolean selected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    static ArrayList<String> CheckedData;
}
