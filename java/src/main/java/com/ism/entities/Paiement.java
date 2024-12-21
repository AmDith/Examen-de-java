package com.ism.entities;



import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString (exclude = {"dette"})
@Entity
@Table(name = "paiement")


public class Paiement extends AbstractEntity{
  private Double montant;
  @ManyToOne
  @JoinColumn
  private Dette dette;
}
