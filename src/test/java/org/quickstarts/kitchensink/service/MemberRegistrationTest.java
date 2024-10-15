package org.quickstarts.kitchensink.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quickstarts.kitchensink.data.MemberRepository;
import org.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public class MemberRegistrationTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private MemberRegistration memberRegistration = new MemberRegistration();

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerTest(){
        Member member = new Member(1L, "John","email","12345");
        memberRegistration.register(member);
        Mockito.verify(memberRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(eventPublisher, Mockito.times(1)).publishEvent(Mockito.any());
    }
}
