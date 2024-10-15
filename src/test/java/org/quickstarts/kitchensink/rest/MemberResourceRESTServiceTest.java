package org.quickstarts.kitchensink.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.quickstarts.kitchensink.data.MemberRepository;
import org.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberResourceRESTServiceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private MemberRepository memberRepository;

    @BeforeEach
    public void init() {
        Mockito.when(memberRepository.findAllByOrderByNameAsc()).thenReturn(Arrays.asList(new Member()));
        Mockito.when(memberRepository.findByEmail(Mockito.any())).thenReturn(Optional.empty());
    }

    @Test
    void listAllMembersTest() throws Exception {
        mockMvc.perform(get("/members")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void lookupByMemberIdTest() throws Exception {
        mockMvc.perform(get("/members/1")).andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void createMemberTest() throws Exception {
        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "        \"id\": 0,\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"email\": \"john.smith@mailinator.com\",\n" +
                "        \"phoneNumber\": \"2125551212\"\n" +
                "    }")).andDo(print()).andExpect(status().isOk());
    }

    @Test
    void createMemberConstraintViolationExceptionTest() throws Exception {
        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "        \"id\": 0,\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"email\": \"john.smith\",\n" +
                "        \"phoneNumber\": \"21255\"\n" +
                "    }")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void createMemberValidationExceptionTest() throws Exception {
        Mockito.when(memberRepository.findByEmail(Mockito.any())).thenReturn(Optional.of(new Member()));
        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "        \"id\": 0,\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"email\": \"john.smith@mailinator.com\",\n" +
                "        \"phoneNumber\": \"2125551212\"\n" +
                "    }")).andDo(print()).andExpect(status().isConflict());
    }

    @Test
    void createMemberExceptionTest() throws Exception {
        Mockito.doThrow(RuntimeException.class).when(memberRepository).save(Mockito.any());
        mockMvc.perform(post("/members").contentType(MediaType.APPLICATION_JSON).content("{\n" +
                "        \"id\": 0,\n" +
                "        \"name\": \"John Smith\",\n" +
                "        \"email\": \"john.smith@mailinator.com\",\n" +
                "        \"phoneNumber\": \"2125551212\"\n" +
                "    }")).andDo(print()).andExpect(status().isBadRequest());
    }


}
