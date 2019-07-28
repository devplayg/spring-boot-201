package com.devplayg.coffee.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WebHelper {
    public static RedirectView getRedirectView(String uri) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(uri);
        redirectView.setExposeModelAttributes(false);
        return redirectView;
    }

    public static List<OrderSpecifier> getOrders(PathBuilder pathBuilder, Pageable pageable, OrderSpecifier defaultOrder) {
        // Default order
        if (pageable.getSort() == Sort.unsorted()) {
            return Arrays.asList(defaultOrder);
        }

        return pageable.getSort().stream()
                .map(o -> {
                    PathBuilder<?> path;
                    path = pathBuilder.get(o.getProperty());
                    return new OrderSpecifier(Order.valueOf(o.getDirection().name()), path);
                })
                .collect(Collectors.toList());
    }
}
