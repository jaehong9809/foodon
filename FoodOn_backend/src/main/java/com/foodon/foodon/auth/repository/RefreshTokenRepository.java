package com.foodon.foodon.auth.repository;

import com.foodon.foodon.auth.domain.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

}
