package com.example.feng.version1.bean;

public class DeviceCreateResponse {

    /**
     * data : {}
     * status : 1200
     * statusInfo : {"message":"添加成功","detail":"OK"}
     */

    private DataBean data;
    private int status;
    private StatusInfoBean statusinfo;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public StatusInfoBean getStatusinfo() {
        return statusinfo;
    }

    public void setStatusinfo(StatusInfoBean statusinfo) {
        this.statusinfo = statusinfo;
    }

    public static class DataBean {
    }

    public static class StatusInfoBean {
        /**
         * message : 添加成功
         * detail : OK
         */

        private String message;
        private String detail;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }
}
