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
import java.time.format.DateTimeFormatter;

/**
 * @author nofaterock
 * @since 2019-04-11
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pay {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long amount;
	private String txName;
	private LocalDateTime txDateTime;

	@Transient
	private String name;

	public Pay(Long amount, String txName, String txDateTime) {
		this.amount = amount;
		this.txName = txName;
		this.txDateTime = LocalDateTime.parse(txDateTime, FORMATTER);
		this.name = "Pay";
	}

	public Pay(Long id, Long amount, String txName, String txDateTime) {
		this(amount, txName, txDateTime);
		this.id = id;
	}

}
