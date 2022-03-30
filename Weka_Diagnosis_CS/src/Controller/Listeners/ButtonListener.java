package Controller.Listeners;

import Controller.Main;
import Controller.Diagnosis.RequestToResponse;
import Controller.P2P.DataClient;
import Controller.XMLConvertors.RequestXMLGenerator;
import Controller.XMLConvertors.ResponseXMLStringParser;
import Model.Request.Patient;
import Model.Request.Request;
import Model.Response.Response;
import View.MyWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class ButtonListener implements ActionListener {

    private static boolean isNewRequest;

    private MyWindow win;
    private JTextArea display;
    private JComboBox<String>[] patientInfoBoxes;
    private JButton newPatientButton;
    private JButton loadButton;
    private JButton predictButton;
    private JButton secondOpinionButton;
    private JButton clearButton;
    private Request request;
    private Patient patient;

    public ButtonListener(MyWindow win) {
        this.win = win;
        this.display = win.getDisplay();
        this.patientInfoBoxes = win.getPatientInfoBoxes();
        this.newPatientButton = win.getNewPatientButton();
        this.loadButton = win.getLoadButton();
        this.predictButton = win.getPredictButton();
        this.secondOpinionButton = win.getSecondOpinionButton();
        this.clearButton = win.getClearButton();
        this.request = Main.request;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ArrayList<Patient> patientArrayList = request.getPatientArrayList();
        isNewRequest = true;

        // Set up new-patient button listener
        if (event.getSource() == newPatientButton) {
            patient = new Patient();
            //request.getPatientArrayList().add(patient);
            //patient = patientArrayList.get(patientArrayList.size()-1);
            for (int i = 0; i < patientInfoBoxes.length; i++) {
                patientInfoBoxes[i].setEnabled(true);
                patientInfoBoxes[i].setSelectedIndex(0);
            }
        }

        // Set up load-patient button listener
        if (event.getSource() == loadButton) {
            isNewRequest = false;
            if (patient.isComplete()) {
                request.getPatientArrayList().add(patient);
                for (int i = 0; i < patientInfoBoxes.length; i++) {
                    patientInfoBoxes[i].setEnabled(false);
                }
                String m = display.getText();
                String newMessage = String.format("%s\n%s", m, patient);
                display.setText(newMessage);
            }
            else {
                String m = display.getText();
                String newMessage = String.format("%s\n%s", m, "Missing patient information! ");
                display.setText(newMessage);
            }
        }

        // Set up Weka-prediction button listener
        if (event.getSource() == predictButton) {
            System.out.println(request);
            RequestXMLGenerator requestXMLGenerator = new RequestXMLGenerator(request);
            try {
                requestXMLGenerator.writeXML("newRequest.xml");

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(requestXMLGenerator.getXMLString());
            //PatientARFF patientARFF = new PatientARFF();
            //patientARFF.addPatient(patient);
            try {
                RequestToResponse requestToResponse = new RequestToResponse(request);
                requestToResponse.run();
                Response response = requestToResponse.getResponse();
                System.out.println(response);
                String m = display.getText();
                String newMessage = String.format("%s\n%s", m, response.getDiagnosisResults());
                display.setText(newMessage);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // Set up clear-input button listener
        if (event.getSource() == clearButton) {
            request = new Request();
            isNewRequest = true;
            display.setText("History record cleared. \n");
        }

        // Set up ComboBox listeners
        if (event.getSource() == patientInfoBoxes[0]) {
            patient.setAge(String.valueOf(patientInfoBoxes[0].getSelectedItem()));
          //  System.out.println("age  = " + patientInfoBoxes[0].getSelectedItem());
        }
        if (event.getSource() == patientInfoBoxes[1]) {
            patient.setMenopause(String.valueOf(patientInfoBoxes[1].getSelectedItem()));
           // System.out.println("menopause  = " + patientInfoBoxes[1].getSelectedItem());
        }
        if (event.getSource() == patientInfoBoxes[2]) {
            patient.setTumor_size(String.valueOf(patientInfoBoxes[2].getSelectedItem()));
           // System.out.println("tumor-size  = " + patientInfoBoxes[2].getSelectedItem());
        }
        if (event.getSource() == patientInfoBoxes[3]) {
            patient.setInv_nodes(String.valueOf(patientInfoBoxes[3].getSelectedItem()));
           // System.out.println("inv-nodes  = " + patientInfoBoxes[3].getSelectedItem());
        }
        if (event.getSource() == patientInfoBoxes[4]) {
            patient.setNode_caps(String.valueOf(patientInfoBoxes[4].getSelectedItem()));
           // System.out.println("node-caps  = " + patientInfoBoxes[4].getSelectedItem());
        }
        if (event.getSource() == patientInfoBoxes[5]) {
            patient.setDeg_malig(String.valueOf(patientInfoBoxes[5].getSelectedItem()));
           // System.out.println("deg-malig  = " + patientInfoBoxes[5].getSelectedItem());
        }
        if (event.getSource() == patientInfoBoxes[6]) {
            patient.setBreast(String.valueOf(patientInfoBoxes[6].getSelectedItem()));
          //  System.out.println("breast  = " + patientInfoBoxes[6].getSelectedItem());
        }
        if (event.getSource() == patientInfoBoxes[7]) {
            patient.setBreast_quad(String.valueOf(patientInfoBoxes[7].getSelectedItem()));
           // System.out.println("breast-quad  = " + patientInfoBoxes[7].getSelectedItem());
        }
        if (event.getSource() == patientInfoBoxes[8]) {
            patient.setIrradiat(String.valueOf(patientInfoBoxes[8].getSelectedItem()));
           // System.out.println("irradiat  = " + patientInfoBoxes[8].getSelectedItem());
        }
        //if (event.getSource() == patientInfoBoxes[9]) {
        //    patient.setClassi(String.valueOf(patientInfoBoxes[9].getSelectedItem()));
        //    System.out.println("Class  = " + patientInfoBoxes[9].getSelectedItem());
        //}

        // Set up "Get second opinion" button listener
        if (event.getSource() == secondOpinionButton) {
            //if (isNewRequest) {
            //    String m = display.getText();
            //    String newMessage = String.format("%s\n%s", m, "No patient information yet!");
            //    display.setText(newMessage);
            //}
            //else {
                String responseXMLString = "";
                Response response = new Response();
                RequestXMLGenerator requestXMLGenerator = new RequestXMLGenerator(request);
                String RequestXMLString = requestXMLGenerator.getXMLString();
                try {
                    DataClient dataClient = new DataClient(20000);
                    dataClient.run(RequestXMLString);
                    responseXMLString = dataClient.getResponseXMLString();
                    ResponseXMLStringParser responseXMLStringParser = new ResponseXMLStringParser(responseXMLString);
                    response = responseXMLStringParser.getResponse();
                } catch (Exception e) {
                    e.printStackTrace();
                } 
            String m = display.getText();
                String newMessage = String.format("%s\n%s\n%s", m, "Second opinion:",
                        response.getDiagnosisResults());
                display.setText(newMessage);
            //}
        }
    }

}
