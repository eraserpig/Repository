package com.dad.springcloud.Utils.Ip2;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * @Author gmq
 * @Date 2023-09-26
 * @Description
 **/
/**
 * 获取地址类
 *
 * @author ruoyi
 */

public class Ip2AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(Ip2AddressUtils.class);

    // 未知地址
    public static final String UNKNOWN = "未知地址";

    public static String getRealAddress(String ip) {
        // 内网不查询
        if (internalIp(ip)) {
            return "内网IP";
        }
        try {
            String rspStr = Ip2RegionUtil.getRegion(ip);
            if (StringUtils.isNotEmpty(rspStr)) {
                String[] obj = rspStr.split("\\|");
                String region = obj[2];
                String city = obj[3];

                return String.format("%s%s", region, city);
            }
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", e.toString());
        }
        // ali地域查询
        return Ip2PureNetUtils.getAlibaba(ip);
    }

    public static String getTrueAddress(String ip) {
        // 内网不查询
        if (internalIp(ip)) {
            return "内网IP";
        }
        try {
            String rspStr = Ip2RegionUtil.getRegion(ip);
            if (StringUtils.isNotEmpty(rspStr)) {
                String[] obj = rspStr.split("\\|");
                String country = obj[0];
                String region = obj[2];
                String city = obj[3];

                return String.format("%s%s%s",country, region, city);
            }
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", e.toString());
        }
        // ali地域查询
        return Ip2PureNetUtils.getAlibaba(ip);
    }

    public static String getCounrty(String ip) {
        // 内网不查询
        if (internalIp(ip)) {
            return "内网IP";
        }
        try {
            String rspStr = Ip2RegionUtil.getRegion(ip);
            if (StringUtils.isNotEmpty(rspStr)) {
                String[] obj = rspStr.split("\\|");
                String country = obj[0];
                return String.format("%s",country);
            }
        } catch (Exception e) {
            log.error("获取地理位置异常 {}", e.toString());
        }
        // ali地域查询
        return Ip2PureNetUtils.getAlibaba(ip);
    }


    /* 判断是否是内网IP */
    public static boolean internalIp(String ipAddress) {
        boolean isInnerIp = false;
        long ipNum = getIpNum(ipAddress);
        /*
         * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
         * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
         */
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd)
                || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    /* 获取IP数 */
    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }


    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    public static void main(String[] args) {
       /* String realAddress = getRealAddress("117.136.79.113");
        System.out.println("realAddress = " + realAddress);
        String realAddress1 = Ip2PureNetUtils.getAlibaba("117.136.79.113");
        System.out.println("realAddress1 = " + realAddress1);*/
       List<String> ipList=new ArrayList<>(16);
        ipList.add("117.136.79.113");
        ipList.add("199.36.158.100");
        ipList.add("116.0.0.0");
        ipList.add("185.199.109.133");
        ipList.add("13.226.120.85");
        ipList.add("20.114.189.70");
        ipList.add("220.181.174.102");

        for(String ip:ipList){
            String trueAddress=getTrueAddress(ip);
            log.info("--------trueAddress: {}--------",trueAddress);
            String country=getCounrty(ip);
            log.info("--------country: {}--------",country);
        }



        }

        }


