package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.AuditCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class AuditFilter extends SearchFilter{

    private String message;

    private List<AuditCategory> categoryList = new ArrayList<>();

    public void tune() {
        super.tune();

//        this.setStartDate(LocalDateTime.now().toLocalDate().minusDays(100).atStartOfDay());
    }
}
