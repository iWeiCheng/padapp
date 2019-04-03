package com.xjysx.xiaochetong.entities;

import java.util.List;

public class MainBean {

    /**
     * userId :
     * dataList : [{"name":"公司简介","type":"2","bgPicture":"http://www.implus100.com/agent/interface/padImg/1.png","Icon":"","application":"http://www.implus100.com/agent/interface/company.jsp","describe":""},{"name":"车保宝","type":"1","bgPicture":"http://www.implus100.com/agent/interface/padImg/2.png","Icon":"http://www.implus100.com/agent/interface/padImg/svg/车保宝.svg","application":"com.ysx.cxbb","describe":"贴心、快捷、全面的\n车主服务平台"},{"name":"雇主险","type":"1","bgPicture":"http://www.implus100.com/agent/interface/padImg/3.png","Icon":"","application":"com.ysx.gzx","describe":"规避风险首选\n高空作业、工地派遣、\n灵活用工"},{"name":"Im-Plus","type":"1","bgPicture":"http://www.implus100.com/agent/interface/padImg/4.png","Icon":"","application":"Im-plus:com.ysx.xxdl","describe":"集报价、投保、核保、支付全流程的保险行销综合管理系统"},{"name":"小车童","type":"1","bgPicture":"http://www.implus100.com/agent/interface/padImg/5.png","Icon":"http://www.implus100.com/agent/interface/padImg/svg/小车童.svg","application":"com.sjdcar.xctt ","describe":"车后服务智能解决方案 · 技术服务商"},{"name":"微信","type":"1","bgPicture":"http://www.implus100.com/agent/interface/padImg/6.png","Icon":"http://www.implus100.com/agent/interface/padImg/svg/微信.svg","application":"com.tencent.mm"}]
     */

    private String userId;
    private List<DataListBean> dataList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        /**
         * name : 公司简介
         * type : 2
         * bgPicture : http://www.implus100.com/agent/interface/padImg/1.png
         * Icon :
         * application : http://www.implus100.com/agent/interface/company.jsp
         * describe :
         */

        private String name;
        private String type;
        private String bgPicture;
        private String Icon;
        private String application;
        private String describe;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBgPicture() {
            return bgPicture;
        }

        public void setBgPicture(String bgPicture) {
            this.bgPicture = bgPicture;
        }

        public String getIcon() {
            return Icon;
        }

        public void setIcon(String Icon) {
            this.Icon = Icon;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }

        public String getDescribe() {
            return describe;
        }

        public void setDescribe(String describe) {
            this.describe = describe;
        }
    }
}
