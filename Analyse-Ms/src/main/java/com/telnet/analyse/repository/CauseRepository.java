package com.telnet.analyse.repository;

import com.telnet.analyse.models.Cause;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


@Transactional
public interface CauseRepository extends JpaRepository<Cause, Long> {
}
