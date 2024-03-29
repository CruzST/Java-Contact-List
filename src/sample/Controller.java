package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import sample.datamodel.Contact;
import sample.datamodel.ContactData;

import java.io.IOException;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane mainPanel;

    @FXML
    private TableView<Contact> contactsTable;

    private ContactData data;

    public void initialize(){
        data = new ContactData();
        data.loadContacts();
        contactsTable.setItems(data.getContacts());
    }

    @FXML
    public void showAddContactDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Add a new Contact");

        FXMLLoader fxmlLoader =new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result =  dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            ContactController contactController = fxmlLoader.getController();
            Contact newContact = contactController.returnContact();

            if (newContact == null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not all fields filled in");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all the fields");
                alert.showAndWait();
                return;
            }

            data.addContact(newContact);
            data.saveContacts();
        }
    }

    @FXML
    public void showEditContactDialog(){
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if(selectedContact == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the Contact you want to edit");
            alert.showAndWait();
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Edit Contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("contactDialog.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e){
            System.out.println("Couldn't Load Dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ContactController contactController = fxmlLoader.getController();
        contactController.editContact(selectedContact);

        Optional<ButtonType> result =  dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            contactController.updateContact(selectedContact);
            data.saveContacts();
        }
    }

    @FXML
    public void deleteContact(){
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if(selectedContact == null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No contact selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you want to delete");
            alert.showAndWait();
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete contact: " + selectedContact.getFirstName() + " " + selectedContact.getLastName());

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            data.deleteContact(selectedContact);
            data.saveContacts();
        }
    }
}
