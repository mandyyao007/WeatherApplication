package com.example.weatherapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/*

{"collectorName":"滨江森林公园基准","collectorPort":"","id":"54","latitude":"31.3888997","longitude":"121.53680877"}
 */
public class CollectorItemBean {
    @SerializedName("collectorName")
    private String collectorName;
    @SerializedName("collectorPort")
    private String collectorPort;
    @SerializedName("id")
    private String id;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorPort(String collectorPort) {
        this.collectorPort = collectorPort;
    }

    public String getCollectorPort() {
        return collectorPort;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "StationItemBean{" +
                "collectorName='" + collectorName + '\'' +
                ", collectorPort='" + collectorPort + '\'' +
                ", id='" + id + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    public static class TreeDataBean {
        @SerializedName("total")
        private int total;
        @SerializedName("rows")
        private List<TreeDataItemBean> mTreeDataItemBeans;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<TreeDataItemBean> getmTreeDataItemBeans() {
            return mTreeDataItemBeans;
        }

        public void setmTreeDataItemBeans(List<TreeDataItemBean> mTreeDataItemBeans) {
            this.mTreeDataItemBeans = mTreeDataItemBeans;
        }

        @Override
        public String toString() {
            return "TreeDataBean{" +
                    "total=" + total +
                    ", mTreeDataItemBeans=" + mTreeDataItemBeans +
                    '}';
        }
    }
}
