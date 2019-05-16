package com.fushun.springboot.demo.web.enumerate;

public enum EProperties {

    STATIC_PATH_PATTERN("static-path-pattern", "静态资源");

    private String cd;

    private String dscp;

    EProperties(String cd, String dscp) {
        this.cd = cd;
        this.dscp = dscp;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getDscp() {
        return dscp;
    }

    public void setDscp(String dscp) {
        this.dscp = dscp;
    }
}
