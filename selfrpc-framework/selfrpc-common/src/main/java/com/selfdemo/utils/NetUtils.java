package com.selfdemo.utils;

import com.selfdemo.exception.NetworkException;
import lombok.extern.slf4j.Slf4j;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Date:2023/8/11
 * Author:ljs
 * Description:
 */
@Slf4j
public class NetUtils {

    public static String getIp() {

        try {
            // 获取所有的网卡信息
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements()) {
                NetworkInterface i_face = interfaces.nextElement();
                //过滤非回环接口
                if(i_face.isLoopback() || i_face.isVirtual() || !i_face.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = i_face.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    //过滤IPv6地址和回环地址
                    if(addr instanceof Inet6Address || addr.isLoopbackAddress()) {
                        continue;
                    }
                    String ipAddress = addr.getHostAddress();
                    System.out.println("局域网IP地址:" + ipAddress);
                    return ipAddress;
                }
            }
            throw new NetworkException();
        } catch (SocketException e) {
            log.error("获取局域网ip时发生异常:",e);
            throw new NetworkException(e);
        }
    }

    public static void main(String[] args) {
        NetUtils.getIp();

    }
}
