package edu.emu.netmonitoring.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.pcap4j.core.*;
import org.pcap4j.util.NifSelector;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class Export {

    @FXML
    private Button selectNicButton; // Assuming FXML ID assignment

    @FXML
    private Button saveAsPcapButton; // Assuming FXML ID assignment

    private PcapHandle handle;
    private PcapDumper dumper;

    // JavaFX initialization method
    public void initialize() {
        // Add action listeners to buttons
        selectNicButton.setOnAction(event -> selectNIC());
        saveAsPcapButton.setOnAction(event -> saveAsPcap());
    }

    @FXML
    private void selectNIC() {
        try {
            PcapNetworkInterface device = new NifSelector().selectNetworkInterface();
            if (device == null) {
                System.out.println("No device selected.");
                return; // No device selected
            }
            // Open the device and start capturing
            handle = device.openLive(65536, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, 10);
            System.out.println("Device selected and capturing started: " + device.getName());
            // Here you might want to start a new thread to handle capturing if you wish to capture continuously
        } catch (PcapNativeException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveAsPcap() {
        if (handle != null) {
            FileChooser fileChooser = new FileChooser();
            // Set a title
            fileChooser.setTitle("Save as .pcap File");
            // Set a default name
            String defaultFilename = "network_capture_" + new Date().getTime() + ".pcap";
            fileChooser.setInitialFileName(defaultFilename);
            // Set filetype filter
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PCAP Files", "*.pcap")
            );
            // popup a fileChooser windows to save your .pcap file
            File file = fileChooser.showSaveDialog(null);

            if (file != null) {
                try {
                    // Save a file base on your selection
                    String filename = ((File) file).getAbsolutePath();
                    dumper = handle.dumpOpen(filename);
                    handle.loop(-1, dumper);
                    dumper.close();
                    handle.close();
                    System.out.println("Capture saved as: " + filename);
                } catch (PcapNativeException | NotOpenException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No capture to save.");
        }
    }
}
