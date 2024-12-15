package org.smartlink.server.task.service.impl;

import com.anwen.mongo.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.smartlink.server.task.momain.DataTask;
import org.smartlink.server.task.service.DataTaskServer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataTaskService extends ServiceImpl<DataTask> implements DataTaskServer {
}

