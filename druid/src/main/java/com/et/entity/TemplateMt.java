package com.et.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 模板下行消息请求
 */
@Entity
@Table(name="TT_MT")
public class TemplateMt {
  @Id
  @Column(name = "SP_MT_ID")
  protected Long spMtId;

  /**
   * SP编号
   */
  @Column(name = "SP_ID")
  protected String spId;

  /**
   * SP服务代码
   */
  @Column(name = "SP_SERVICE_CODE")
  protected String spSc;

  /**
   * 消息发送方
   */
  @Column(name = "FROM_USER")
  protected String fromUser;

  /**
   * 消息接收方
   */
  @Column(name = "TO_USER")
  protected String toUser;

  /**
   * 模板Id
   */
  @Column(name = "TEMPLATE_ID")
  protected String templateId;

  /**
   * 模板发送消息Id
   */
  @Column(name = "MSG_ID")
  protected String msgId;

  /**
   * 消息发送到qq的状态
   */
  @Column(name = "MSG_STATUS")
  private String msgStatus;
  
  /**
   * qq将消息发送到粉丝的状态
   */
  @Column(name = "MSG_STATUS1")
  private String msgStatus1;
  
  /**
   * 粉丝是否查看信息
   */
  @Column(name = "MSG_STATUS2")
  private String msgStatus2;
  
  @Column(name = "SP_MT_TIME")
  protected Date spMtTime;
  
  @Column(name = "CARRIER_MT_TIME")
  protected Date carrierMtTime;

  @Column(name = "CARRIER_RT_TIME")
  protected Date carrierRtTime;

  @Column(name = "SP_RT_TIME")
  protected Date spRtTime;

  /**
   * 省代码
   */
  @Column(name = "PROVINCE_CODE")
  protected String provinceCode;

  /**
   * 市代码
   */
  @Column(name = "CITY_CODE")
  protected String cityCode;

  public TemplateMt() {
  }
  
  public Long getSpMtId() {
    return spMtId;
  }

  public void setSpMtId(Long spMtId) {
    this.spMtId = spMtId;
  }

  public String getSpId() {
    return spId;
  }

  public void setSpId(String spId) {
    this.spId = spId;
  }

  public String getSpSc() {
    return spSc;
  }

  public void setSpSc(String spSc) {
    this.spSc = spSc;
  }

  public String getFromUser() {
    return fromUser;
  }

  public void setFromUser(String fromUser) {
    this.fromUser = fromUser;
  }

  public String getToUser() {
    return toUser;
  }

  public void setToUser(String toUser) {
    this.toUser = toUser;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public String getMsgStatus() {
    return msgStatus;
  }

  public void setMsgStatus(String msgStatus) {
    this.msgStatus = msgStatus;
  }

  public String getMsgStatus1() {
    return msgStatus1;
  }

  public void setMsgStatus1(String msgStatus1) {
    this.msgStatus1 = msgStatus1;
  }

  public String getMsgStatus2() {
    return msgStatus2;
  }

  public void setMsgStatus2(String msgStatus2) {
    this.msgStatus2 = msgStatus2;
  }

  public Date getSpMtTime() {
    return spMtTime;
  }

  public void setSpMtTime(Date spMtTime) {
    this.spMtTime = spMtTime;
  }

  public Date getCarrierMtTime() {
    return carrierMtTime;
  }

  public void setCarrierMtTime(Date carrierMtTime) {
    this.carrierMtTime = carrierMtTime;
  }

  public Date getCarrierRtTime() {
    return carrierRtTime;
  }

  public void setCarrierRtTime(Date carrierRtTime) {
    this.carrierRtTime = carrierRtTime;
  }

  public Date getSpRtTime() {
    return spRtTime;
  }

  public void setSpRtTime(Date spRtTime) {
    this.spRtTime = spRtTime;
  }

  public String getProvinceCode() {
    return provinceCode;
  }

  public void setProvinceCode(String provinceCode) {
    this.provinceCode = provinceCode;
  }

  public String getCityCode() {
    return cityCode;
  }

  public void setCityCode(String cityCode) {
    this.cityCode = cityCode;
  }

}