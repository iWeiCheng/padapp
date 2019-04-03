package com.xjysx.xiaochetong.network;



public class BaseBean<T> {
        private int resultCode;
        private String resultDesc;
        private T data;

        public BaseBean(int resultCode, String resultDesc, T data) {
            this.resultCode = resultCode;
            this.resultDesc = resultDesc;
            this.data = data;
        }

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public String getResultDesc() {
            return resultDesc;
        }

        public void setResultDesc(String resultDesc) {
            this.resultDesc = resultDesc;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }


}
