package io.github.rimesc.planner.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class ThemeMapperTest {

    private ThemeMapper themeMapper;

    @BeforeEach
    public void setUp() {
        themeMapper = new ThemeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(themeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(themeMapper.fromId(null)).isNull();
    }
}
