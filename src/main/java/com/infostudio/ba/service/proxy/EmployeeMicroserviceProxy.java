package com.infostudio.ba.service.proxy;

import com.infostudio.ba.service.helper.model.EmEmployees;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.infostudio.ba.config.Constants.AUTHORIZATION_HEADER;

@FeignClient(name = "hcmEmpMicroservice")
public interface EmployeeMicroserviceProxy {

	@GetMapping("/api/em-employees/{id}")
	ResponseEntity<EmEmployees> getEmployeeById(@PathVariable(name = "id") Long id,
												  @RequestHeader(AUTHORIZATION_HEADER) String token);

}
