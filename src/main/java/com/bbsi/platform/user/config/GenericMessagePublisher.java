
package com.bbsi.platform.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.bbsi.platform.common.event.dto.GenericEventDTO;
import com.bbsi.platform.common.generic.LogUtils;
import com.bbsi.platform.common.messaging.GenericEventMessagePublisher;
import com.bbsi.platform.common.messaging.Processor;

@Service
@Primary
@EnableBinding(Processor.class)
public class GenericMessagePublisher extends GenericEventMessagePublisher{

	@Autowired
	private Processor sampleMQSource;

	@Override
	public void publishMessage(GenericEventDTO eventDTO) {
		LogUtils.basicDebugLog.accept("Publishing the notification from user mgmt.. ::: " + eventDTO.getEmpCode());
		sampleMQSource.sample().send(MessageBuilder.withPayload(eventDTO).build());
	}
}
