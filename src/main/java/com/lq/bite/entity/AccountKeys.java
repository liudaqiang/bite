package com.lq.bite.entity;
/**
 * 非登录用户 保存key信息
 * @author l.q
 *
 */
public class AccountKeys implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String userName;
	private String publicKey;
	private String privateKey;
	private String isRight;//是否正确0正确1不正确
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	public String getIsRight() {
		return isRight;
	}
	public void setIsRight(String isRight) {
		this.isRight = isRight;
	}
	
	
}
