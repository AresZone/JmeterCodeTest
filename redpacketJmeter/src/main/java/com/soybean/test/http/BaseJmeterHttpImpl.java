package com.soybean.test.http;/**
 * Created by William on 2018/10/15.
 */


import com.soybean.common.logger.DushuLogger;
import com.soybean.test.Entity.Packet;
import com.soybean.test.Entity.PacketShareUrlVO;
import com.soybean.test.util.HttpClientUtils;
import com.soybean.test.util.HttpResult;
import com.soybean.test.util.JsonUtil;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.threads.JMeterVariables;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <br>
 * Description: BaseJmeterHttpImpl<br>
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : WangLei<br>
 * Date       : 2018/10/15 14:20<br>
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号<br>
 * v1.0.0      2018/10/15             WangLei         新增              1001<br>
 ********************************************************************/
public class BaseJmeterHttpImpl extends AbstractJavaSamplerClient {

    private static String label = "dushu.io";
//    private  String ip ="http://gateway-java-bench.dushu.io";
    private final String ip = "http://10.80.60.181:1111";

    // 测试结果
    private SampleResult sr;


    public final static int THREAD_SIZE = 30;
    private ExecutorService executorService = Executors.newFixedThreadPool(THREAD_SIZE);


    @Override
    public  SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        Packet packet =new Packet();

        JMeterVariables jMeterVariables = javaSamplerContext.getJMeterVariables();
        Iterator<Map.Entry<String, Object>> iterator = jMeterVariables.getIterator();
        while (iterator.hasNext()){
            Map.Entry<String, Object> entry = iterator.next();//把Object型强转成int型
            DushuLogger.info("获取到的参数为："+JsonUtil.toJSONObject(entry));

            if(entry.getKey().equals("senderId")){
                packet.setUserId((String) entry.getValue());
            }
            if(entry.getKey().equals("orderNo")){
                packet.setOrderNo((String) entry.getValue());
            }
            if(entry.getKey().equals("token")){
                packet.setToken(entry.getValue().toString().split("-"));
            }
            if(entry.getKey().equals("getId")){
                packet.setGetterId (entry.getValue().toString().split("-"));
            }

            packet.setAppId("1001");



        }

        boolean flag = true;
        String errorInfo ="";
        SampleResult sr;

        sr = new SampleResult();
        try {
            // 记录程序执行时间以及执行结果
            sr.sampleStart();
            DushuLogger.info("接收到的参数："+JsonUtil.toJSON(packet));
            DushuLogger.info("开始创建红包");
            HttpResult httpResult = HttpClientUtils.postUrlAsJson(ip + "/redPacket-system/redPacket/generateRedPacket", JsonUtil.toJSONObject(packet));
            if (!httpResult.isSuccess()) {
                DushuLogger.error("调用生成红包接口失败");
                errorInfo="调用生成红包接口失败,请求参数为："+JsonUtil.toJSONObject(packet);
                flag=false;
            }
            Map map = JsonUtil.fromJSON(httpResult.getResponse(), Map.class);
            DushuLogger.info("创建红包返回结果："+JsonUtil.toJSON(map));
            if (map.get("data")== null || !"0000".equals(map.get("status") ) ) {
                DushuLogger.error("创建红包失败");
                errorInfo="调用生成红包接口成功，但是处理失败:"+JsonUtil.toJSON(map)+"  ,请求参数为："+JsonUtil.toJSONObject(packet);
                flag =false;
            } else {

                //领红包
                final CountDownLatch latch = new CountDownLatch(THREAD_SIZE);
                for (int i = 0; i < THREAD_SIZE; i++) {
                    PacketShareUrlVO packetShareUrlVO = new PacketShareUrlVO();
                    packetShareUrlVO.setRedPacketId(Long.parseLong(map.get("data").toString()));
                    packetShareUrlVO.setAppId(packet.getAppId());
                    packetShareUrlVO.setToken(packet.getToken()[i]);
                    packetShareUrlVO.setUserId(packet.getGetterId()[i]);
                    DushuLogger.info(JsonUtil.toJSON(packetShareUrlVO));
                    DushuLogger.info(packetShareUrlVO);

                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                                //领红包
                                HttpResult httpResultSmall = HttpClientUtils.postUrlAsJson(ip + "/redPacket-system/redPacket/bindSmallPacketAndUser", JsonUtil.toJSONObject(packetShareUrlVO));
                                if (!httpResultSmall.isSuccess()) {
                                    DushuLogger.error("【领红包失败】调用领红包接口失败："+JsonUtil.toJSONObject(packetShareUrlVO));

                                }
                                Map map = JsonUtil.fromJSON(httpResultSmall.getResponse(), Map.class);
                                if (map.get("status") == null || !"0000".equals(map.get("status"))) {
                                    DushuLogger.error("【领红包失败】调用领红包接口成功，但是处理失败，入参为："+JsonUtil.toJSONObject(packetShareUrlVO)+"处理结果为："+JsonUtil.toJSONObject(map));
                                } else {
                                    //更新会期
                                    Map<String,Object> addVipParamMap = new HashMap<>();
                                    addVipParamMap.put("token",packetShareUrlVO.getToken());
                                    HttpResult addVipTermResult = HttpClientUtils.postUrlAsJson(ip + "/redPacket-system/redPacket/addVipTerm", addVipParamMap);
                                    if (!addVipTermResult.isSuccess()) {
                                        DushuLogger.error("【更新会期失败】调用更新会期接口失败："+JsonUtil.toJSONObject(addVipParamMap));
                                    }
                                    Map addVipMap = JsonUtil.fromJSON(addVipTermResult.getResponse(), Map.class);
                                    if (addVipMap.get("status") == null || !"0000".equals(addVipMap.get("status"))) {
                                        DushuLogger.error("【更新会期失败】调用更新会期接口成功，但是处理失败，入参："+JsonUtil.toJSONObject(addVipParamMap)+"处理结果为："+JsonUtil.toJSONObject(addVipMap));
                                    }else {
                                        DushuLogger.info("更新会期成功");
                                    }
                                }
                            latch.countDown();
                        }
                    });

                }
                latch.await();
            }
            sr.setSuccessful(flag);

        } catch (Exception e) {
            sr.setSuccessful(false);
        } finally {
            sr.sampleEnd();
        }

        //将数据打印到查看结果树当中
        sr.setResponseData(flag?"红包流程执行成功":"红包流程执行失败,原因："+errorInfo, null);
        sr.setDataType(SampleResult.TEXT);
        return sr;
    }


}
