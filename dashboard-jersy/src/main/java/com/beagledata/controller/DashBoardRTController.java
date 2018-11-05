package com.beagledata.controller;

import java.util.Map;

import com.beagledata.dao.DashBoardRTDAO;

/**
 * @author wl
 */
public class DashBoardRTController {
	
	public Map<String, Object> listarRt(){
		return DashBoardRTDAO.getInstance().listRt();
	}
	
	public static void main(String[] args) {
		Map<String, Object> listarRt = new DashBoardRTController().listarRt();
		System.out.println(listarRt);
	}
	
}
