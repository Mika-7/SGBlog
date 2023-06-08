package com.cdtu.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT 工具类
 */
public class JwtUtil {
    // 有效期为:一天
    public static final Long JWT_TTL = 24 * 60 * 60 * 1000L;
    // 设置秘钥明文
    public static final String JWT_KEY = "sangeng";
    public static String getUUID() {
        String token = UUID.randomUUID().toString();
        return token;
    }

    /**
     * 生成 Jwt
     * @param subject token 中要存放的数据（json 格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    /**
     * 生成 Jwt
     * @param subject token 中要存放的数据（json 格式）
     * @param tllMills token 超时时间
     * @return
     */
    public static String createJWT(String subject, Long tllMills) {
        JwtBuilder builder = getJwtBuilder(subject, tllMills, getUUID());
        return builder.compact();
    }

    /**
     * 生成 Jwt
     * @param id
     * @param subject
     * @param tllMills
     * @return
     */
    public static String createJWT(String id, String subject, Long tllMills) {
        JwtBuilder builder = getJwtBuilder(subject, tllMills, id);
        return builder.compact();
    }
    private static JwtBuilder getJwtBuilder(String subject, Long ttlMills, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        Long nowMills = System.currentTimeMillis();
        Date now = new Date(nowMills);

        if (ttlMills == null) {
            ttlMills = JWT_TTL;
        }

        Long expMills = nowMills + ttlMills;
        Date expDate = new Date(expMills);
        return Jwts.builder()
                .setId(uuid)  // 唯一 ID
                .setSubject(subject) // 主题 可以是 JSON 数据
                .setIssuer("sg") // 签发者
                .setIssuedAt(now) // 签发时间
                .signWith(signatureAlgorithm, secretKey) // 使用 H256 对称加密算法签名，第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 生成加密的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(JWT_KEY);
        SecretKey key = new SecretKeySpec(encodeKey, 0, encodeKey.length, "AES");
        return key;
    }

    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MWRhZDIxYy02NGZkLTQ5NWItODYwOS01NDc4M2VhZmE3OGMiLCJzdWIiOiJ7ZGF0YTogJzIyMzMnfSIsImlzcyI6InNnIiwiaWF0IjoxNjgzOTUzNTU0LCJleHAiOjE2ODQwMzk5NTR9.WYbMJz1yYb-15zrEH_LOXJBfpJvzUhnT1fgMsHMONEg";

        Claims claims = null;
        try {
            claims = parseJWT(token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(claims);
    }

    /**
     * 解析 jwt
     * @param jwt
     * @return
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
