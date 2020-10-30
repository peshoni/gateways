package com.pepe.gateways.task.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pepe.gateways.task.models.Device;

@Repository
public interface DevicesRepository extends JpaRepository<Device, Long> {

	List<Device> findByParentId(long id);

	@Query(value = "SELECT * FROM `devices` d WHERE d.parent_id = 0 ", nativeQuery = true)
	List<Device> findAvailableDevices();

}
