package com.lianlian.utils.io.jwt;

public class JwtClaims {
	/** 标准的注册声明  */
	private String iss;//发行者
	private String sub;//token
	private String aud;//接收jwt的一方
	private long exp = 30 * 30 * 1000;//过期时间 毫秒 
	private long nbf;//在nbf时间之前，jwt是不可以使用的。
	private long iat;//jwt的签发时间
	private String jti;//jwt的标识 sessionId
	private String audience;//don't know
	
	/** 公共声明部分 */
	private Object data;//其他信息
	
	public String getIss() {
		return iss;
	}
	public void setIss(String iss) {
		this.iss = iss;
	}
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getAud() {
		return aud;
	}
	public void setAud(String aud) {
		this.aud = aud;
	}
	public long getExp() {
		return exp;
	}
	
	/**
	 * @param exp 毫秒
	 */
	public void setExp(long exp) {
		this.exp = exp;
	}
	public long getNbf() {
		return nbf;
	}
	
	/**
	 * 在nbf时间之前，jwt是不可以使用的。
	 * @param nbf 毫秒
	 */
	public void setNbf(long nbf) {
		this.nbf = nbf;
	}
	public long getIat() {
		return iat;
	}
	
	/**
	 * jwt的签发时间
	 * @param iat 毫秒
	 */
	public void setIat(long iat) {
		this.iat = iat;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public String getJti() {
		return jti;
	}
	public void setJti(String jti) {
		this.jti = jti;
	}
	public JwtClaims(String jti) {
		super();
		this.jti = jti;
	}
	public String getAudience() {
		return audience;
	}
	public void setAudience(String audience) {
		this.audience = audience;
	}
	
}

