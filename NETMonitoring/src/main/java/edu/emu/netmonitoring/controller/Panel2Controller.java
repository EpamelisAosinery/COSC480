package edu.emu.netmonitoring.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.pcap4j.core.*;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;

public class Panel2Controller {

    public void initialize() {
        this.cInterfaceName.setCellValueFactory(new PropertyValueFactory<>("interfaceName"));
        this.cDisplayName.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        this.cIPAddress.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        this.cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.cIPType.setCellValueFactory(new PropertyValueFactory<>("IPType"));

        this.cAction.setCellFactory(col -> {
            Button exportButton = new Button("Export");
            TableCell<NetworkInterfaceInfo, String> cell = new TableCell<NetworkInterfaceInfo, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(exportButton);
                    }
                }
            };

            exportButton.setOnAction(event -> {
                NetworkInterfaceInfo info = cell.getTableRow().getItem();
                if (info != null) {
                    exportAction(info);
                }
            });

            return cell;
        });

        this.tableView.setItems(this.getNetworkInterfaces());
    }

    private void exportAction(NetworkInterfaceInfo info) {
        System.out.println("Button Pressed");
    }


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

    @FXML
    private TableView<NetworkInterfaceInfo> tableView;
    @FXML
    private TableColumn<NetworkInterfaceInfo, String> cInterfaceName, cDisplayName, cIPAddress, cStatus, cIPType, cAction;

    private ObservableList<NetworkInterfaceInfo> getNetworkInterfaces() {

        ObservableList<NetworkInterfaceInfo> networkInterfaces = FXCollections.observableArrayList();

        try {

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()) {
                NetworkInterface individualInterface = interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = individualInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress theAddress = inetAddresses.nextElement();
                    networkInterfaces.add(new NetworkInterfaceInfo(individualInterface.getName(), individualInterface.getDisplayName(), theAddress.getHostAddress(), individualInterface.isUp() ? "Up" : "Down"));
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }


        return networkInterfaces;
    }


}
