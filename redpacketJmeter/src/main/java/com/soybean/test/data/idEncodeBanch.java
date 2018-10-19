package com.soybean.test.data;/**
 * Created by William on 2018/10/17.
 */

import com.soybean.test.util.IntegerEncryptTool;

import java.io.*;

/**
 * <br>
 * Description: idEncodeBanch<br>
 * Company    : 上海黄豆网络科技有限公司 <br>
 * Author     : WangLei<br>
 * Date       : 2018/10/17 10:45<br>
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号<br>
 * v1.0.0      2018/10/17             WangLei         新增              1001<br>
 ********************************************************************/
public class idEncodeBanch {

    /**
     * 读取未加密的id，对id加密后再输出到txt文件中
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws IOException {
         /* 写入Txt文件 */
        File writename = new File("D:\\data\\orderNo.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
        writename.createNewFile(); // 创建新文件
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));
          /* 读入TXT文件 */
        String pathname = "D:\\data\\所有可以用的id.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
        File filename = new File(pathname); // 要读取以上路径的input。txt文件
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
        BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
        String line = "";

        while (line != null) {
            line = br.readLine(); // 一次读入一行数据


            //加密后输出
            out.write(IntegerEncryptTool.Encrypt(Integer.parseInt(line))+"\r\n");
//            if (line != null){
//                // 加orderNo最大值后输入
//                out.write((Integer.parseInt(line)+901876)+"\r\n");
//            }

            out.flush(); // 把缓存区内容压入文件
        }
        br.close();
        out.close(); // 最后记得关闭文件
    }
}
