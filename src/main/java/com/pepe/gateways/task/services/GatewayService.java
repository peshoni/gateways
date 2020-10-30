/**
 * 
 */
package com.pepe.gateways.task.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pepe.gateways.task.models.Device;
import com.pepe.gateways.task.models.Gateway;
import com.pepe.gateways.task.repositories.DevicesRepository;
import com.pepe.gateways.task.repositories.GatewayRepository;
import com.pepe.gateways.task.utils.GatesUtility;

/**
 * 
 * @author Petar Ivanov
 *
 */
@Service(value = "gatewayService")
public class GatewayService {
	private final GatewayRepository gatewayRepository;
	private final DevicesRepository devicesRepository;

	public GatewayService(GatewayRepository gatewayRepo, DevicesRepository devicesRepo) {
		this.gatewayRepository = gatewayRepo;
		this.devicesRepository = devicesRepo;
	}

	/**
	 * Loads 10 Gateways with random data and child devices.
	 * 
	 * @return
	 */
	public boolean loadTestCollection() {
		for (int i = 0; i < 10; i++) {
			Gateway gate = new Gateway();
			gate.setName("name_" + i);
			gate.setSerialNumber(i + "_" + System.currentTimeMillis());
			gate.setIpAddress((i + 1) + ".111.111.222");
			gate = this.addGateway(gate);
			long parentId = gate.getId();
			int childs = GatesUtility.getRandomNumberUsingNextInt(1, 10);
			for (int j = 0; j < childs; j++) {
				Device d = new Device();
				d.setParentId(parentId);
				d.setOnline(true);
				d.setVendor(GatesUtility.generatesString(10).toUpperCase());
				this.createDevice(d);
			}
		}
		return true;
	}

	/**
	 * Fetches all gateway object with
	 * 
	 * @return
	 */
	public List<Gateway> getAllGateways() {
		List<Gateway> gateways = this.gatewayRepository.findAll();
		attachGatewayChilds(gateways);
		return gateways;
	}

	private void attachGatewayChilds(List<Gateway> gateways) {
		for (Gateway gateway : gateways) {
			gateway.setDevices(devicesRepository.findByParentId(gateway.getId()));
		}
	}

	public List<Device> getAllDevices() {
		return this.devicesRepository.findAll();
	}

	/**
	 * Saves {@link Gateway} into {@link GatewayRepository}
	 * 
	 * @param gateway The {@link Gateway} object
	 * @return {@link Gateway}
	 */
	public Gateway addGateway(Gateway gateway) {
		return this.gatewayRepository.save(gateway);
	}

	/**
	 * Creates new row into `device` table
	 * 
	 * @param device {@link Device}
	 * @return {@link Device}
	 */
	public Device createDevice(Device device) {
		device.setCreated(new Date());
		return this.devicesRepository.save(device);
	}

	public Device findDeviceById(long deviceId) {
		Optional<Device> optDevice = devicesRepository.findById(deviceId);
		if (optDevice.isPresent()) {
			return optDevice.get();
		}
		return null;
	}

	public Device detachDevice(Device device) {
		device.setParentId(0);
		return this.updateDevice(device);
	}

	/**
	 * A method for entity saving.
	 * 
	 * @param device The {@link Device}
	 * @return {@link Device}
	 */
	public Device updateDevice(Device device) {
		device.setUpdated(new Date());
		return devicesRepository.save(device);
	}

	/**
	 * A method for searching for not assigned to gateway devices
	 * 
	 * @return
	 */
	public List<Device> getAllAvailableDevices() {
		return devicesRepository.findAvailableDevices();
	}

	/**
	 * Gets random string with 10 length from digit and upper case letters.
	 * 
	 * @return
	 */
	public String getGatewayName() {
		return GatesUtility.generatesString(10).toUpperCase();
	}

	public boolean dropData() {
		this.devicesRepository.deleteAll();
		this.gatewayRepository.deleteAll();
		return true;
	}

}
