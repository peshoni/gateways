/**
 * 
 */
package com.pepe.gateways.task.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pepe.gateways.task.models.ApiResponse;
import com.pepe.gateways.task.models.Device;
import com.pepe.gateways.task.models.Gateway;
import com.pepe.gateways.task.services.GatewayService;

/**
 * @author Petar Ivanov Rest controller for requests processing from the UI
 *
 */
@RestController
@CrossOrigin(/* origins = "http://localhost:4200", */ maxAge = 3600)
@RequestMapping("api/v1/")
public class GatewaysRestControler {
	private final GatewayService gatewayService;

	public GatewaysRestControler(GatewayService gatewayServ) {
		this.gatewayService = gatewayServ;
	}

	// region Gateways
	@GetMapping(value = "gateways/load")
	public Object loadRandomGateways() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Data was fetched succesfully.",
				gatewayService.loadTestCollection());
	}

	@GetMapping(value = "gateways/drop")
	public Object dropData() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Data was delete succesfully.", gatewayService.dropData());
	}

	@GetMapping(value = "gateways/uniquename")
	public Object getGateRandomName() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Name was generated succesfully.",
				gatewayService.getGatewayName());
	}

	@PostMapping(value = "gateways/")
	public Object addGateway(@RequestBody Gateway gateway) {
		return new ApiResponse<>(HttpStatus.OK.value(), "Gateway was saved succesfully.",
				gatewayService.addGateway(gateway));
	}

	@GetMapping(value = "gateways/all")
	public Object findAllGateways() {
		return new ApiResponse<>(HttpStatus.OK.value(), "Data was fetched succesfully.",
				gatewayService.getAllGateways());
	}

	// endregion Gateways
	// region Devices

	@PostMapping(value = "devices/")
	public Object addDevice(@RequestBody Device device) {
		if (device.getId() > 0) {// the old one - update it
			return new ApiResponse<>(HttpStatus.OK.value(), "Device was updated succesfully.",
					gatewayService.updateDevice(device));
		} else { // the new one
			return new ApiResponse<>(HttpStatus.OK.value(), "Device was saved succesfully.",
					gatewayService.createDevice(device));
		}
	}

	@PostMapping(value = "devices/detach/")
	public Object detachDevice(@RequestBody Device device) {
		return new ApiResponse<>(HttpStatus.OK.value(), "Device was detached succesfully.",
				gatewayService.detachDevice(device));
	}

	@GetMapping(value = "devices/state/{deviceId}/{state}")
	public Object changeDeviceState(@PathVariable long deviceId, @PathVariable boolean state) {
		Device device = gatewayService.findDeviceById(deviceId);
		if (device != null) {
			device.setOnline(state);
			gatewayService.updateDevice(device);
			return new ApiResponse<>(HttpStatus.OK.value(), "Device state was chenged succesfully.", true);
		}
		return new ApiResponse<>(HttpStatus.OK.value(), "Device not found.", false);
	}

	@GetMapping(value = "devices/available")
	public Object getAvailableDevices() {
		List<Device> devices = gatewayService.getAllAvailableDevices();
		return new ApiResponse<>(HttpStatus.OK.value(), "Awailable devices was fetched.", devices);
	}
	// endregion Devices
}
