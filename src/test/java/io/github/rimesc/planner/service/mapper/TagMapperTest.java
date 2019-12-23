package io.github.rimesc.planner.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TagMapperTest {

    private TagMapper tagMapper;

    @BeforeEach
    public void setUp() {
        tagMapper = new TagMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(tagMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(tagMapper.fromId(null)).isNull();
    }
}
