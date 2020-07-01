package com.yibee;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Collect;

public interface CollectRepository  extends PagingAndSortingRepository<Collect, Long>{
	 @Query(value = "from Collect c where c.goodsId=?1 and c.memberId = ?2")
	 Optional<Collect> findByGoodsidAndMemberid(Long goodsId,Long memberId);

}
