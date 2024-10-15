package org.quickstarts.kitchensink.data;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.Reception;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;
import org.quickstarts.kitchensink.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@Component
public class MemberListProducer {

    @Autowired
    private MemberRepository memberRepository;

    private List<Member> members;

    public List<Member> getMembers() {
        return members;
    }

    @EventListener
    public void onMemberListChanged(MemberEvent memberEvent) {
        retrieveAllMembersOrderedByName();
    }

    @PostConstruct
    public void retrieveAllMembersOrderedByName() {
        members = memberRepository.findAllByOrderByNameAsc();
    }

}
