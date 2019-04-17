package com.nofaterock.batch.job.rest;

import com.nofaterock.batch.pay.Pay2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author nofaterock
 * @since 2019-04-15
 */
@Slf4j
@RequiredArgsConstructor
public class RestPay2Writer implements ItemWriter<Pay2> {

	private final RestTemplate restTemplate;

	@Override
	public void write(List<? extends Pay2> pay2s) throws Exception {
		for (Pay2 pay2 : pay2s) {
			restTemplate.postForEntity("http://localhost:8080/api/pay2s", pay2, null);
		}
	}
}
