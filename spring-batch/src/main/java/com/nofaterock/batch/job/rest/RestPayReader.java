package com.nofaterock.batch.job.rest;

import com.nofaterock.batch.pay.Pay;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author nofaterock
 * @since 2019-04-15
 */
@RequiredArgsConstructor
public class RestPayReader implements ItemReader<Pay> {

	private final RestTemplate restTemplate;

	private int idx;
	private List<Pay> pays;

	@Override
	public Pay read() throws Exception {
		if (this.pays == null) {
			this.pays = fetch();
		}

		Pay pay = null;
		if (idx < this.pays.size()) {
			pay = this.pays.get(idx);
			idx++;
		}

		return pay;
	}

	private List<Pay> fetch() {
		ResponseEntity<Pay[]> response = restTemplate.getForEntity("http://localhost:8080/api/pays", Pay[].class);
		return Arrays.asList(Objects.requireNonNull(response.getBody()));
	}

}
