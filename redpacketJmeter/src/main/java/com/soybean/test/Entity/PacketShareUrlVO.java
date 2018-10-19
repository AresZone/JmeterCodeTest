package com.soybean.test.Entity;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 *
 * @author : zhang
 *         Date       : 2018/9/25
 *         Modify     : 修改日期          修改人员        修改说明          JIRA编号
 *         v1.0.0       2018/9/25          zhang                       1001
 ********************************************************************/
public class PacketShareUrlVO {

    private String userId;

    private long redPacketId;

    private String token;

    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(long redPacketId) {
        this.redPacketId = redPacketId;
    }
}
