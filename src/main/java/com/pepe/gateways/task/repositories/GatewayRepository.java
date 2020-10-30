/**
 * 
 */
package com.pepe.gateways.task.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pepe.gateways.task.models.Gateway;

/**
 * 
 * @author Petar Ivanov
 *
 */
@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {

	Gateway findByName(String name);

}
