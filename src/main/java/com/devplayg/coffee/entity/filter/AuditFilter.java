package com.devplayg.coffee.entity.filter;

import com.devplayg.coffee.definition.AuditCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class AuditFilter extends SearchFilter{

    public AuditFilter() {
        categoryList = new ArrayList<>();
    }

    // Message
    private String message;

    // Category
    private List<AuditCategory> categoryList;

    // IP
    private String ip;

    public void tune() {
        super.tune();

//        this.setStartDate(LocalDateTime.now().toLocalDate().minusDays(100).atStartOfDay());
    }


}
