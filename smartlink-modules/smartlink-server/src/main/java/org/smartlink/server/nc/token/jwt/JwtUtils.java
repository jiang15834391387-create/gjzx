//package org.smartlink.server.nc.token.jwt;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * token生成工具
// */
//public class JwtUtils {
//
//    /**
//     * 有效期，单位是毫秒
//     */
//    private static Long expire = 259200000L;
//
//    /**
//     * 生成JWT令牌
//     */
//    public static String generateJwt(Map<String, Object> claims, String signKey) {
//        String jwt = Jwts.builder()
//                .setClaims(claims)
//                .signWith(SignatureAlgorithm.HS256, signKey)
//                .setExpiration(new Date(System.currentTimeMillis() + expire))
//                .compact();
//        return jwt;
//    }
//
//    /**
//     * 解析JWT令牌
//     */
//    public static Claims parseJWT(String jwt, String signKey) {
//        Claims claims = Jwts.parser()
//                .setSigningKey(signKey)
//                .parseClaimsJws(jwt)
//                .getBody();
//        return claims;
//    }
//
//    // 写入用户信息
//    HashMap<String, Object> tokenMap = new HashMap<>();
//	tokenMap.put("userName",userDB.getUsername());
//	tokenMap.put("id",userDB.getUserId());
//
//    // 生成token
//    String token = JwtUtils.generateJwt(tokenMap,tokenKey);
//
//}
