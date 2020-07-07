package com.yibee;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.yibee.entity.CountMessage;
import com.yibee.entity.Message;

public interface MessageRepository extends PagingAndSortingRepository<Message, Long>{
	@Query(value = "FROM Message m where (m.toId=?1 and m.fromId=?2) or (m.toId=?2 and m.fromId=?1)")
	Page<Message> findMessageByIDs(Long id1,Long id2,Pageable pageable);
	
	@Query(value = "select new com.yibee.entity.CountMessage(count(m),m.toId,m.fromId,u.userName) FROM Message m left join Member u on m.fromId=u.id where m.toId=?1 and m.hasRead=0 group by m.fromId")
	List<CountMessage> findNewCountById(Long toId);
}
