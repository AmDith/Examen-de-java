package com.ism.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;

import com.ism.App;
import com.ism.core.Database.ClientRepoListInt;
import com.ism.entities.Client;
import com.ism.entities.Dette;
import com.ism.entities.User;
import com.ism.repositories.JPA.ClientRepoJpa;
import com.ism.service.ClientService;
import com.ism.service.ClientServiceInt;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class DetteClientController extends CoreController {
  @FXML
  private TableView<Dette> tableView;
  @FXML
  private TableColumn<Dette, LocalDate> dateColumn;
  @FXML
  private TableColumn<Dette, Double> montantColumn;
  @FXML
  private TableColumn<Dette, Double> verserColumn;
  @FXML
  private TableColumn<Dette, Double> restantColumn;
  @FXML
  private TableColumn<Dette, Void> actionsColumn;

  @FXML
  private Pagination pagination;
  
  private ClientServiceInt clientService;
  private ClientRepoListInt clientRepo;
  private String filterdettes = "all";
  public static Dette selectedDette;
  public static List<Dette> alldettes = new ArrayList<>();

  

  // Liste observable pour stocker les clients
  private ObservableList<Dette> clientList = FXCollections.observableArrayList();
  private static final int ROWS_PER_PAGE = 4; // Nombre de lignes par page

  public DetteClientController() {
      clientRepo = new ClientRepoJpa();
      clientService = new ClientService(clientRepo);
  }

  @FXML
  public void initialize() {
      // Configurer les colonnes de texte
      dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreate"));
      montantColumn.setCellValueFactory(new PropertyValueFactory<>("montant"));
      verserColumn.setCellValueFactory(new PropertyValueFactory<>("montantVerser"));
      restantColumn.setCellValueFactory(new PropertyValueFactory<>("montantRestant"));

      // Ajouter des boutons à la colonne "Actions"
      addButtonToTable();

      // Charger les données
      loadClientData();

      // Assigner la liste observable au TableView
      tableView.setItems(clientList);

      initPagination();
  }

  @FXML
  private void handleSoldButtonClick()  {
    filterdettes = "sold"; // Met à jour le filtre pour ne montrer que les utilisateurs
    loadClientData(); // Recharge les données avec le filtre
  }

  @FXML
  private void handleNoSoldButtonClick()  {
    filterdettes = "nosold"; // Remet le filtre à false pour afficher tous les clients
    loadClientData(); // Recharge toutes les données
  }

  @FXML
  private void handleAllSoldButtonClick()  {
    filterdettes = "all"; // Remet le filtre à false pour afficher tous les
    loadClientData(); // Recharge toutes les données
  }

  @FXML
  private void search() {
      loadClientData();
  }


  private void loadClientData() {
      List<Dette> filtreDette = new ArrayList<>();
      System.out.println(ConnexionController.userLogin);
      User user = ConnexionController.userLogin;
      System.out.println(user);
      Client client = clientService.searchUser(user);
      System.out.println(client);
      System.out.println("uhohhi");

      if (filterdettes.equals("sold")) {
        alldettes.stream()
          .peek(dette ->dette.setMontantRestant(dette.getMontant() - dette.getMontantVerser()))
          .filter(dette -> dette.getMontantRestant() == 0)
          .forEach(filtreDette::add);
      }
      else if (filterdettes.equals("nosold")) {
        alldettes.stream()
          .peek(dette ->dette.setMontantRestant(dette.getMontant() - dette.getMontantVerser()))
          .filter(dette -> dette.getMontantRestant() != 0)
          .forEach(filtreDette::add);
      }
    else if (ConnexionController.userLogin != null) {
        filtreDette = client.getDettes().stream()
            .peek(dette -> dette.setMontantRestant(dette.getMontant() - dette.getMontantVerser()))
            .collect(Collectors.toList());
            System.out.println(filtreDette);
      alldettes = filtreDette;
   }
    System.out.println("boss");
    clientList.setAll(filtreDette);
    System.out.println("c'est bon");
    System.out.println(clientList);
    initPagination(); // Recharge la pagination après chaque filtre
    pagination.setCurrentPageIndex(0); // Réinitialise l'index de page
  }
  

  private void initPagination() {
      int pageCount = (int) Math.ceil((double) clientList.size() / ROWS_PER_PAGE);
      pagination.setPageCount(pageCount);
      pagination.setCurrentPageIndex(0);

      // Définir l'action de changement de page
      pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> updateTableView(newIndex.intValue()));
      
      // Charger la première page
      updateTableView(0);
  }

  private void updateTableView(int pageIndex) {
      int start = pageIndex * ROWS_PER_PAGE;
      int end = Math.min(start + ROWS_PER_PAGE, clientList.size());
      ObservableList<Dette> clientsSubList = FXCollections.observableArrayList(clientList.subList(start, end));
      tableView.setItems(clientsSubList);
  }

  // Méthode pour ajouter un bouton à chaque ligne de la colonne "Actions"
  private void addButtonToTable() {
      Callback<TableColumn<Dette, Void>, TableCell<Dette, Void>> cellFactory = new Callback<>() {
          @Override
          public TableCell<Dette, Void> call(final TableColumn<Dette, Void> param) {
              return new TableCell<>() {

                  private final Button btn = new Button("Details");

                  {
                      btn.setOnAction((ActionEvent event) -> {
                        Dette dette = getTableView().getItems().get(getIndex());
                          try {
                              handleActionButton(dette);
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                      });
                  }

                  @Override
                  public void updateItem(Void item, boolean empty) {
                      super.updateItem(item, empty);
                      if (empty) {
                          setGraphic(null);
                      } else {
                          setGraphic(btn);
                      }
                  }
              };
          }
      };

      actionsColumn.setCellFactory(cellFactory);
  }

  // Action à exécuter lorsqu'on clique sur le bouton
  private void handleActionButton(Dette dette) throws IOException {
      selectedDette = dette;  // Stocke le client dans la variable statique
      System.out.println("selectedDette");
      System.out.println(selectedDette);
      if (ConnexionController.hip.compareTo("Boutiquier") == 0) {
        App.setRoot("DetailDette");
      }
      else if (ConnexionController.hip.compareTo("Admin") == 0) {
        App.setRoot("DetailDetteAdmin");
      }
      else{
        App.setRoot("DetailDetteClient");
      }
  }


  @FXML
  private void create() throws IOException {
      App.setRoot("createDette");
  }

}