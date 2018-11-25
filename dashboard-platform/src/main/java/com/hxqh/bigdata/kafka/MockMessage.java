package com.hxqh.bigdata.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import scala.util.Random;

public class MockMessage {

	public static String mockMessage() {
		
		//报文类型  3001  3003
		List<String> sendTypes = new ArrayList<>();
		sendTypes.add("3001");
		sendTypes.add("3003");
		
		//银行代号
		String xy_code = "0309";
		
		//银行卡号
		String card_code_pre = "451289249";
		
		while(card_code_pre.length()<19) {
			
			card_code_pre += new Random().nextInt(10);
		}
		
		//交易类型
		List<String> tranTypes = new ArrayList<>();
		tranTypes.add("0000");
		tranTypes.add("9999");
		tranTypes.add("8000");
		tranTypes.add("1110");
		tranTypes.add("7000");
		tranTypes.add("1044");
		tranTypes.add("2000");
		tranTypes.add("2040");
		tranTypes.add("2090");
		tranTypes.add("6110");
		tranTypes.add("XXXX");
		tranTypes.add("1010");
		tranTypes.add("6000");
		tranTypes.add("1050");
		tranTypes.add("6010");
		
		//交易类型标志
		List<String> tranFlags = new ArrayList<>();
		tranFlags.add("O");
		tranFlags.add("R");
		
		//交易时间
		//月份
		String month = "0"+(new Random().nextInt(12)+1);
		month = month.length()==2?month:month.substring(1);
		
		//日
		String day = "0"+(new Random().nextInt(30)+1);
		day = day.length()==2?day:day.substring(1);
		
		//小时
		String hour = "0"+(new Random().nextInt(24)+1);
		hour = hour.length()==2?hour:hour.substring(1);
		
		//分秒
		String min = "0"+(new Random().nextInt(60)+1);
		min = min.length()==2?min+"00":min.substring(1)+"00";
		
		String date = month+day+hour+min;
		
		//金额
/*		String amount = new Random().nextInt()+"10000000";
		
		while(amount.length()<12) {
			amount = 0+amount;
		}*/
		
		String amount = "000000000010";
		
		//交易金额符号
		String amtAssgin = " ";
		
		//交易币种
		String curr_num = "155";
		
		//手机号码
		String tel = "000000000000";
		
		//持卡人姓名
		String name = "";
		while(name.length()<30) {
			
			name += " ";
		}
		
		//email
		String email = "";
		while(email.length()<40) {
			email += " ";
		}
		
		//c13
		List<String> c13s = new ArrayList<>();
		c13s.add("1");
		c13s.add("2");
		c13s.add("3");
		c13s.add("4");
		
		//c14
		String c14 = "";
		while(c14.length()<8) {
			c14 += " ";
		}
		
		//limit
		String limit = "0000000000";
		
		//商户代码
		List<String> commercial_codes = new ArrayList<>();
		commercial_codes.add("000110000000000");
		commercial_codes.add("000120000000000");
		commercial_codes.add("000130000000000");
		commercial_codes.add("000140000000000");
		commercial_codes.add("000150000000000");
		commercial_codes.add("000210000000000");
		commercial_codes.add("000220000000000");
		commercial_codes.add("000230000000000");
		commercial_codes.add("000310000000000");
		commercial_codes.add("000320000000000");
		commercial_codes.add("000340000000000");
		commercial_codes.add("000450000000000");
		commercial_codes.add("000420000000000");
		commercial_codes.add("000460000000000");
		commercial_codes.add("000510000000000");
		commercial_codes.add("000520000000000");
		commercial_codes.add("000530000000000");
			
		//渠道代码
		List<String> channelNos = new ArrayList<>();
		channelNos.add("V");
		channelNos.add("M");
		channelNos.add("A");
		channelNos.add("C");
		channelNos.add("B");
		channelNos.add("I");
		channelNos.add("P");
		
		//折合人民币金额
		String cn_amount = "000000000010";
		
		//折合人民币金额
		String rb_amount = "000000000010";
		
		//mcc
		String mcc = "0000";
		
		//pos
		String pos = "000";
		
		//转发机构代码
		String rd_code = "00000000000";
		
		//交易渠道分类定义
		String channel_def = "00";
		
		//c24
		String c24 = "0";
		
		//c25
		String c25 = "00";
		
		//受卡方名称地址
		String rec_address = "";
		while(rec_address.length()<40) {
			rec_address += "0";
		}
		
		String message = sendTypes.get(new Random().nextInt(sendTypes.size()));
		xy_code = xy_code;
		card_code_pre = card_code_pre;
		String transType = tranTypes.get(new Random().nextInt(tranTypes.size()));
		String transFlag = tranFlags.get(new Random().nextInt(tranFlags.size()));
		date = date;
		amount = amount;
		amtAssgin = amtAssgin;
		
		/*System.out.println(message.length());
		System.out.println(xy_code.length());
		System.out.println(card_code_pre.length());
		System.out.println(transType.length());
		System.out.println(transFlag.length());
		System.out.println(date.length());
		System.out.println(amount.length());
		System.out.println(amtAssgin.length());*/
		
		curr_num = curr_num;
		tel = tel;
		name = name;
		email = email;
		String c13 = c13s.get(new Random().nextInt(c13s.size()));
		c14 = c14;
		limit = limit;
		String commercial_code = commercial_codes.get(new Random().nextInt(commercial_codes.size()));
		
/*		System.out.println(curr_num.length());
		System.out.println(tel.length());
		System.out.println(name.length());
		System.out.println(email.length());
		System.out.println(c13.length());
		System.out.println(c14.length());
		System.out.println(limit.length());
		System.out.println(commercial_code.length());*/

		String channelNo = channelNos.get(new Random().nextInt(channelNos.size()));
		cn_amount = cn_amount;
		rb_amount = rb_amount;
		mcc = mcc;
		pos = pos;
		rd_code = rd_code;
		channel_def = channel_def;
		c24 = c24;
		c25 = c25;
		rec_address = rec_address;
		
	/*	System.out.println(channelNo.length());
		System.out.println(cn_amount.length());
		System.out.println(rb_amount.length());
		System.out.println(mcc.length());
		System.out.println(pos.length());
		System.out.println(rd_code.length());
		System.out.println(channel_def.length());
		System.out.println(c24.length());
		System.out.println(c25.length());
		System.out.println(rec_address.length());*/
		
		if(new Random().nextInt(11)%5 == 0) {
			message = "3001";
		}else {
			message = "3001";
		}
		
		if("3001".equals(message)) {
			
			message += xy_code+card_code_pre
					+transType+transFlag+date+amount+amtAssgin+curr_num+tel+name+email
					+c13+c14+limit+commercial_code+channelNo+cn_amount+rb_amount+mcc
					+pos+rd_code+channel_def+c24+c25+rec_address;
		}else if ("3003".equals(message)) {
			message += xy_code+card_code_pre
					+transType+transFlag+date+amount+amtAssgin+curr_num+tel+name+email
					+c13+c14+limit+commercial_code+channelNo;
		}
		return message;
	}
	
	public static void main(String[] args) {
		
		String msg = mockMessage();
		System.out.println(msg);
		System.out.println(msg.length());
	}
	
}
