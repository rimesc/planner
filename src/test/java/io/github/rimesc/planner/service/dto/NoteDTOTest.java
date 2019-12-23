package io.github.rimesc.planner.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.github.rimesc.planner.web.rest.TestUtil;

public class NoteDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NoteDTO.class);
        NoteDTO noteDTO1 = new NoteDTO();
        noteDTO1.setId(1L);
        NoteDTO noteDTO2 = new NoteDTO();
        assertThat(noteDTO1).isNotEqualTo(noteDTO2);
        noteDTO2.setId(noteDTO1.getId());
        assertThat(noteDTO1).isEqualTo(noteDTO2);
        noteDTO2.setId(2L);
        assertThat(noteDTO1).isNotEqualTo(noteDTO2);
        noteDTO1.setId(null);
        assertThat(noteDTO1).isNotEqualTo(noteDTO2);
    }
}
