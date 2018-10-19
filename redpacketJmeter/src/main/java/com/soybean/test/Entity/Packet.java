package com.soybean.test.Entity;/**
 * Created by William on 2018/10/15.
 */

/**
 * <br>
 * Description: Packet<br>
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : WangLei<br>
 * Date       : 2018/10/15 14:44<br>
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号<br>
 * v1.0.0      2018/10/15             WangLei         新增              1001<br>
 ********************************************************************/

public class Packet {
    private String userId;
    private String[] getterId;
    private String orderNo;
    private String appId;
    private String[] token;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String[] getGetterId() {
        return getterId;
    }

    public void setGetterId(String[] getterId) {
        this.getterId = getterId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String[] getToken() {
        return token;
    }

    public void setToken(String[] token) {
        this.token = token;
    }
}
