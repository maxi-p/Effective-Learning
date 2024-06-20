package com.maxip.codeupload.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.maxip.codeupload.persistence.entity.Note;
import com.maxip.codeupload.persistence.entity.User;
import com.maxip.codeupload.persistence.repository.springdatajpa.NoteRepository;
import com.maxip.codeupload.persistence.repository.springdatajpa.SubjectCategoryRepository;
import com.maxip.codeupload.persistence.repository.springdatajpa.UserRepository;
import com.maxip.codeupload.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.Charset;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest
{
    @Autowired
    private MockMvc     mockMvc;
    private String      TOKEN;
    private String      USERNAME;
    private User        USER;
    private Long        TEST_ID = 1L;
    private String      TEST_CATEGORY = "JAVA_API";
    private String      TEST_KEY_STRING = "str.substring(int start, int end);";
    private String      TEST_VALUE_STRING = "substring from to index. from inclusive, to non-inclusive";
    private String      TEST_UPDATE = "[UPDATE!!!]";
    public  MediaType   APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private SubjectCategoryRepository subjectCategoryRepository;
    @Autowired
    private Environment env;

    @BeforeEach
    public void setUp() throws Exception
    {
        USERNAME    = env.getProperty("test.admin");
        USER        = userRepository.findByUsername(USERNAME);
        TOKEN       = jwtService.generateToken(userDetailsService.loadUserByUsername(USERNAME));
    }

    @Test
    public void shouldGetAllNotes() throws Exception
    {
        Set<Note> notes = noteRepository.findByUser(USER);
        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(notes);

        MvcResult res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/note").header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        String content = res.getResponse().getContentAsString();

        assertEquals(content, json);
    }

    @Test
    public void shouldReadNote() throws Exception
    {
        Note note = noteRepository.findByIdAndUser(TEST_ID, USER);
        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(note);

        MvcResult res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/note/"+TEST_ID).header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        String content = res.getResponse().getContentAsString();

        assertEquals(content, json);
    }

    @Test
    public void shouldCreateNote() throws Exception
    {
        Set<Note> notes = noteRepository.findByUser(USER);

        Note note = new Note();
        note.setKey(TEST_KEY_STRING);
        note.setValue(TEST_VALUE_STRING);
        note.setSubjectCategory(subjectCategoryRepository.findByName(TEST_CATEGORY));

        ObjectWriter ow = new ObjectMapper().writer();
        String jsonNote = ow.writeValueAsString(note);

        MvcResult res = mockMvc.perform(post("http://localhost:8080/api/v1/user-service/"+USERNAME+"/note")
                        .header("Authorization", "Bearer " + TOKEN)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonNote))
                .andExpect(status().isOk())
                .andReturn();

        note.setId(Long.valueOf(res.getResponse().getContentAsString()));
        notes.add(note);
        String jsonSet = ow.writeValueAsString(notes);

        res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/note").header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        String content = res.getResponse().getContentAsString();

        assertEquals(content, jsonSet);
    }

    @Test
    public void shouldUpdateNote() throws Exception
    {
        Note note = noteRepository.findByIdAndUser(TEST_ID, USER);
        note.setKey(TEST_UPDATE);

        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(note);

        mockMvc.perform(put("http://localhost:8080/api/v1/user-service/"+USERNAME+"/note")
                        .header("Authorization", "Bearer " + TOKEN)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().isOk());

        MvcResult res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/note/"+TEST_ID)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        String content = res.getResponse().getContentAsString();

        assertEquals(content, json);
    }

    @Test
    public void shouldDeleteNote() throws Exception
    {
        Set<Note> notes = noteRepository.findByUser(USER);
        Note note = noteRepository.findByIdAndUser(TEST_ID, USER);
        notes.removeIf(cur -> cur.getId().equals(note.getId()));

        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(notes);
        mockMvc.perform(delete("http://localhost:8080/api/v1/user-service/"+USERNAME+"/note/"+TEST_ID)
                .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk());

        MvcResult res = mockMvc.perform(get("http://localhost:8080/api/v1/user-service/"+USERNAME+"/note")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andReturn();
        String content = res.getResponse().getContentAsString();

        assertEquals(content, json);
    }
}
