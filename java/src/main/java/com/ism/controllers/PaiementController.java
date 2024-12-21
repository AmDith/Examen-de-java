package com.ism.controllers;

import java.io.IOException;

import com.ism.core.Database.DetteRepoListInt;
import com.ism.core.Database.PaiementRepoListInt;
import com.ism.entities.Dette;
import com.ism.entities.Paiement;
import com.ism.repositories.JPA.DetteRepoJpa;
import com.ism.repositories.JPA.PaiementRepoJpa;
import com.ism.service.DetteServceInt;
import com.ism.service.DetteService;
import com.ism.service.PaiementService;
import com.ism.service.PaiementServiceInt;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class PaiementController extends CoreController {

  @FXML
  private TextField qteVendu;

  @FXML
  private TextField idDette;

  @FXML
  private AnchorPane montantPane;

  private DetteRepoListInt  detteRepo;
  private DetteServceInt detteService;
  private PaiementRepoListInt paiementRepo;
  private PaiementServiceInt paiementService;


  private Dette dette;

  public PaiementController() {
      detteRepo = new DetteRepoJpa();
      detteService = new DetteService(detteRepo);
      paiementRepo = new PaiementRepoJpa();
      paiementService = new PaiementService(paiementRepo);
    }

//   public void restrictInputToInteger(TextField qte) {
//     qte.textProperty().addListener((observable, oldValue, newValue) -> {
//         if (!newValue.matches("\\d*")) { // Expression régulière pour les chiffres seulement
//             qte.setText(newValue.replaceAll("[^\\d]", "")); // Remplace les caractères non numériques
//         }
//     });
// }

  @FXML
  private void search() {
    dette = finDette();
    if (dette != null) {
      montantPane.setVisible(true);
      montantPane.setManaged(true);
  } else {
      showAlert("Erreur", "Aucune dette trouvée avec cet identifiant.");
      montantPane.setVisible(false);
      montantPane.setManaged(false);
  }
  }

  @FXML
  private void somme() throws IOException {
    Paiement paiement = new Paiement();
    Double montant;
    if (qteVendu.getText().isEmpty() || !qteVendu.getText().matches("\\d+")) {
      showAlert("Erreur de connexion", "Entrez des entiers.");
    }
    montant = Double.valueOf(qteVendu.getText());
    dette.setMontantRestant(dette.getMontant() - dette.getMontantVerser());
    if (montant <= 0 ||  montant >  dette.getMontantRestant() ) {
      showAlert("Erreur de connexion", "La somme doit être un montant positif et ne doit pas dépasser le montant restant .");
    } else { 
      dette.setMontantVerser(dette.getMontantVerser() + montant);
      dette.setMontantRestant(dette.getMontant() - dette.getMontantVerser());
        paiement.setMontant(montant);
        paiement.setDette(dette);
        paiementService.saveList(paiement);
        detteService.findData().update(dette);
        System.out.println("Il reste: " + dette.getMontantRestant() + "$");
  }
}

  private Dette finDette(){
    if (idDette.getText().isEmpty() || !idDette.getText().matches("\\d+")) {
      showAlert("Erreur de connexion", "Entrez des entiers.");
      return null;
    }
    Integer qteField = Integer.valueOf(idDette.getText());
    Dette dette =  detteService.findData().selectById(qteField);
    idDette.clear();
    qteVendu.clear();
    return dette;

  }

   












}
