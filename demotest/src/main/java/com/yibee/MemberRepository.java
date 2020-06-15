package com.yibee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Member;

public interface MemberRepository extends PagingAndSortingRepository<Member, Long>{

	@Query(value = "select MAX(m.id) FROM Member m")
	public Long getMaxId();
}
