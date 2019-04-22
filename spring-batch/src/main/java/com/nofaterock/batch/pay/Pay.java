package com.nofaterock.batch.pay;

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
 * @author 한승룡
 * @since 2019-04-11
 */
@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Pay {

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
		this.txDateTime = LocalDateTime.parse(txDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
		this.name = "Pay";
	}

}
