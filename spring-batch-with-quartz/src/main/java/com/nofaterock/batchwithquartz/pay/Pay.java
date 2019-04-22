package com.nofaterock.batchwithquartz.pay;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author 한승룡
 * @since 2019-04-11
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
public class Pay {

	private Long id;
	private Long amount;
	private String txName;
	private LocalDateTime txDateTime;

}
