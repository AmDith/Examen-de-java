package com.ism.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn; 
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import com.ism.enums.Etat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"client"})
@EqualsAndHashCode(callSuper = false , of = {"login"}) 
@Entity
@Table(name = "user_entity")
@NamedQueries({
  @NamedQuery(name ="SelectByLogin", query = "SELECT u FROM User u WHERE u.login = :login"),
  @NamedQuery(name ="SelectByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
  @NamedQuery(name ="SelectByPassword", query = "SELECT u FROM User u WHERE u.password = :password")
})


public class User extends  AbstractEntity{

  @Column(length = 25, unique = true)
  private String login;
  @Column(length = 25,unique = true)
  private String email;
  @Column(length = 25,unique = true)
  private String password;
  @Enumerated(EnumType.STRING)
  private Etat etat;
  //Navigabilité
  @OneToOne
  @JoinColumn
  private Role role;

  //Navigabilité
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @JoinColumn
  private Client client;

  
  
}
