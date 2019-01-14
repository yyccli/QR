package com.example.hasee.express.express.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListItemMessageLab {
    private static ListItemMessageLab sListItemMessageLab;

    private List<ListItemMessage> mList;

    public ListItemMessageLab() {
        mList = new ArrayList<>();

    }

    public static ListItemMessageLab getListItemMessageLab() {
        if (null == sListItemMessageLab) {
            sListItemMessageLab = new ListItemMessageLab();
        }
        return sListItemMessageLab;
    }

    public void clear() {
        mList.clear();
    }

    public void add(String receiver, String sender) {
        mList.add(new ListItemMessage(receiver, sender));
    }

    public List<ListItemMessage> getList() {
        return mList;
    }

}
