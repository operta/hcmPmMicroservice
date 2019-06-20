package com.infostudio.ba.service.proxy;

import com.infostudio.ba.service.helper.model.ApConstants;
import com.infostudio.ba.service.helper.model.UserNotifications;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.infostudio.ba.config.Constants.AUTHORIZATION_HEADER;

@FeignClient(name = "hcmCoreMicroservice")
public interface CoreMicroserviceProxy {

	@GetMapping("/api/ap-constants/key/{name}")
	ResponseEntity<ApConstants> getApConstantByName(@PathVariable(name = "name") String name,
													@RequestHeader(AUTHORIZATION_HEADER) String token);

	@PostMapping("/api/user-notifications")
	ResponseEntity<UserNotifications> createNotification(@RequestBody UserNotifications userNotifications,
														 @RequestHeader(AUTHORIZATION_HEADER) String token);

}
