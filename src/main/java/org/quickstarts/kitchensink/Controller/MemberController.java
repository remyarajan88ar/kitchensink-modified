package org.quickstarts.kitchensink.Controller;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.quickstarts.kitchensink.data.MemberListProducer;
import org.quickstarts.kitchensink.model.Member;
import org.quickstarts.kitchensink.service.MemberRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberRegistration memberRegistration;

    @Autowired
    private MemberListProducer memberListProducer;

    private Member newMember;

    private List<Member> memberList;

    @GetMapping("")
    public String getPage(Model model){
        model.addAttribute("newMember", newMember);
        return "index";
    }

    @GetMapping("/list")
    public String getAllMembers(Model model) {
        memberList = memberListProducer.getMembers();
        model.addAttribute("members", memberList);
        model.addAttribute("newMember", newMember);
        return "index";
    }

    @PostMapping("/add")
    public String addMember(@Valid @ModelAttribute Member newMember, BindingResult result, Model model) {
        if (result.hasFieldErrors("name")
                || result.hasFieldErrors("email")
                || result.hasFieldErrors("phoneNumber")) {
            return "index";
        }
        register(newMember);
        return "redirect:list";
    }

    @PostConstruct
    public void initNewMember() {
        newMember = new Member();
    }

    public void register(Member newMember) {
        try {
            memberRegistration.register(newMember);
            initNewMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            log.error("error: {}", errorMessage);
        }
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }


}
