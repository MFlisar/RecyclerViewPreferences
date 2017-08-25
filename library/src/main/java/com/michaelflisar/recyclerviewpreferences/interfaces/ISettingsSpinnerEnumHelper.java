package com.michaelflisar.recyclerviewpreferences.interfaces;


import java.util.List;

public interface ISettingsSpinnerEnumHelper {

    List<String> getTitles();

    int getListIndex(int listId);

    int getListId(int index);
}
