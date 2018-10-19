package com.soybean.test.util;/**
 * Created by William on 2018/10/15.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.soybean.common.utils.basic.JsonSerializeFilter;
import com.soybean.common.utils.basic.StringUtil;

import java.util.Date;

/**
 * <br>
 * Description: JsonUtil<br>
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : WangLei<br>
 * Date       : 2018/10/15 15:18<br>
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号<br>
 * v1.0.0      2018/10/15             WangLei         新增              1001<br>
 ********************************************************************/
public class JsonUtil {
    private static final SerializeFilter SERIALIZEFILTER = new JsonSerializeFilter();

    public JsonUtil() {
    }

    public static void main(String[] args) {
        System.out.println(toJSON(new Date()));
    }

    public static final String toJSON(Object obj) {
        return JSON.toJSONString(obj, SERIALIZEFILTER, new SerializerFeature[]{SerializerFeature.IgnoreNonFieldGetter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.IgnoreErrorGetter, SerializerFeature.NotWriteDefaultValue, SerializerFeature.DisableCircularReferenceDetect});
    }

    public static final <T> T fromJSON(String json, Class<T> clz) {
        return JSON.parseObject(json, clz, new Feature[]{Feature.OrderedField});

    }

    public static final JSONObject toJSONObject(Object json) {
        return StringUtil.isNullOrEmpty(json)?null:(json instanceof String?JSON.parseObject(json.toString()):JSON.parseObject(toJSON(json)));
    }

    public static final String getPrettyJSON(String json) {
        return JSON.toJSONString(JSON.parse(json), true);
    }

    public static final String mkJsonp(String func, String json) {
        return func + "(" + json + ")";
    }
}
