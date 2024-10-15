package org.quickstarts.kitchensink.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quickstarts.kitchensink.model.Member;

import java.util.Arrays;

public class MemberListProducerTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberEvent memberEvent;

    @InjectMocks
    private MemberListProducer memberListProducer = new MemberListProducer();

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        Member member = new Member(1L, "John","email","12345");
        Mockito.when(memberRepository.findAllByOrderByNameAsc()).thenReturn(Arrays.asList(member));
    }

    @Test
    void onMemberListChanged(){
        memberListProducer.onMemberListChanged(memberEvent);
        Assertions.assertEquals(1, memberListProducer.getMembers().size());
    }

}
