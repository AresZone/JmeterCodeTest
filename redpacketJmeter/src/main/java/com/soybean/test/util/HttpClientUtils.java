package com.soybean.test.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**<br>
 * Description: 封装httpclient4.3及以上版本的网络请求工具类，4.3版本中不再使用DefaultHttpClient<br>
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : Iceberg<br>
 * Date       : 2018年9月17日 上午8:56:57<br>
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号<br>
 * v1.0.0      2018年9月17日             xuejiawei         新增              1001<br>
 ********************************************************************/
public class HttpClientUtils {
    private static final transient Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);
    static {
        SSLUtilities.trustAllHostnames();
        SSLUtilities.trustAllHttpsCertificates();
    }
    public static final String DEFAULT_ENCODE = "utf-8";
    public static final String APPLICATION_JSON = "application/json";

    public static final int DEFAULT_TIMEOUT = 40 * 1000;
    public static final int EXCEPTION_HTTP_STATUSCODE = 9999;
    public static final int CONNECTION_TIMEOUT = 20 * 1000;

    public static final String USER_AGENT = "User-Agent";
    public static final String mobileUserAgent = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16";

    public static HttpResult getMobileUrlAsString(String url) {
        HttpGet httpGet = getHttpGet(url, null, null, DEFAULT_TIMEOUT);
        Map<String, String> reqheader = new HashMap<String, String>();
        reqheader.put(USER_AGENT, mobileUserAgent);
        return executeHttpRequest(httpGet, reqheader, DEFAULT_ENCODE);
    }

    public static HttpResult getUrlAsString(String url) {
        HttpGet httpGet = getHttpGet(url, null, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult getUrlAsString(String url, Map<String, String> params) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult getUrlAsString(String url, Map<String, String> params, Map<String, String> reqHeader) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpGet, reqHeader, DEFAULT_ENCODE);
    }

    public static HttpResult getUrlAsString(String url, Map<String, String> params, boolean isstrict) {
        HttpGet httpGet = getHttpGet(url, params, isstrict, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult getUrlAsString(String url, Map<String, String> params, int timeoutMills) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_ENCODE, timeoutMills);
        HttpResult result = executeHttpRequest(httpGet, null, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult getUrlAsString(String url, Map<String, String> params, BasicClientCookie cookie) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpGet, null, cookie, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult getUrlAsString(String url, Map<String, String> params, String encode) {
        HttpGet httpGet = getHttpGet(url, params, encode, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpGet, null, encode);
        return result;
    }

    public static boolean getUrlAsInputStream(String url, Map<String, String> params, RequestCallback callback) {
        HttpGet httpGet = getHttpGet(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpGet, null, null, callback, DEFAULT_ENCODE);
    }

    public static boolean getUrlAsInputStream(String url, Map<String, String> params, RequestCallback callback,
            String encode) {
        HttpGet httpGet = getHttpGet(url, params, encode, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpGet, null, null, callback, DEFAULT_ENCODE);
    }

    public static HttpResult postUrlAsJson(String url, Map<String, Object> params) {
        HttpPost httpPost = getHttpPostJson(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpPost, null, DEFAULT_ENCODE);
    }

    public static HttpResult postUrlAsString(String url, Map<String, String> params) {
        HttpPost httpPost = getHttpPost(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpPost, null, DEFAULT_ENCODE);
    }

    public static HttpResult postUrlAsString(String url, Map<String, String> params, int timeoutMills) {
        HttpPost httpPost = getHttpPost(url, params, DEFAULT_ENCODE, timeoutMills);
        return executeHttpRequest(httpPost, null, DEFAULT_ENCODE);
    }

    public static HttpResult postUrlAsString(String url, Map<String, String> params, Map<String, String> reqHeader,
            String encode) {
        HttpPost httpPost = getHttpPost(url, params, encode, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpPost, reqHeader, null, encode);
    }

    public static HttpResult postUrlAsString(String url, String body, Map<String, String> reqHeader, String encode) {
        HttpPost httpPost = getHttpPost(url, body, encode, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpPost, reqHeader, null, encode);
    }

    public static boolean postUrlAsInputStream(String url, Map<String, String> params, RequestCallback callback) {
        HttpPost httpPost = getHttpPost(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpPost, null, null, callback, DEFAULT_ENCODE);
    }

    public static HttpResult postBodyAsString(String url, String body) {
        return postBodyAsString2(url, body, "UTF-8");
    }

    public static HttpResult postBodyAsString2(String url, String body, String encode) {
        HttpPost httpPost = getHttpPost(url, body, encode, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpPost, null, encode);
    }

    public static HttpResult postBodyAsString2(String url, String body, String encode, Map<String, String> header) {
        HttpPost httpPost = getHttpPost(url, body, encode, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpPost, header, encode);
    }

    public static HttpResult postBodyAsString(String url, String body, int timeoutMills) {
        return postBodyAsString2(url, body, "UTF-8", timeoutMills);
    }

    public static HttpResult postBodyAsString2(String url, String body, String encode, int timeoutMills) {
        HttpPost httpPost = getHttpPost(url, body, encode, timeoutMills);
        return executeHttpRequest(httpPost, null, encode);
    }

    public static HttpResult deleteUrlAsString(String url, Map<String, String> params) {
        HttpDelete httpDelete = getHttpDelete(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpDelete, null, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult deleteUrlAsString(String url, Map<String, String> params, Map<String, String> reqHeader) {
        HttpDelete httpDelete = getHttpDelete(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        return executeHttpRequest(httpDelete, reqHeader, DEFAULT_ENCODE);
    }

    public static HttpResult deleteUrlAsString(String url, Map<String, String> params, boolean isstrict) {
        HttpDelete httpDelete = getHttpDelete(url, params, isstrict, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpDelete, null, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult deleteUrlAsString(String url, Map<String, String> params, int timeoutMills) {
        HttpDelete httpDelete = getHttpDelete(url, params, DEFAULT_ENCODE, timeoutMills);
        HttpResult result = executeHttpRequest(httpDelete, null, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult deleteUrlAsString(String url, Map<String, String> params, BasicClientCookie cookie) {
        HttpDelete httpDelete = getHttpDelete(url, params, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpDelete, null, cookie, DEFAULT_ENCODE);
        return result;
    }

    public static HttpResult deleteUrlAsString(String url, Map<String, String> params, String encode) {
        HttpDelete httpDelete = getHttpDelete(url, params, encode, DEFAULT_TIMEOUT);
        HttpResult result = executeHttpRequest(httpDelete, null, encode);
        return result;
    }

    public static HttpResult putUrlAsJson(String url, Map<String, Object> params) {
        HttpPut httpPut = getHttpPutJson(url, params, "UTF-8", DEFAULT_TIMEOUT);
        return executeHttpRequest(httpPut, null, "utf-8");
    }

    public static HttpResult uploadFile(String url, Map<String, String> params, InputStream is, String inputName,
            String fileName) {
        Map<String, InputStream> uploadMap = new HashMap<String, InputStream>();
        uploadMap.put(inputName, is);
        Map<String, String> fileNameMap = new HashMap<String, String>();
        fileNameMap.put(inputName, fileName);

        return uploadFile(url, params, uploadMap, fileNameMap, null, DEFAULT_ENCODE, DEFAULT_TIMEOUT);
    }

    public static HttpResult uploadFile(String url, Map<String, String> params, Map<String, InputStream> uploadMap,
            Map<String, String> fileNameMap, Map<String, String> headers, String encode, int timeoutMills) {
        if (StringUtils.isBlank(encode))
            encode = DEFAULT_ENCODE;
        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost request = new HttpPost(url);
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.setCharset(Charset.forName(encode));

        ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                builder.addTextBody(key, params.get(key), contentType);
            }
        }

        for (String input : uploadMap.keySet()) {
            builder.addBinaryBody(fileNameMap.get(input), uploadMap.get(input));
        }

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutMills)
                .setConnectTimeout(CONNECTION_TIMEOUT).setExpectContinueEnabled(false).build();// 设置请求和传输超时时间
        request.setConfig(requestConfig);
        request.setEntity(builder.build());
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), DEFAULT_ENCODE);
                return HttpResult.getSuccessReturn(result);
            } else {
                int statusCode = response.getStatusLine().getStatusCode();
                return HttpResult.getFailure("httpStatus:" + response.getStatusLine().getStatusCode(), statusCode);
            }
        } catch (Exception e) {
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(),
                    EXCEPTION_HTTP_STATUSCODE);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static HttpResult executeHttpRequest(HttpUriRequest request, Map<String, String> reqHeader,
            String charset) {
        return executeHttpRequest(request, reqHeader, null, charset);
    }

    private static HttpResult executeHttpRequest(HttpUriRequest request, Map<String, String> reqHeader,
            BasicClientCookie cookie, String charset) {
        if (StringUtils.isBlank(charset))
            charset = DEFAULT_ENCODE;
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (cookie != null) {
            CookieStore cookieStore = new BasicCookieStore();
            cookieStore.addCookie(cookie);
            builder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient client = builder.build();

        if (reqHeader != null && reqHeader.size() > 0) {
            for (String name : reqHeader.keySet()) {
                request.addHeader(name, reqHeader.get(name));
            }
        }

        try {
            CloseableHttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(response.getEntity(), charset);
                Map<String, Object> responseHeaderMaps = Maps.newHashMap();
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    responseHeaderMaps.put(header.getName(), header.getValue());
                }
                return HttpResult.getSuccessReturn(result, JsonUtil.toJSON(responseHeaderMaps), true);
            } else {
                int statusCode = response.getStatusLine().getStatusCode();
                String msg = "httpStatus:" + statusCode + response.getStatusLine().getReasonPhrase() + ", Header: ";
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    msg += header.getName() + ":" + header.getValue();
                }
                System.out.println("msg:" + msg);
                return HttpResult.getFailure("httpStatus:" + response.getStatusLine().getStatusCode(), statusCode);
            }
        } catch (Exception e) {
            return HttpResult.getFailure(request.getURI() + " exception:" + e.getClass().getCanonicalName(),
                    EXCEPTION_HTTP_STATUSCODE);
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static boolean executeHttpRequest(HttpUriRequest request, Map<String, String> reqHeader,
            BasicClientCookie cookie, RequestCallback callback, String encode) {
        if (StringUtils.isBlank(encode))
            encode = DEFAULT_ENCODE;
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (cookie != null) {
            CookieStore cookieStore = new BasicCookieStore();
            cookieStore.addCookie(cookie);
            builder.setDefaultCookieStore(cookieStore);
        }
        CloseableHttpClient client = builder.build();

        if (reqHeader != null && reqHeader.size() > 0) {
            for (String name : reqHeader.keySet()) {
                request.addHeader(name, reqHeader.get(name));
            }
        }

        try {
            CloseableHttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return callback.processResult(response.getEntity().getContent());
            } else {
                String msg = "httpStatus:" + response.getStatusLine().getStatusCode()
                        + response.getStatusLine().getReasonPhrase() + ", Header: ";
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    msg += header.getName() + ":" + header.getValue();
                }
                logger.error("ERROR HttpUtils:" + msg + request.getURI());
            }
        } catch (Exception e) {
            logger.error(request.getURI() + ":" + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static HttpGet getHttpGet(String url, Map<String, String> params, String encode, int timeoutMills) {
        return getHttpGet(url, params, true, encode, timeoutMills);
    }

    private static HttpGet getHttpGet(String url, Map<String, String> params, boolean isstrict, String encode,
            int timeoutMills) {
        if (params != null) {
            if (url.indexOf('?') == -1)
                url += "?";
            else
                url += "&";
            for (String name : params.keySet()) {
                try {
                    if (!isstrict || StringUtils.isNotBlank(params.get(name)))
                        url += name + "=" + URLEncoder.encode(params.get(name), encode) + "&";
                } catch (UnsupportedEncodingException e) {
                }
            }
            url = url.substring(0, url.length() - 1);
        }
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutMills)
                .setConnectTimeout(CONNECTION_TIMEOUT).setExpectContinueEnabled(false).build();// 设置请求和传输超时时间
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    private static HttpPost getHttpPostJson(String url, Map<String, Object> params, String encoding, int timeoutMills) {
        HttpPost httpPost = new HttpPost(url);
        if (params != null) {
            StringEntity entity;
            try {
                entity = new StringEntity(JsonUtil.toJSON(params), encoding);
                entity.setContentEncoding(encoding);
                entity.setContentType(APPLICATION_JSON);
                httpPost.setEntity(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutMills)
                .setConnectTimeout(CONNECTION_TIMEOUT).setExpectContinueEnabled(false).build();// 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        return httpPost;
    }

    private static HttpPost getHttpPost(String url, String body, String encoding, int timeoutMills) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept-Encoding", "gzip,deflate");
        if (body != null) {
            HttpEntity entity = new StringEntity(body, encoding);
            httpPost.setEntity(entity);
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutMills)
                .setConnectTimeout(CONNECTION_TIMEOUT).setExpectContinueEnabled(false).build();// 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        return httpPost;
    }

    private static HttpPost getHttpPost(String url, Map<String, String> params, String encoding, int timeoutMills) {
        HttpPost httpPost = new HttpPost(url);
        if (params != null) {
            List<NameValuePair> form = new ArrayList<NameValuePair>();
            for (String name : params.keySet()) {
                form.add(new BasicNameValuePair(name, params.get(name)));
            }
            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, encoding);
                httpPost.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutMills)
                .setConnectTimeout(CONNECTION_TIMEOUT).setExpectContinueEnabled(false).build();// 设置请求和传输超时时间
        httpPost.setConfig(requestConfig);
        return httpPost;
    }

    private static HttpDelete getHttpDelete(String url, Map<String, String> params, String encode, int timeoutMills) {
        return getHttpDelete(url, params, true, encode, timeoutMills);
    }

    private static HttpDelete getHttpDelete(String url, Map<String, String> params, boolean isstrict, String encode,
            int timeoutMills) {
        if (params != null) {
            if (url.indexOf('?') == -1)
                url += "?";
            else
                url += "&";
            for (String name : params.keySet()) {
                try {
                    if (!isstrict || StringUtils.isNotBlank(params.get(name)))
                        url += name + "=" + URLEncoder.encode(params.get(name), encode) + "&";
                } catch (UnsupportedEncodingException e) {
                }
            }
            url = url.substring(0, url.length() - 1);
        }
        HttpDelete httpDelete = new HttpDelete(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutMills)
                .setConnectTimeout(CONNECTION_TIMEOUT).setExpectContinueEnabled(false).build();// 设置请求和传输超时时间
        httpDelete.setConfig(requestConfig);
        return httpDelete;
    }

    private static HttpPut getHttpPutJson(String url, Map<String, Object> params, String encoding, int timeoutMills) {
        HttpPut httpPut = new HttpPut(url);
        if (params != null) {
            StringEntity entity;
            try {
                entity = new StringEntity(JsonUtil.toJSON(params), encoding);
                entity.setContentEncoding(encoding);
                entity.setContentType(APPLICATION_JSON);
                httpPut.setEntity(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeoutMills)
                .setConnectTimeout(CONNECTION_TIMEOUT).setExpectContinueEnabled(false).build();// 设置请求和传输超时时间
        httpPut.setConfig(requestConfig);
        return httpPut;
    }

}
