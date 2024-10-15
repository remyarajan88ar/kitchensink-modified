package org.quickstarts.kitchensink.data;

import lombok.Getter;
import org.quickstarts.kitchensink.model.Member;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberEvent extends ApplicationEvent {
    private Member member;
    public MemberEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }
}
