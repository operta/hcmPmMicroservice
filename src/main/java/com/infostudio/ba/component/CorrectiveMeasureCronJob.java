package com.infostudio.ba.component;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.infostudio.ba.domain.PmCorMeasureStates;
import com.infostudio.ba.domain.PmCorrectiveMeasures;
import com.infostudio.ba.repository.PmCorMeasureStatesRepository;
import com.infostudio.ba.repository.PmCorrectiveMeasuresRepository;
import com.infostudio.ba.service.helper.model.ApConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.infostudio.ba.config.Constants.CORRECTIVE_MEASURE_EXPIRED_TIME;

@Component
public class CorrectiveMeasureCronJob {

	private final Logger logger = LoggerFactory.getLogger(CorrectiveMeasureCronJob.class);

	@PersistenceContext
	private EntityManager entityManager;

	private final PmCorrectiveMeasuresRepository pmCorrectiveMeasuresRepository;

	private final PmCorMeasureStatesRepository pmCorMeasureStatesRepository;

	private final static String CORRECTIVE_MEASURE_EXPIRED_KEY = "correctiveMeasureExpiredState";


	public CorrectiveMeasureCronJob(PmCorrectiveMeasuresRepository pmCorrectiveMeasuresRepository,
								    PmCorMeasureStatesRepository pmCorMeasureStatesRepository) {
		this.pmCorrectiveMeasuresRepository = pmCorrectiveMeasuresRepository;
		this.pmCorMeasureStatesRepository = pmCorMeasureStatesRepository;
	}

	@Scheduled(fixedRate = CORRECTIVE_MEASURE_EXPIRED_TIME)
	public void correctiveMeasureExpired() {
		logger.debug("CORRECTIVE MEASURE STATUS CHANGE CRON JOB STARTED");
		ApConstants expiredStateConstant = getExpiredStateConstant();
		logger.debug("EXPIRED STATE CONSTANT: {}", expiredStateConstant);
		if (expiredStateConstant == null) {
			return;
		}
		try {
			Long correctiveMeasureExpiredStateId = Long.valueOf(expiredStateConstant.getValue());
			List<PmCorrectiveMeasures> correctiveMeasuresThatExpired = getCorrectiveMeasuresThatExpired(correctiveMeasureExpiredStateId);
			if (correctiveMeasuresThatExpired.isEmpty()) {
				return ;
			}
			correctiveMeasuresThatExpired.forEach((cm) -> {
				logger.debug("CORRECTIVE MEASURE: {}", cm);
			});

			PmCorMeasureStates corMeasureStatesExpired = pmCorMeasureStatesRepository.findOne(correctiveMeasureExpiredStateId);
			logger.debug("CORRECTIVE MEASURE STATE: {}", corMeasureStatesExpired);
			for (PmCorrectiveMeasures measure : correctiveMeasuresThatExpired) {
				measure.setIdCmState(corMeasureStatesExpired);
				pmCorrectiveMeasuresRepository.save(measure);
			}
		} catch (NumberFormatException e) {
			logger.error("The value: {} couldn't be parsed to a long", expiredStateConstant.getValue());
		}
	}

	private ApConstants getExpiredStateConstant() {
		String query = "SELECT a.key, a.value FROM ap_constants a WHERE a.key='" + CORRECTIVE_MEASURE_EXPIRED_KEY + "'";
		List<Object[]> rawRecords = entityManager.createNativeQuery(query).getResultList();
		ApConstants apConstants = null;
		for (Object[] record : rawRecords){
			if (record.length == 2) {
				apConstants = new ApConstants();
				apConstants.setKey((String)record[0]);
				apConstants.setValue((String)record[1]);
			}
		}
		return apConstants;
	}

	private List<PmCorrectiveMeasures> getCorrectiveMeasuresThatExpired(Long correctiveMeasureExpiredStateId) {
		return pmCorrectiveMeasuresRepository.findAllByEndDateBeforeAndIdCmStateIdNot(LocalDate.now(), correctiveMeasureExpiredStateId);
	}

}
