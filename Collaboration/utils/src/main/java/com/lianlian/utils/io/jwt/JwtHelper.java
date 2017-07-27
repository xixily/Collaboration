package com.lianlian.utils.io.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import com.lianlian.utils.system.ResourcesUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


/**
 * jwt tools
 * @author dengxf
 *
 */
public class JwtHelper {
	
	public static final long JWT_EXP = 30 * 60 * 1000;//token默认失效时间为30分钟
	public static final String CAS_KEY = "admin_cas";
	
	/**
	 * 解析jwt
	 * @param jsonWebToken
	 * @param base64security
	 * @return
	 */
	public static Claims parseJWT(String jsonWebToken, String base64security) {
		try {
			Claims claims = Jwts.parser().
					setSigningKey(DatatypeConverter.parseBase64Binary(base64security))
					.parseClaimsJws(jsonWebToken.trim()).getBody();
//			if(null != claims.get("data")) {//用DES 加密算法，把data加密一下。
//				claims.put("data", DES.decrypt(claims.get("data").toString().getBytes(), "admin_cas"));
//			}
			return claims;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取data值
	 * @param jsonWebToken
	 * @param base64security
	 * @return
	 */
	public static JwtClaims getData(String jsonWebToken, String base64security) {
		Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64security))
		.isSigned(jsonWebToken);
		Claims claims = parseJWT(jsonWebToken, base64security);
		JwtClaims jwtClaims = null;
		if(null != claims) {
			jwtClaims = (JwtClaims) claims.get("data");
		}
		return jwtClaims;
	}
	
	/**
		Returns true if the specified JWT compact string represents a signed JWT (aka a 'JWS'), false otherwise. 
	 * @param jsonWebToken
	 * @param base64security
	 * @return
	 */
	public static boolean isSigned(String jsonWebToken, String base64security) {
		return Jwts.parser().
		setSigningKey(DatatypeConverter.parseBase64Binary(base64security))
		.isSigned(jsonWebToken);
	}
	
	/**
	 * 验证token有效性
	 * @param token
	 * @param base64security
	 * @return
	 */
	public static boolean isValidate(String token, String base64security) {
		try {
			Jws<Claims> jwtClaims = Jwts.parser().setSigningKey(base64security).parseClaimsJws(token.trim());
			Long exp = (Long) jwtClaims.getBody().get(Claims.EXPIRATION);
			return exp - System.currentTimeMillis() > 0;
		}catch(ExpiredJwtException e) {
			return false;
		}catch(Exception e2) {
			return false;
		}
	}
	
	/**
	 * 生成token
	 * @param jwtClaims token 的声明部分
	 * @param name
	 * @param userId
	 * @param role
	 * @param audience
	 * @param issuer
	 * @param TTLMillis
	 * @param base64Security 密钥
	 * @return
	 */
	public static String createJWT(JwtClaims jwtClaims, String name, String userId, String role, String audience, String issuer, long TTLMillis, String base64Security) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;//加密算法 HS256
		long nowMillis = System.currentTimeMillis();//创建token的时间
		Date now = new Date(nowMillis);
		
		//生成签名密钥
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName()); 
		
		//添加构成JWT的参数
		 JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
				 .claim("role", role)
				 .claim("unique_name", name)
				 .claim("userid", userId)
				 .claim("data", new JwtClaims("1234"))
				 .setIssuer(issuer)
				 .setAudience(audience)
				 .signWith(signatureAlgorithm, signingKey);
		 
		 //添加Token过期时间
		 if (TTLMillis >= 0) {
			 long expMillis = nowMillis + TTLMillis;
			 Date exp = new Date(expMillis);
			 builder.setExpiration(exp).setNotBefore(now);
		 }
		 
		 //生成JWT
		 return builder.compact();
	}
	
	public static String createJWT(JwtClaims jwtClaims, String base64Security) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;//加密算法 HS256
		long nowMillis = System.currentTimeMillis();//创建token的时间
		Date now = new Date(nowMillis);
		
		//生成签名密钥
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName()); 
		
		//添加构成JWT的参数
		JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
				.claim("data", jwtClaims)
				.setIssuer(jwtClaims.getIss())
				.setIssuedAt(now)//开始时间
				.setExpiration(new Date(nowMillis + jwtClaims.getExp())) //过期时间
				.setAudience(jwtClaims.getAudience())
				.signWith(signatureAlgorithm, signingKey);
		
		//添加Token过期时间
		long TTLMillis = jwtClaims.getExp()<0 ? JwtHelper.JWT_EXP : jwtClaims.getExp();
		long expMillis = nowMillis + TTLMillis;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp).setNotBefore(now);
		
		//生成JWT
		return builder.compact();
	}
	
	public static void main(String[] args) {
//		String jwt = createJWT(new JwtClaims("1234"), "user", "dengxf", "3", "213", "localhost:8080/cas", 30 * 60 * 1000, "admin_cas");
		JwtClaims jtc = new JwtClaims("1234");
		jtc.setExp(10000);
		String jwt = createJWT(jtc, ResourcesUtil.getBase64SecreateKey());
//		String jwt = createJWT(new JwtClaims("12345"), CAS_KEY);
//		Claims claims = parseJWT("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjp7ImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAvIiwic3ViIjpudWxsLCJhdWQiOm51bGwsImV4cCI6OTAwMDAwLCJuYmYiOjAsImlhdCI6MCwianRpIjoia2NvczRpb3NnbDhqMWRodzlzcGFydnBrdyIsImF1ZGllbmNlIjpudWxsLCJkYXRhIjp7ImNvZGUiOjAsIm1lc3NhZ2UiOiLmk43kvZzmiJDlip_vvIEiLCJzeXN0ZW1EYXRlIjoxNTAwODc5MDUxNTg2LCJkYXRhIjp7ImlkIjpudWxsLCJzdGF0dXMiOm51bGwsImNyZWF0b3IiOm51bGwsIm1vZGlmaWVyIjpudWxsLCJnbXRDcmVhdGUiOm51bGwsImdtdE1vZGlmaWVkIjpudWxsLCJsb2dpbklkIjoiZGVuZ3hmIiwibmFtZSI6IumCk-mbquWzsCIsIm1vYmlsZSI6bnVsbCwiZW1haWwiOiJkZW5neGZAeWludG9uZy5jb20uY24iLCJkZXB0TmFtZSI6IkNOPemCk-mbquWzsCxPVT3mioDmnK_kuK3lv4MsT1U96L-e6L-e5pSv5LuYLGRjPWxpYW5saWFucGF5LGRjPWNvbSIsImdtdExvZ2luIjpudWxsLCJsZGFwRGVzY24iOm51bGx9LCJzdWNjZXNzIjp0cnVlfX0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAvIiwiZXhwIjoxNTAwODc5OTU1LCJuYmYiOjE1MDA4NzkwNTV9.NPg6csTJVDCamFI2FHnRId65BncGHtl1nuoaAoQ73AY", "admin_cas");
		Claims claims = parseJWT(jwt, ResourcesUtil.getBase64SecreateKey());
//		Object obj = claims.get("data");
		System.out.println(claims);
		
		System.out.println(isValidate(jwt, ResourcesUtil.getBase64SecreateKey()));
		
		System.out.println(isSigned(jwt, CAS_KEY));
//		JwtClaims jc = new JwtClaims("");
		
//		System.out.println("");
//		AccessToken accessTokenEntity = new AccessToken();  
	}
}
