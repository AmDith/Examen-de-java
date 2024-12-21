package com.ism.repositories.JPA;

import com.ism.core.Database.PaiementRepoListInt;
import com.ism.repositories.Impl.RepositoryJpaImpl;
import com.ism.entities.Paiement;

public class PaiementRepoJpa extends RepositoryJpaImpl<Paiement> implements PaiementRepoListInt {

  public PaiementRepoJpa() {
    super(Paiement.class);
    table = "Paiement";
  }
  
}
