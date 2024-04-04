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

    public void initialize() {
        cInterfaceName.setCellValueFactory(new PropertyValueFactory<>("interfaceName"));
        cDisplayName.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        cIPAddress.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        cIPType.setCellValueFactory(new PropertyValueFactory<>("IPType"));

        tableView.setItems(getNetworkInterfaces());
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
