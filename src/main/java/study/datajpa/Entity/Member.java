package study.datajpa.Entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    private Long id;
    private String name;


    protected Member() {

    }

    public Member(String name) {
        this.name = name;
    }
}
