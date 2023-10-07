package com.dad.springcloud.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author gmq
 * @Date 2023-09-26
 * @Description
 **/
public class HttpUtils {

    public static String request(String strUrl,String requestType) {
        HttpURLConnection connection;//声明连接，初始值为null;
        BufferedReader buffer ;//声明缓存流
        StringBuffer resultBuffer;

        try {
            //新建一个新的url
            URL url = new URL(strUrl);
            //打开连接
            connection = (HttpURLConnection)url.openConnection();
            //哪种请求方式 POST GET....
            connection.setRequestMethod(requestType);
            //设置请求需要返回的数据类型和字符集类型
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");


            //允许写出
            connection.setDoOutput(true);
            //允许读入
            connection.setDoInput(true);
            //不使用缓存
            connection.setUseCaches(false);

            //得到响应码
            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK){
                //得到响应流
                InputStream inputStream = connection.getInputStream();
                //将响应流转换成字符串
                resultBuffer = new StringBuffer();
                String line;
                buffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                buffer.close();

                return resultBuffer.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void main(String[] args) throws InterruptedException {
//        String result = request("http://139.9.35.73:21200/chat/get/online?id=123", "POST");
//        System.out.println(result);

//        post();

        System.out.println(4 ^ 2);
        System.out.println(3 ^ 2);
        System.out.println(3 ^ 3);
        System.out.println(9 ^ 3);
    }

    /**
     * 可以传body值的post请求
     */
    public static void post() {
        try {
            URL url = new URL("http://139.9.35.73:21200/chat/get/online");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.connect();

            String body = "id=123456";
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(body);
            writer.close();

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                StringBuffer resultBuffer = new StringBuffer();
                String line;
                BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while ((line = buffer.readLine()) != null) {
                    resultBuffer.append(line);
                }
                System.out.println("kwwl"+"result============="+resultBuffer.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
