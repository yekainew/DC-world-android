package com.jkkg.hhtx.net.bean;

import java.util.List;

public class MethodTransferConfBean {


    /**
     * code : 200
     * data : {"date":[{"transfer_conf_max_num":"500","transfer_conf_status":0,"transfer_conf_fee_scale":"0.003","transfer_conf_id":1,"transfer_conf_create_time":"2020-09-28 15:55:17","bc_name":"TRX","transfer_conf_min_num":"10"},{"transfer_conf_max_num":"500","transfer_conf_status":0,"transfer_conf_fee_scale":"0.003","transfer_conf_id":2,"transfer_conf_create_time":"2020-09-28 15:55:29","bc_name":"USDT","transfer_conf_min_num":"10"},{"transfer_conf_max_num":"500","transfer_conf_status":0,"transfer_conf_fee_scale":"0.003","transfer_conf_id":3,"transfer_conf_create_time":"2020-09-29 17:22:30","bc_name":"DC","transfer_conf_min_num":"10"}],"extra":"操作成功","type":200}
     * session : a1c3c0e2-eaf6-4cc1-9aa2-001b3c0b5325
     * type : 0
     * from_host : 119.147.145.236
     * func : bc.method.transferConf
     * port : 0
     * host : 113.98.201.156
     * from_port : 0
     * hash : c4ee6f4bd206eb6a65225ab4b3496b96
     * server_type : GE_MAIN
     * timestamp : 1602408027597
     */

    private int code;
    private DataBean data;
    private String session;
    private int type;
    private String from_host;
    private String func;
    private int port;
    private String host;
    private int from_port;
    private String hash;
    private String server_type;
    private String timestamp;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFrom_host() {
        return from_host;
    }

    public void setFrom_host(String from_host) {
        this.from_host = from_host;
    }

    public String getFunc() {
        return func;
    }

    public void setFunc(String func) {
        this.func = func;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getFrom_port() {
        return from_port;
    }

    public void setFrom_port(int from_port) {
        this.from_port = from_port;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getServer_type() {
        return server_type;
    }

    public void setServer_type(String server_type) {
        this.server_type = server_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataBean {
        /**
         * date : [{"transfer_conf_max_num":"500","transfer_conf_status":0,"transfer_conf_fee_scale":"0.003","transfer_conf_id":1,"transfer_conf_create_time":"2020-09-28 15:55:17","bc_name":"TRX","transfer_conf_min_num":"10"},{"transfer_conf_max_num":"500","transfer_conf_status":0,"transfer_conf_fee_scale":"0.003","transfer_conf_id":2,"transfer_conf_create_time":"2020-09-28 15:55:29","bc_name":"USDT","transfer_conf_min_num":"10"},{"transfer_conf_max_num":"500","transfer_conf_status":0,"transfer_conf_fee_scale":"0.003","transfer_conf_id":3,"transfer_conf_create_time":"2020-09-29 17:22:30","bc_name":"DC","transfer_conf_min_num":"10"}]
         * extra : 操作成功
         * type : 200
         */

        private String extra;
        private int type;
        private List<DateBean> date;

        public String getExtra() {
            return extra;
        }

        public void setExtra(String extra) {
            this.extra = extra;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<DateBean> getDate() {
            return date;
        }

        public void setDate(List<DateBean> date) {
            this.date = date;
        }

        public static class DateBean {
            /**
             * transfer_conf_max_num : 500
             * transfer_conf_status : 0
             * transfer_conf_fee_scale : 0.003
             * transfer_conf_id : 1
             * transfer_conf_create_time : 2020-09-28 15:55:17
             * bc_name : TRX
             * transfer_conf_min_num : 10
             */

            private String transfer_conf_max_num;
            private int transfer_conf_status;
            private String transfer_conf_fee_scale;
            private int transfer_conf_id;
            private String transfer_conf_create_time;
            private String bc_name;
            private String transfer_conf_min_num;

            public String getTransfer_conf_max_num() {
                return transfer_conf_max_num;
            }

            public void setTransfer_conf_max_num(String transfer_conf_max_num) {
                this.transfer_conf_max_num = transfer_conf_max_num;
            }

            public int getTransfer_conf_status() {
                return transfer_conf_status;
            }

            public void setTransfer_conf_status(int transfer_conf_status) {
                this.transfer_conf_status = transfer_conf_status;
            }

            public String getTransfer_conf_fee_scale() {
                return transfer_conf_fee_scale;
            }

            public void setTransfer_conf_fee_scale(String transfer_conf_fee_scale) {
                this.transfer_conf_fee_scale = transfer_conf_fee_scale;
            }

            public int getTransfer_conf_id() {
                return transfer_conf_id;
            }

            public void setTransfer_conf_id(int transfer_conf_id) {
                this.transfer_conf_id = transfer_conf_id;
            }

            public String getTransfer_conf_create_time() {
                return transfer_conf_create_time;
            }

            public void setTransfer_conf_create_time(String transfer_conf_create_time) {
                this.transfer_conf_create_time = transfer_conf_create_time;
            }

            public String getBc_name() {
                return bc_name;
            }

            public void setBc_name(String bc_name) {
                this.bc_name = bc_name;
            }

            public String getTransfer_conf_min_num() {
                return transfer_conf_min_num;
            }

            public void setTransfer_conf_min_num(String transfer_conf_min_num) {
                this.transfer_conf_min_num = transfer_conf_min_num;
            }
        }
    }
}
