package com.nofaterock.batch.pay;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 한승룡
 * @since 2019-04-12
 */
@Mapper
public interface PayMapper {

	List<Pay> selectAll(long amount);

	void insertPay2(Pay2 pay2);

}
