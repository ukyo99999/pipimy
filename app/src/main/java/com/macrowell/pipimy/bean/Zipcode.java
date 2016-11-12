package com.macrowell.pipimy.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Zipcode implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<String> cityList;
    private List<List<Data>> dataList;
    private Map<String, Data> dataMap;
    
    public Zipcode() {
        this.cityList = new ArrayList<String>();
        this.dataList = new ArrayList<List<Data>>();
        this.dataMap = new HashMap<String, Data>();
    }

    public int getCityPosition(Data data) {
        int position = -1;
        for (int i = 0 ; i < this.cityList.size() ; i++) {
            if (this.cityList.get(i).equals(data.getCity())) {
                position = i;
                break;
            }
        }
        
        return position;
    }
    
    public boolean addCity(String city) {
        return cityList.add(city);
    }
    
    public List<String> getCityList() {
        return this.cityList;
    }
    
    public List<Data> getDataList(int location) {
        return this.dataList.get(location);
    }

    public boolean addDataList(List<Data> data) {
        return dataList.add(data);
    }

    public Data putData(String id, Data data) {
        return dataMap.put(id, data);
    }
    
    public Data get(String id) {
        return this.dataMap.get(id);
    }

    public String toString(String id) {
        if (this.dataMap.containsKey(id)) {
            Data data = this.dataMap.get(id);
            
            return data.getZip() + " " + data.getCity() + data.getArea();
        } else {
            return "";
        }
        
    }
    
    public void setUnmodifiable() {
        Collections.unmodifiableList(this.cityList);
        Collections.unmodifiableList(this.dataList);
        Collections.unmodifiableMap(this.dataMap);
    }

    public static class Data implements Serializable {
        private static final long serialVersionUID = 1L;

        private String id;
        private String city;
        private String area;
        private String zip;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

    }

}
