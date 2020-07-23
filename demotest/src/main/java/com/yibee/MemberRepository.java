package com.yibee;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.Member;

public interface MemberRepository extends PagingAndSortingRepository<Member, Long>{

	@Query(value = "select MAX(m.id) FROM Member m")
	public Long getMaxId();
	
	public Optional<Member> findByUserName(String userName);
	
	@Query(value = "select count(m) from Member m where m.actived = 1 and m.email = ?1")
	public int findUniqueEmail(String email);
	
	@Query(value = "select m.userName from Member m where m.id=?1")
	public String findNameById(Long id);
}
