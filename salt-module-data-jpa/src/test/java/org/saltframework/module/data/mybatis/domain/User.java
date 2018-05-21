package org.saltframework.module.data.mybatis.domain;

import java.io.Serializable;

import javax.persistence.*;

import lombok.*;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @since 2018. 4. 13.
 */
@Entity
@Table(name = "USER")
@Getter
@Setter(value = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

//  @Column(name = "USER_ID")
//  private String userId;

  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;

  @Column(name = "ADDRESS")
  private String address;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "USER_ID")
  private Member member;

  public User() {
  }

  public User(String phoneNumber, String address, Member member) {
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.member = member;
  }
}
