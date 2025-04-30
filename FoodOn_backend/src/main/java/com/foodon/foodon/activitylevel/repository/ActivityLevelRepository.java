package com.foodon.foodon.activitylevel.repository;

import com.foodon.foodon.activitylevel.domain.ActivityLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLevelRepository extends JpaRepository<ActivityLevel, Long> {
}
