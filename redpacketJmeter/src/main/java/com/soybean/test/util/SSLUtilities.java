package com.soybean.test.util;

import javax.net.ssl.*;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**<br>
 * Description: 这里用一句话描述这个类的作用<br>
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : Iceberg<br>
 * Date       : 2018年9月17日 上午8:53:06<br>
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号<br>
 * v1.0.0      2018年9月17日             xuejiawei         新增              1001<br>
 ********************************************************************/
public final class SSLUtilities {
    private static HostnameVerifier _hostnameVerifier;
    private static TrustManager[] _trustManagers;

    public static void trustAllHostnames() {
        if (_hostnameVerifier == null) {
            _hostnameVerifier = new FakeHostnameVerifier();
        }
        HttpsURLConnection.setDefaultHostnameVerifier(_hostnameVerifier);

    }

    public static void trustAllHttpsCertificates() {
        SSLContext context;
        if (_trustManagers == null) {
            _trustManagers = new TrustManager[] { new FakeX509TrustManager() };
        }
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, _trustManagers, new SecureRandom());
        } catch (GeneralSecurityException gse) {
            throw new IllegalStateException(gse.getMessage());
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public static class FakeHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, javax.net.ssl.SSLSession session) {
            return (true);
        }
    }

    public static class FakeX509TrustManager implements X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }

    public static boolean isDeprecatedSSLProtocol() {
        return ("com.sun.net.ssl.internal.www.protocol".equals(System.getProperty("java.protocol.handler.pkgs")));
    }
}
