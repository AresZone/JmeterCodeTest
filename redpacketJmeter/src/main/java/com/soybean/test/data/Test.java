package com.soybean.test.data;/**
 * Created by William on 2018/10/18.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <br>
 * Description: Test<br>
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : WangLei<br>
 * Date       : 2018/10/18 9:15<br>
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号<br>
 * v1.0.0      2018/10/18             WangLei         新增              1001<br>
 ********************************************************************/
public class Test {

    public static void main(String[] args) throws IOException {
           /* 写入Txt文件 */
        File writename = new File("D:\\data\\tcc使用数据.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
        for (int i = 1; i <= 10000; i++) {
            out.write(i+"\r\n");
        }
        out.close();
    }
}
