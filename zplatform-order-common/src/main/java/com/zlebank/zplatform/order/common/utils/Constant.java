/* 
 * Constant.java  
 * 
 * version TODO
 *
 * 2016年10月13日 
 * 
 * Copyright (c) 2016,zlebank.All rights reserved.
 * 
 */
package com.zlebank.zplatform.order.common.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

/**
 * Class Description
 *
 * @author guojia
 * @version
 * @date 2016年10月13日 上午9:32:56
 * @since 
 */
public class Constant {


	
    
	private String instead_pay_realtime_start_time;
	private String instead_pay_realtime_end_time;
	private String zlebank_coopinsti_code;
	private String refund_day;
    private boolean canRun;
    private String refresh_interval;
    private static Constant constant;
    public static synchronized Constant getInstance(){
		if(constant==null){
			constant = new Constant();
		}
		return constant;
	}
    
	private Constant(){
		refresh();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (canRun) {
					try {
						refresh();
						int interval = NumberUtils.toInt(refresh_interval, 60) * 1000;// 刷新间隔，单位：秒
						Thread.sleep(interval);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	
	public void refresh(){
		try {
			String path = "/home/web/trade/";
			File file = new File(path+ "zlrt.properties");
			if(!file.exists()){
			    path = getClass().getResource("/").getPath();
			    file = null;
			}
			Properties prop = new Properties();
			InputStream stream = null;
			stream = new BufferedInputStream(new FileInputStream(new File(path+ "zlrt.properties")));
			prop.load(stream);
			canRun = true;
			refresh_interval = prop.getProperty("refresh_interval");
			instead_pay_realtime_start_time = prop.getProperty("instead_pay_realtime_start_time");
			instead_pay_realtime_end_time = prop.getProperty("instead_pay_realtime_end_time");
			zlebank_coopinsti_code =  prop.getProperty("zlebank_coopinsti_code");
			refund_day = prop.getProperty("refund_day","180");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the instead_pay_realtime_start_time
	 */
	public String getInstead_pay_realtime_start_time() {
		return instead_pay_realtime_start_time;
	}

	/**
	 * @param instead_pay_realtime_start_time the instead_pay_realtime_start_time to set
	 */
	public void setInstead_pay_realtime_start_time(
			String instead_pay_realtime_start_time) {
		this.instead_pay_realtime_start_time = instead_pay_realtime_start_time;
	}

	/**
	 * @return the instead_pay_realtime_end_time
	 */
	public String getInstead_pay_realtime_end_time() {
		return instead_pay_realtime_end_time;
	}

	/**
	 * @param instead_pay_realtime_end_time the instead_pay_realtime_end_time to set
	 */
	public void setInstead_pay_realtime_end_time(
			String instead_pay_realtime_end_time) {
		this.instead_pay_realtime_end_time = instead_pay_realtime_end_time;
	}

	/**
	 * @return the canRun
	 */
	public boolean isCanRun() {
		return canRun;
	}

	/**
	 * @param canRun the canRun to set
	 */
	public void setCanRun(boolean canRun) {
		this.canRun = canRun;
	}

	/**
	 * @return the refresh_interval
	 */
	public String getRefresh_interval() {
		return refresh_interval;
	}

	/**
	 * @param refresh_interval the refresh_interval to set
	 */
	public void setRefresh_interval(String refresh_interval) {
		this.refresh_interval = refresh_interval;
	}

	/**
	 * @return the zlebank_coopinsti_code
	 */
	public String getZlebank_coopinsti_code() {
		return zlebank_coopinsti_code;
	}

	/**
	 * @param zlebank_coopinsti_code the zlebank_coopinsti_code to set
	 */
	public void setZlebank_coopinsti_code(String zlebank_coopinsti_code) {
		this.zlebank_coopinsti_code = zlebank_coopinsti_code;
	}

	/**
	 * @return the refund_day
	 */
	public String getRefund_day() {
		return refund_day;
	}

	/**
	 * @param refund_day the refund_day to set
	 */
	public void setRefund_day(String refund_day) {
		this.refund_day = refund_day;
	}
	
	
	
	
}
