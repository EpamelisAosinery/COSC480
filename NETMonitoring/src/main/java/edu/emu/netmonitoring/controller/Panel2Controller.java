package edu.emu.netmonitoring.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class Panel2Controller {

    @FXML
    private TableView<NetworkInterfaceInfo> tableView;
    @FXML
    private TableColumn<NetworkInterfaceInfo, String> cInterfaceName, cDisplayName, cIPAddress, cStatus, cIPType, cAction;

    // There are javafx components in this file that are linked with an fxml file
    // initialize is called to build / populate a table utilizing that layout
    public void initialize() {
        this.cInterfaceName.setCellValueFactory(new PropertyValueFactory<>("interfaceName"));
        this.cDisplayName.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        this.cIPAddress.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        this.cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.cIPType.setCellValueFactory(new PropertyValueFactory<>("IPType"));

        // to create a button for each row in the action column
        this.cAction.setCellFactory(col -> {
            // Create a button to be used
            Button exportButton = new Button("Export");
            // the cell we will be referencing
            TableCell<NetworkInterfaceInfo, String> cell = new TableCell<NetworkInterfaceInfo, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    // here we say .... is the cell empty? if it is put a button in it
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(exportButton);
                    }
                }
            };
            // a simple event handler if the button is clicked
            // perform method exportAction()
            exportButton.setOnAction(event -> {
                NetworkInterfaceInfo info = cell.getTableRow().getItem();
                if (info != null) {
                    exportAction(info);
                }
            });

            return cell;
        });
        // populate the table with the appropriate information
        this.tableView.setItems(this.getNetworkInterfaces());
    }

    // the method called when export button is clicked. Needs to be implemented
    private void exportAction(NetworkInterfaceInfo info) {
        System.out.println("Button Pressed");
    }

    // the core of the class. This is the Network Interface Information
    public static class NetworkInterfaceInfo {

        private String cInterfaceName;
        private String cDisplayName;
        private String cIPAddress;
        private String cStatus;

        public NetworkInterfaceInfo(String cInterfaceName, String cDisplayName, String cIPAddress, String cStatus) {
            this.cInterfaceName = cInterfaceName;
            this.cDisplayName = cDisplayName;
            this.cIPAddress = cIPAddress;
            this.cStatus = cStatus;
        }

        public String getInterfaceName() {
            return cInterfaceName;
        }

        public String getDisplayName() {
            return cDisplayName;
        }

        public String getIpAddress() {
            return cIPAddress;
        }

        public String getStatus() {
            return cStatus;
        }

        // created a hack of how to tell if a network was ipv4 or ipv6
        public String getIPType() {

            if (getIpAddress().contains(":")) {

                return "IPv6";
            } else if (getIpAddress().contains(".")) {
                return "IPv4";
            } else {
                return "Unknown";
            }

        }


    }


    // really the whole thing. Collections the Information about all the network interfaces
    private ObservableList<NetworkInterfaceInfo> getNetworkInterfaces() {
        // a place to store all the info
        ObservableList<NetworkInterfaceInfo> networkInterfaces = FXCollections.observableArrayList();

        try {
            // try to run through all network interfaces on a local machine
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            // loop through them... while theres another network interface
            while (interfaces.hasMoreElements()) {
                // snag all of the addresses assigned to it
                NetworkInterface individualInterface = interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = individualInterface.getInetAddresses();
                // while there are addresses to go through
                while (inetAddresses.hasMoreElements()) {
                    // add a new instance of that class to the list
                    // snag its info
                    InetAddress theAddress = inetAddresses.nextElement();
                    networkInterfaces.add(new NetworkInterfaceInfo(individualInterface.getName(), individualInterface.getDisplayName(), theAddress.getHostAddress(), individualInterface.isUp() ? "Up" : "Down"));
                }
            }
            // its a try an catch ... it needs a catch
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        // return all the info
        return networkInterfaces;
    }


}
