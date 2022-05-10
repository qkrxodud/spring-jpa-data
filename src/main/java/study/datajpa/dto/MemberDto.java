package study.datajpa.dto;

import lombok.Data;
import study.datajpa.Entity.Member;

@Data
public class MemberDto {
    private Long id;
    private String userName;
    private String teamName;

    public MemberDto(Long id, String userName, String teamName) {
        this.id = id;
        this.userName = userName;
        this.teamName = teamName;
    }
    public MemberDto(Member m) {
        id = m.getId();
        userName = m.getUserName();
    }
}
