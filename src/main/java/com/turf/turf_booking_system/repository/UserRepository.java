package com.turf.turf_booking_system.repository;

import com.turf.turf_booking_system.model.users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<users, Long> {
}
