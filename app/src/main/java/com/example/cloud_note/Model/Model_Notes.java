package com.example.cloud_note.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Model_Notes {
    @SerializedName("notes")
    private List<Model_List_Note> list;

    public Model_Notes() {
    }

    public List<Model_List_Note> getList() {
        return list;
    }

    public void setList(List<Model_List_Note> list) {
        this.list = list;
    }
}
