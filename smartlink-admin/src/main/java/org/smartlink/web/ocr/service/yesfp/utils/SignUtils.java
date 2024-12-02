package org.smartlink.web.ocr.service.yesfp.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.compression.CompressionCodecs;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;


/**
 * @author wangweir
 */
public class SignUtils {

    public static String sign(String requestMap,String type,String path,String password) throws Exception {

        // 读取CA证书与PEM格式证书需要根据实际证书使用情况而定,目前这两种都支持
        PrivateKey privateKey;
        if (type.equals("CA")){
            privateKey = loadPrivateKeyOfCA(path,password);
        }else{
            privateKey = loadPrivateKeyOfPem(path);
        }

        Map<String, Object> claims =
                JwtParamBuilder.build().setSubject("tester").setIssuer("einvoice").setAudience("einvoice")
                        .addJwtId().addIssuedAt().getClaims();
        // 需要将表单参数requestdatas的数据进行md5加密，然后放到签名数据的requestdatas中。
        // 此签名数据必须存在，否则在验证签名时会不通过。
        claims.put("requestdatas", getMD5(requestMap));
        // 使用jdk1.6版本时，删除下面代码的中.compressWith(CompressionCodecs.DEFLATE)
        String compactJws = Jwts.builder().signWith(SignatureAlgorithm.RS512, privateKey)
                .setClaims(claims).compressWith(CompressionCodecs.DEFLATE).compact();

        return compactJws;
    }

    /**
     * 读取PEM编码格式
     *
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    protected static PrivateKey loadPrivateKeyOfPem(String path)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PemReader reader = new PemReader(new FileReader(path));
        byte[] privateKeyBytes = reader.readPemObject().getContent();
        reader.close();
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = kf.generatePrivate(spec);
        return privateKey;
    }

    /**
     * 计算MD5
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private static String getMD5(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] buf = null;
        buf = str.getBytes("utf-8");
        MessageDigest md5 = null;
        md5 = MessageDigest.getInstance("MD5");
        md5.update(buf);
        byte[] tmp = md5.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : tmp) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }

    /**
     * 读取证书私钥
     *
     * @return
     * @throws UnrecoverableKeyException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws IOException
     */
    protected static PrivateKey loadPrivateKeyOfCA(String path,String password) throws UnrecoverableKeyException,
            KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        FileInputStream in = new FileInputStream(path);
        KeyStore ks = KeyStore.getInstance("pkcs12");
        ks.load(in, password.toCharArray());
        String alias = ks.aliases().nextElement();
        PrivateKey caprk = (PrivateKey) ks.getKey(alias, password.toCharArray());
        return caprk;
    }

}
