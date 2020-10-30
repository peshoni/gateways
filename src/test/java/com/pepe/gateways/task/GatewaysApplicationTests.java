package com.pepe.gateways.task;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pepe.gateways.task.models.Device;
import com.pepe.gateways.task.models.Gateway;
import com.pepe.gateways.task.repositories.DevicesRepository;
import com.pepe.gateways.task.repositories.GatewayRepository;
import com.pepe.gateways.task.services.GatewayService;

@SpringBootTest
class GatewaysApplicationTests {
	private final String gateName = "gateway_1";
	private final String vendorName = "vendor_1";

	@Autowired
	private GatewayRepository gatewayRepository;

	@Autowired
	private DevicesRepository deviceRepository;

	@Autowired
	private GatewayService gatewayService;

	@Test
	void testSaveAndGetGatewayByName() {
		gatewayRepository.save(buildGateway(gateName));
		Gateway gate = gatewayRepository.findByName(gateName);
		assertEquals(gateName, gate.getName());
		clearDataFromDB();
	}

	@Test
	void findByParentById() {
		gatewayRepository.save(gatewayRepository.save(buildGateway(gateName)));
		Gateway gate = gatewayRepository.findByName(gateName);
		Device device = new Device();
		device.setParentId(gate.getId());
		deviceRepository.save(device);
		assertEquals(1, deviceRepository.findByParentId(gate.getId()).size());
		clearDataFromDB();
	}

//Service tests
	@Test
	void testLoadTestCollectionMethodForChilds() {
		gatewayService.loadTestCollection();
		List<Device> devices = deviceRepository.findByParentId(1L);
		assertTrue(devices.size() > 0);
		clearDataFromDB();
	}

	@Test
	void testForGatewayServiceAddGatewayMethod() {
		Gateway gate = gatewayService.addGateway(buildGateway(gateName));
		assertTrue(gate.getId() > 0);
		clearDataFromDB();
	}

	@Test
	void testForGatewayServiceCreateDeviceMethod() {
		Device device = gatewayService.createDevice(this.buildDevice(vendorName, 0));
		assertNotNull(device.getCreated());
		clearDataFromDB();
	}

	@Test
	void testForGatewayServiceFindDeviceByIdMethodWhitNotExistingId() {
		assertNull(gatewayService.findDeviceById(0));
	}

	@Test
	void testForGatewayServiceFindDeviceByIdMethodWithExistId() {
		Device device = gatewayService.createDevice(this.buildDevice(vendorName, 0));
		assertNotNull(gatewayService.findDeviceById(device.getId()));
		clearDataFromDB();
	}

	@Test
	void testForGatewayServiceDetachDeviceMethod() {
		Device device = gatewayService.createDevice(this.buildDevice(vendorName, 1));
		Device afterDetach = gatewayService.detachDevice(device);
		assertEquals(afterDetach.getParentId(), 0);
		clearDataFromDB();
	}

	@Test
	void testForGatewayServiceUpdateDeviceMethodWithChangeParentIdForExample() {
		Gateway gate = gatewayService.addGateway(buildGateway(gateName));
		Device device = gatewayService.createDevice(this.buildDevice(vendorName, 0));
		device.setParentId(gate.getId());
		Device updatedDevice = gatewayService.updateDevice(device);
		assertEquals(updatedDevice.getParentId(), gate.getId());
		clearDataFromDB();
	}

	@Test
	void testForGatewayServicegetAllAvailableDevicesMethod() {
		Gateway gate = gatewayService.addGateway(buildGateway(gateName));
		List<Device> devices = new ArrayList<Device>();
		for (int i = 0; i < 10; i++) {
			Device device = gatewayService.createDevice(this.buildDevice(vendorName, 0));
			if (i % 2 == 0) {
				device.setParentId(gate.getId());
			}
			devices.add(device);
		}
		deviceRepository.saveAll(devices);

		List<Device> childs = gatewayService.getAllAvailableDevices();
		assertEquals(childs.size(), 5L);
		clearDataFromDB();
	}

	@Test
	void testForGatewayServicegetGatewayNameMethod() {
		assertEquals(gatewayService.getGatewayName().length(), 10);
	}

//Utils
	private Gateway buildGateway(String name) {
		Gateway gate = new Gateway();
		gate.setName(name);
		return gate;
	}

	private Device buildDevice(String vendorName, long parentId) {
		Device device = new Device();
		device.setVendor(vendorName);
		return device;
	}

	private void clearDataFromDB() {
		gatewayService.dropData();
	}
}
