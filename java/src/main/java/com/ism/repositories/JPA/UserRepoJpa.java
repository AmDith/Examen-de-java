package com.ism.repositories.JPA;

import com.ism.core.Database.UserRepoListInt;
import com.ism.repositories.Impl.RepositoryJpaImpl;
import com.ism.entities.User;

public class UserRepoJpa extends RepositoryJpaImpl<User> implements UserRepoListInt{

  public UserRepoJpa(){
    super(User.class);
    table = "User";
  }

  @Override
  public String login(String val) {
    datas = selectAll();
    boolean isUnique = datas.stream()
                            .noneMatch(user -> user.getLogin().equals(val));
    return isUnique ? val : null;
  }

  @Override
  public User selectForUser(String val,String val1,String val2) {
    try {
      em.getTransaction().begin();
      data= this.em.createNamedQuery(val1, User.class)
      .setParameter(val2, val)
      .getSingleResult();    
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
      e.printStackTrace();
    }
    return data;
  }

  @Override
  public User selectByLogin(String val) {
    try {
      em.getTransaction().begin();
      data= this.em.createNamedQuery("SelectByLogin", User.class)
      .setParameter("login", val)
      .getSingleResult();    
      em.getTransaction().commit();
    } catch (Exception e) {
      em.getTransaction().rollback();
      e.printStackTrace();
    }
    System.out.println(data);
    return data;
  }
 
  
}
