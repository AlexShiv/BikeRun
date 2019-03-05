package ru.bacca.bikerun.models;

import java.util.List;

public class Model<T> {

    private List<T> listModel;

    public List<T> getListModel() {
        return listModel;
    }

    public void setListModel(List<T> listModel) {
        this.listModel = listModel;
    }
}
