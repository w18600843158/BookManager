package com.example.bookManager.util;

import java.util.UUID;

/**
 * Cookie都是一串无意义的码串，用JDK自带的UUID生成器可以非常方便的生成这样一串随机的字符串。
 */
public class UuidUtils {

  public static String next(){
    return UUID.randomUUID().toString().replace("-","a");
  }

}
