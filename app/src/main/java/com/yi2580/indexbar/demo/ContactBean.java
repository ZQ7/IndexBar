package com.yi2580.indexbar.demo;

import com.yi2580.indexbar.IBaseIndex;

public class ContactBean implements IBaseIndex {
    private String indexTag;
    private String name;

    public String getIndexTag() {
        return indexTag;
    }

    public void setIndexTag(String indexTag) {
        this.indexTag = indexTag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getBaseIndexTag() {
        return getIndexTag();
    }
}
