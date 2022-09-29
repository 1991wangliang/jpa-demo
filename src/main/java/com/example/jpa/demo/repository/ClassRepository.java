package com.example.jpa.demo.repository;

import com.example.jpa.demo.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClassRepository extends JpaRepository<SchoolClass,Integer>, JpaSpecificationExecutor<SchoolClass> {


}
