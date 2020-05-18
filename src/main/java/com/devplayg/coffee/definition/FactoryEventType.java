package com.devplayg.coffee.definition;

import com.devplayg.coffee.util.EnumModel;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
public class FactoryEventType {
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SteelEventCategory steelEventCategory;

    public enum Category implements EnumModel {

        // High
        FIRE("factoryevent.fire", 7),
        FUME("factoryevent.fume", 8),
        SPARK("factoryevent.spark", 6),
        HAND_FOOT_CRUSH("factoryevent.hand-foot-crush", 0),
        FALLING("factoryevent.falling", 1),

        // Middle
        PPE_REQUIRED("factoryevent.no-ppe", 2),

        // Low
        ABNORMAL_CONDITION("factoryevent.abnormal-condition", 3),
        INTRUSION("factoryevent.intrusion", 4),
        OBSTACLE("factoryevent.obstacle", 5),
        WORK("factoryevent.work", 11);

        private String description;
        private int value;

        Category(String description, int value) {
            this.description = description;
            this.value = value;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String getCode() {
            return name();
        }

        public int getValue() {
            return value;
        }
    }

    public enum SteelEventCategory implements EnumModel {

        STEEL_PAINTING_DEFECT("factoryevent.steel-painting-defect", 9),
        STEEL_PAINTING_COMPLETE("factoryevent.steel-painting-complete", 10);

        private String description;
        private int value;

        SteelEventCategory(String description, int value) {
            this.description = description;
            this.value = value;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String getCode() {
            return name();
        }

        public int getValue() {
            return value;
        }
    }

    public enum DayAndNight implements EnumModel {
        DAY("day", 1),
        NIGHT("night", 2);

        private String description;
        private int value;

        DayAndNight(String description, int value) {
            this.description = description;
            this.value = value;
        }

        @Override
        public String getDescription() {
            return this.description;
        }

        @Override
        public String getCode() {
            return name();
        }

        public int getValue() {
            return value;
        }
    }
}
