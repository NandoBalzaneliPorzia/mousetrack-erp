// src/main/java/com/comexapp/util/GenHash.java
package com.comexapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenHash {
    public static void main(String[] args) {
        var enc = new BCryptPasswordEncoder(10); // mesmo strength padrão
        System.out.println(enc.encode("123456"));
    }
}
