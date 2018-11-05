package com.hxqh.bigdata.domain;

import java.io.Serializable;

/**
 * 消息的实体类：实时报文转成对象
 * @author wulong
 *
 */
public class Message_3001 implements Serializable {

	private static final long serialVersionUID = 1L;

	String trans_code;
	String bnk_nbr;
	String card_nbr;
	String trans_type;
	String or_flg;
	String date_me;
	String amount;
	String amt_sign;
	String curr_num;
	String mb_hone;
	String name;
	String email;
	String ysq_trans_type;
	String sec_resp_code;
	String account_limit;
	String commercial_code;
	String channel_no;
	String cn_amount;
	String pay_amount;
	String mcc;
	String pos_inp;
	String trans_org_code;
	String trans_channel_type_def;
	String c1;
	String c2;
	String revaddress;

	public Message_3001() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getTrans_code() {
		return trans_code;
	}

	public void setTrans_code(String trans_code) {
		this.trans_code = trans_code;
	}

	public String getBnk_nbr() {
		return bnk_nbr;
	}

	public void setBnk_nbr(String bnk_nbr) {
		this.bnk_nbr = bnk_nbr;
	}

	public String getCard_nbr() {
		return card_nbr;
	}

	public void setCard_nbr(String card_nbr) {
		this.card_nbr = card_nbr;
	}

	public String getTrans_type() {
		return trans_type;
	}

	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}

	public String getOr_flg() {
		return or_flg;
	}

	public void setOr_flg(String or_flg) {
		this.or_flg = or_flg;
	}

	public String getDate_me() {
		return date_me;
	}

	public void setDate_me(String date_me) {
		this.date_me = date_me;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getAmt_sign() {
		return amt_sign;
	}

	public void setAmt_sign(String amt_sign) {
		this.amt_sign = amt_sign;
	}

	public String getCurr_num() {
		return curr_num;
	}

	public void setCurr_num(String curr_num) {
		this.curr_num = curr_num;
	}

	public String getMb_hone() {
		return mb_hone;
	}

	public void setMb_hone(String mb_hone) {
		this.mb_hone = mb_hone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getYsq_trans_type() {
		return ysq_trans_type;
	}

	public void setYsq_trans_type(String ysq_trans_type) {
		this.ysq_trans_type = ysq_trans_type;
	}

	public String getSec_resp_code() {
		return sec_resp_code;
	}

	public void setSec_resp_code(String sec_resp_code) {
		this.sec_resp_code = sec_resp_code;
	}

	public String getAccount_limit() {
		return account_limit;
	}

	public void setAccount_limit(String account_limit) {
		this.account_limit = account_limit;
	}

	public String getCommercial_code() {
		return commercial_code;
	}

	public void setCommercial_code(String commercial_code) {
		this.commercial_code = commercial_code;
	}

	public String getChannel_no() {
		return channel_no;
	}

	public void setChannel_no(String channel_no) {
		this.channel_no = channel_no;
	}

	public String getCn_amount() {
		return cn_amount;
	}

	public void setCn_amount(String cn_amount) {
		this.cn_amount = cn_amount;
	}

	public String getPay_amount() {
		return pay_amount;
	}

	public void setPay_amount(String pay_amount) {
		this.pay_amount = pay_amount;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getPos_inp() {
		return pos_inp;
	}

	public void setPos_inp(String pos_inp) {
		this.pos_inp = pos_inp;
	}

	public String getTrans_org_code() {
		return trans_org_code;
	}

	public void setTrans_org_code(String trans_org_code) {
		this.trans_org_code = trans_org_code;
	}

	public String getTrans_channel_type_def() {
		return trans_channel_type_def;
	}

	public void setTrans_channel_type_def(String trans_channel_type_def) {
		this.trans_channel_type_def = trans_channel_type_def;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public String getRevaddress() {
		return revaddress;
	}

	public void setRevaddress(String revaddress) {
		this.revaddress = revaddress;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
