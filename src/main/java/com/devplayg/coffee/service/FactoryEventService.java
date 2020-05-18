package com.devplayg.coffee.service;

import com.devplayg.coffee.entity.FactoryEvent;
import com.devplayg.coffee.entity.filter.FactoryEventFilter;
import com.devplayg.coffee.entity.filter.NewsFilter;
import com.devplayg.coffee.exception.ResourceNotFoundException;
import com.devplayg.coffee.framework.AssetManager;
import com.devplayg.coffee.repository.device.DeviceRepository;
import com.devplayg.coffee.repository.factoryevent.FactoryEventPredicate;
import com.devplayg.coffee.repository.factoryevent.FactoryEventRepository;
import com.devplayg.coffee.repository.factoryevent.FactoryEventRepositoryImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class FactoryEventService {
    private final FactoryEventRepositoryImpl factoryEventRepositoryImpl;
    private final FactoryEventRepository factoryEventRepository;
    private final AssetManager assetManager;
    private final DeviceRepository deviceRepository;

    public FactoryEventService(FactoryEventRepository factoryEventRepository, FactoryEventRepositoryImpl factoryEventRepositoryImpl, AssetManager assetManager, DeviceRepository deviceRepository) {
        this.factoryEventRepositoryImpl = factoryEventRepositoryImpl;
        this.factoryEventRepository = factoryEventRepository;
        this.assetManager = assetManager;
        this.deviceRepository = deviceRepository;
    }

    public List<FactoryEvent> getList(FactoryEventFilter filter, Pageable pageable) {
        // 데이터 조회
        List<FactoryEvent> list = factoryEventRepositoryImpl.find(FactoryEventPredicate.find(filter), pageable);

        // 기관정보 설정
        list.forEach(r -> r.setAsset(assetManager.get(r.getFactoryId())));
        return list;
    }

    public Page<FactoryEvent> getPageList(FactoryEventFilter filter, Pageable pageable) {
        Page<FactoryEvent> page = factoryEventRepository.findAll(FactoryEventPredicate.find(filter), pageable);
        page.getContent().forEach(r -> r.setAsset(assetManager.get(r.getFactoryId())));
        return page;
    }

    public HashMap<String, Object> getNews(NewsFilter filter) {
        HashMap<String, Object> m = new HashMap<>();
        List<FactoryEvent> list = null;
        if (filter.getFactoryIdList().size() > 0) {
            list = factoryEventRepository.findTop20ByIdGreaterThanAndDateAfterAndFactoryIdInOrderByIdDesc(filter.getLastFactoryEventId(), LocalDateTime.now().minusSeconds(300), filter.getFactoryIdList());
            m.put("list", list);
            return m;
        }
        list = factoryEventRepository.findTop20ByIdGreaterThanAndDateAfterOrderByIdDesc(filter.getLastFactoryEventId(), LocalDateTime.now().minusSeconds(300));
        m.put("list", list);
        return m;
    }

    public FactoryEvent toggleEventType(long logId) {
        FactoryEvent factoryEvent = factoryEventRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("factoryevent", "id", logId));

        int eventType = 0;
        if (factoryEvent.getEventType() >= 0) { // 일반 이벤트이면 이벤트를 ignore 함
            eventType = -1;
        } else { // 무시된 이벤트이면 복구
            eventType = factoryEvent.getOriginEventType();
        }

        factoryEvent.setEventType(eventType);
        factoryEventRepository.save(factoryEvent);
        return factoryEvent;
    }
}
