package com.algostyle.backend.repository;

import com.algostyle.backend.model.entity.FichierExcel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilePathRepository extends JpaRepository<FichierExcel,Long>{

}
