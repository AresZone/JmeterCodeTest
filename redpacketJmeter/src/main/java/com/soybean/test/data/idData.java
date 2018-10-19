package com.soybean.test.data;/**
 * Created by William on 2018/10/17.
 */

import java.io.*;

/**
 * <br>
 * Description: TokenData<br>
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : WangLei<br>
 * Date       : 2018/10/17 10:41<br>
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号<br>
 * v1.0.0      2018/10/17             WangLei         新增              1001<br>
 ********************************************************************/
public class idData {

    /**
     * 读取id，整理id为id1-id2-. .-id15的状态
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
          /* 写入Txt文件 */
        File writename = new File("D:\\data\\排版后的加密id20181018.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
          /* 读入TXT文件 */
        String pathname = "D:\\data\\加密后的id.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
        File filename = new File(pathname); // 要读取以上路径的input。txt文件
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = "";
        String a ="";
        int count =1;
        while (line != null) {
            line = br.readLine(); // 一次读入一行数据
            if (count<=30){
                if("".equals(a)){
                    a = line;
                }else {
                    a= a + "-"+line;
                }
                count ++;
            }
            if (count == 31){
                //加密
                System.out.println(a);
                out.write(a+"\r\n"); // \r\n即为换行
                a="";
            }
            if (count>30){
                count =1;
            }
        }
        br.close();

        out.close(); // 最后记得关闭文件
    }
}
