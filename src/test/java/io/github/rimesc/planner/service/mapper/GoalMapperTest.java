package io.github.rimesc.planner.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GoalMapperTest {

    private GoalMapper goalMapper;

    @BeforeEach
    public void setUp() {
        goalMapper = new GoalMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(goalMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(goalMapper.fromId(null)).isNull();
    }
}
