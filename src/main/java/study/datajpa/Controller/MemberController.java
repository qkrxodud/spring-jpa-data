package study.datajpa.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import study.datajpa.Entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUserName();
    }

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        Member findMember = memberRepository.findById(member.getId()).get();
        return findMember.getUserName();
    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable) {
        //PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC, "userName"));
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @GetMapping(value = "/members_page")
    public Page<Member> list2(@PageableDefault(size = 12, sort = "userName",
                        direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
        return page;
    }

    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
        memberRepository.save(new Member("userB"));
        memberRepository.save(new Member("userC"));
        memberRepository.save(new Member("userD"));
    }
}
