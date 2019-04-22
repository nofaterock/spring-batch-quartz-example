package com.nofaterock.batch.controller;

import com.nofaterock.batch.pay.Pay;
import com.nofaterock.batch.pay.Pay2;
import com.nofaterock.batch.pay.PayMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 한승룡
 * @since 2019-04-15
 */
@RestController
@RequestMapping("/api")
public class PayController {

	@Resource
	private PayMapper payMapper;

	@GetMapping("/pays")
	public List<Pay> getPays() {
		return payMapper.selectAll(2000);
	}

	@PostMapping("/pay2s")
	public void insertPay2(Pay2 pay2) {
		payMapper.insertPay2(pay2);
	}

}
