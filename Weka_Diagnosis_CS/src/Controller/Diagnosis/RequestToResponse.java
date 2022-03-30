package Controller.Diagnosis;

import Model.Request.Patient;
import Model.Request.Request;
import Model.Response.Diagnosis;
import Model.Response.Response;

import java.util.ArrayList;

public class RequestToResponse {
    // This class takes in a request instance, do Controller.Weka prediction, and returns a response instance.
    private Request request;
    private Response response;

    public RequestToResponse(Request request) throws Exception {
        this.request = request;
        response = new Response();
    }
    public void run() throws Exception {

        String arffFileName = request.getArffFileName();
        request.writeARFF();
        Predict2 p = new Predict2("breast-cancer.arff", arffFileName); // do weka prediction
        p.Run();

        // initialize response and assign values
        response.setRequestID(request.getRequestID());
        response.setReceiverIPAddress(request.getSenderIPAddress());
        response.setSenderIPAddress(request.getReceiverIPAddress());
        ArrayList<Patient> patientArrayList = request.getPatientArrayList();
        ArrayList<String> predictions = p.getPredictions();
        ArrayList<Diagnosis> diagnosisArrayList = new ArrayList<Diagnosis>();
        for (int i = 0; i < patientArrayList.size(); i++) {
            Diagnosis diagnosis = new Diagnosis();
            diagnosis.setPatientID(patientArrayList.get(i).getPatientID());
            diagnosis.setOpinion(predictions.get(i));
            diagnosisArrayList.add(diagnosis);
        }
        response.setDiagnosisArrayList(diagnosisArrayList);
    }

    public Response getResponse() {
        return response;
    }
}
