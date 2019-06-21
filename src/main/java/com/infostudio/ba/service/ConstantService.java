package com.infostudio.ba.service;

import com.infostudio.ba.service.helper.model.ApConstants;
import com.infostudio.ba.service.proxy.CoreMicroserviceProxy;
import com.infostudio.ba.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;


@Service
public class ConstantService {

	private final Logger log = LoggerFactory.getLogger(ConstantService.class);

	private final CoreMicroserviceProxy coreMicroserviceProxy;

	public ConstantService(CoreMicroserviceProxy coreMicroserviceProxy) {
		this.coreMicroserviceProxy = coreMicroserviceProxy;
	}

	private Long convertStringToLong(String value, String entityName) {
		Long num = null;
		try {
			num = Long.valueOf(value);
		} catch (NumberFormatException e) {
			log.error(e.getMessage());
			throw new BadRequestAlertException("An error happened while converting string to long",
				entityName, "stringTooLongConversionError");
		}
		return num;
	}

	public Long getApConstantValueByName(String name, String token, String entityName) {
	 	ApConstants constant  = coreMicroserviceProxy.getApConstantByName(name, token).getBody();
		if (constant == null) {
			throw new BadRequestAlertException(String.format("Constant with key '%s' does not exist", name),
					entityName, "constantDoesNotExist");
		}
		return convertStringToLong(constant.getValue(), entityName);
	}
}
