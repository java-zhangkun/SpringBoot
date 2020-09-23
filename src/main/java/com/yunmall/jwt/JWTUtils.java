package com.yunmall.jwt;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JWTUtils {
	private static final String JWT_SECERT="jwt_secert";
	private static final ObjectMapper MAPPER=new ObjectMapper();
	private static final int JWT_ERROR_FAIL=1005;
	private static final int JWT_ERROR_EXPIRE=1006;
	
	public static SecretKey generalKey(){
		try {
			byte[] encodedkey =JWT_SECERT.getBytes("UTF-8");
			SecretKey key=new SecretKeySpec(encodedkey, 0, encodedkey.length, "AES");
			return key;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String createJWT(String id,String iss,String subject,long ttlMills) {
		SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		SecretKey generalKey = generalKey();
		
		JwtBuilder builder=Jwts.builder().setId(id).setIssuer(iss).setSubject(subject).setIssuedAt(now).signWith(signatureAlgorithm, generalKey);
		if(ttlMills>=0) {
			long expMillis=nowMillis+ttlMills;
			Date expDate = new Date(expMillis);
			builder.setExpiration(expDate);
		}
		return builder.compact();
	}
	
	public static JWTResult validateJWT(String jwtStr) {
		JWTResult checkResult=new JWTResult();
		Claims claims=null;
		try {
			claims=parseJWT(jwtStr);
			checkResult.setSuccess(true);
			checkResult.setClaims(claims);
		} catch (ExpiredJwtException e) {
			checkResult.setSuccess(false);
			checkResult.setErrCode(JWT_ERROR_EXPIRE);
		}catch (SignatureException e) {
			checkResult.setSuccess(false);
			checkResult.setErrCode(JWT_ERROR_FAIL);
		}catch (Exception e) {
			checkResult.setSuccess(false);
			checkResult.setErrCode(JWT_ERROR_FAIL);
		}
		return checkResult;
	}

	public static Claims parseJWT(String jwtStr) {
		SecretKey generalKey = generalKey();
		return Jwts.parser().setSigningKey(generalKey).parseClaimsJws(jwtStr).getBody();
	}
	
	public static String generalSubject(Object subobj) {
		try {
			return MAPPER.writeValueAsString(subobj);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
