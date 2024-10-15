package org.quickstarts.kitchensink.service;

import jakarta.inject.Inject;
import org.quickstarts.kitchensink.data.MemberEvent;
import org.quickstarts.kitchensink.data.MemberRepository;
import jakarta.enterprise.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MemberRegistration {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Transactional
    public void register(Member member) {
        log.info("Registering {}", member.getName());
        memberRepository.save(member);
        eventPublisher.publishEvent(new MemberEvent(this,member));
    }
}
