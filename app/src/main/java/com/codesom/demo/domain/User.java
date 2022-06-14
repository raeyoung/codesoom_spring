package com.codesom.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
// User라는 예약어 때문에 Table 명을 추가해줘야함
@Table(name="Users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;

    private String password;

    private boolean deleted = false;

    public void changeWith(User source) {
        name = source.getName();
        password = source.getPassword();
    }

    public void destroy() {
        deleted = true;
    }
}
