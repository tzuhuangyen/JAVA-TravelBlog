package com.blog.travelblogapp.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//要導入3個 jjwt Library
@Service
//負責產生JWT token的地方！
public class JwtService {


    //用一個變數把這個方法包起，可以在實例化時就產生密鑰，且可以讓驗證時跟生成時的JWT一樣！
    private String secretKey;

    public JwtService(){
        secretKey = generateSecretKey();
    }

    //產生私密鑰的方式！
    public String generateSecretKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");//用這個算法產生！
            SecretKey secretKey = keyGen.generateKey();
            System.out.println("Secret Key : " + secretKey.toString());
            return Base64.getEncoder().encodeToString(secretKey.getEncoded()); //把取得密鑰用base64加密
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    public String generateToken(String username) {

        //key是String, value是Object
        //claim是定義你的Payload，因為有很多資訊，所以用map封裝
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .setClaims(claims) //我們這裡是空的！

                // 下面是jwt庫定義的專門方法，我們也可以在上面map自訂義下面這些，用專門方法好處是確保Token格式符合JWT標準！
                .setSubject(username) //使用者名稱
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(null) //無到期日

                //下面是signature，也就是我們的簽名，來定義我們的真實身分
                //H256是不安全的簽名方式！不推薦
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
                //會自動幫我產生header的 alg(算法類型)
                //使用密鑰(getKey())跟Header和payload，來產生我們的簽名
                //密鑰是最重要的，不能被洩漏出去，因為header跟payload是公開的，否則會被其他人偽造JWT！
    }

    //產生密鑰
    private Key getKey() {
        //密鑰通常不會透過二進位制存儲或傳輸，因為傳輸不安全，且有些二進制數據的字符系統無法打印
        //所以我們先使用Base64編碼把原始的二進制數據轉換成ASCII字符
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // HMAC 通常要求二進位制！
        return Keys.hmacShaKeyFor(keyBytes); //把傳過來的密鑰用HMAC SHA算法來加密密鑰
    }

    public String extractUserName(String token) {
        // extract the username from jwt token
        return extractClaim(token, Claims::getSubject); //取得username！
    } //getSubject是Claims的方法！

    // 定義可以從JWT中獲取特定的Claims
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);// 1️⃣ 先解析 Token，取得所有 Claims
        return claimResolver.apply(claims); // 2️⃣ 使用 lambda 表達式，從 Claims 取出特定的資訊
    }

    //負責驗證JWT，驗證成功返回payload！
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder() //創建JWT解析器
                .setSigningKey(getKey()) // 設置 JWT 解析器的簽名密鑰
                .build()// 建立解析器
                .parseClaimsJws(token)// 讀取Header&payload 並使用上面的密鑰重新計算JWY簽名，並比對傳來的token
                .getBody();// 驗證成功的話，指定返回Token的payload
    }


    // 之前已經驗證過token，這裡簡單檢查username，並且token沒過期！
    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        //不檢查是否過期
        return (userName.equals(userDetails.getUsername()));

//        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //檢查token是否過期了
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //獲取token的到期日
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}