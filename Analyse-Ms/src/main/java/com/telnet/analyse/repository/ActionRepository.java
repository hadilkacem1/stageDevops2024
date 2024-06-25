package com.telnet.analyse.repository;




import com.telnet.analyse.models.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface ActionRepository  extends JpaRepository<Action,Long> {
}
