package org.smartlink.server.image.service.impl;

import com.anwen.mongo.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.smartlink.server.image.momain.DataImage;
import org.smartlink.server.image.service.DataImageServer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataImageService  extends ServiceImpl<DataImage> implements DataImageServer {



}
