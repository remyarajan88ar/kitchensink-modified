package org.quickstarts.kitchensink.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quickstarts.kitchensink.Controller.MemberController;
import org.quickstarts.kitchensink.data.MemberListProducer;
import org.quickstarts.kitchensink.data.MemberRepository;
import org.quickstarts.kitchensink.model.Member;
import org.quickstarts.kitchensink.service.MemberRegistration;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.ArrayList;
import java.util.List;

public class MemberControllerTest {

    @Mock
    private MemberRegistration memberRegistration;

    @Mock
    private MemberListProducer memberListProducer;

    @Mock
    Model model;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    private MemberController memberController = new MemberController();

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        memberController.initNewMember();
    }

    @Test
    void getAllMembersTest(){
        Mockito.when(memberListProducer.getMembers()).thenReturn(new ArrayList<>());
        String response =memberController.getAllMembers(model);
        Assertions.assertEquals("index", response);

    }

    @ParameterizedTest
    @CsvSource({"false,redirect:list","true,index"})
    void addMemberTest(boolean result, String response){
        Mockito.when(bindingResult.hasFieldErrors(Mockito.anyString())).thenReturn(result);
        String redirect =memberController.addMember(new Member(),bindingResult, model);
        Assertions.assertEquals(response, redirect);
    }

    @Test
    void registerTest(){
        memberController.register(new Member());
        Mockito.verify(memberRegistration, Mockito.times(1)).register(Mockito.any());
    }




}
