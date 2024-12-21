package com.ism.repositories.JPA;


import com.ism.core.Database.DetteRepoListInt;
import com.ism.repositories.Impl.RepositoryJpaImpl;
import com.ism.entities.Dette;

public class DetteRepoJpa extends RepositoryJpaImpl<Dette> implements DetteRepoListInt{

  public DetteRepoJpa() {
    super(Dette.class);
    table = "Dette";
  }

  
  
}
