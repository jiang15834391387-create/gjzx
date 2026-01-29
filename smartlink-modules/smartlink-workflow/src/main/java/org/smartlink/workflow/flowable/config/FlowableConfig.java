package org.smartlink.workflow.flowable.config;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.smartlink.workflow.flowable.handler.TaskTimeoutJobHandler;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * flowable配置
 *
 * @author may
 */
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    @Autowired
    private GlobalFlowableListener globalFlowableListener;
    @Autowired
    private IdentifierGenerator identifierGenerator;

    @Autowired
    private ObjectProvider<AutoSkipFlowableListener> autoSkipFlowableListenerProvider;

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        processEngineConfiguration.setIdGenerator(() -> identifierGenerator.nextId(null).toString());

        List<FlowableEventListener> listeners = new ArrayList<>();
        listeners.add(globalFlowableListener);

        AutoSkipFlowableListener autoSkip = autoSkipFlowableListenerProvider.getIfAvailable();
        if (autoSkip != null) {
            listeners.add(autoSkip);
        }

        processEngineConfiguration.setEventListeners(listeners);
        processEngineConfiguration.addCustomJobHandler(new TaskTimeoutJobHandler());

    }
}
