package com.algostyle.backend.repository;

import com.algostyle.backend.model.entity.Project;
import com.algostyle.backend.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findAllByUser(User user);
}
