package com.tle.web.api.hierarchy;

import java.util.*;
public class HierarchyDao {
    String uuid;
    String value;
    List<HierarchyDao> dao;


    public String getUuid() {
        return uuid;
    }

    public String getValue() {
        return value;
    }

    public List<HierarchyDao> getDao() {
        return dao;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDao(List<HierarchyDao> dao) {
        this.dao = dao;
    }

    public HierarchyDao() {

    }

    public HierarchyDao(String uuid, String value, List<HierarchyDao> dao) {
        this.uuid = uuid;
        this.value = value;
        this.dao = dao;
    }
 
}
