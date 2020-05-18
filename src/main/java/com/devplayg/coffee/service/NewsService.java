package com.devplayg.coffee.service;

import com.devplayg.coffee.entity.FactoryEvent;
import com.devplayg.coffee.entity.filter.NewsFilter;
import com.devplayg.coffee.repository.factoryevent.FactoryEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

@Service
public class NewsService {
    private final FactoryEventRepository factoryEventRepository;


    public NewsService(FactoryEventRepository factoryEventRepository) {
        this.factoryEventRepository = factoryEventRepository;
    }

    public HashMap<String, Object> get(NewsFilter filter) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("system", this.getSystemNews());
        m.put("factoryevent", this.getFactoryEventNews(filter));
        return m;
    }

    private HashMap<String, Object> getSystemNews() {
        HashMap<String, Object> m = new HashMap<>();
        m.put("time", System.currentTimeMillis() / 1000);
        m.put("timezone", TimeZone.getDefault().toZoneId());
        return m;
    }

    private HashMap<String, Object> getFactoryEventNews(NewsFilter filter) {
        HashMap<String, Object> m = new HashMap<>();
        if (filter.getLastFactoryEventId() > 0) {
            List<FactoryEvent> list = factoryEventRepository.findTop20ByIdGreaterThanAndDateAfterOrderByIdDesc(filter.getLastFactoryEventId(), LocalDateTime.now().minusSeconds(300));
            m.put("list", list);
            return m;
        }
        List<FactoryEvent> list = factoryEventRepository.findTop20ByDateGreaterThanEqualOrderByIdDesc(LocalDateTime.now().minusSeconds(300));
        m.put("list", list);

        return m;
    }

}
