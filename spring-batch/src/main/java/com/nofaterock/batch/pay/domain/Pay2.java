package com.nofaterock.batch.pay.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDateTime;

/**
 * @author nofaterock
 * @since 2019-04-11
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pay2 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long amount;
	private String txName;
	private LocalDateTime txDateTime;

	@Transient
	private String name;

	public Pay2(Long amount, String txName, LocalDateTime txDateTime) {
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = txDateTime;
		this.name = "Pay2";
	}

}
