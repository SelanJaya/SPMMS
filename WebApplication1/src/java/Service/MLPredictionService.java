/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.ProjectDAO;
import Service.DashboardInsightsService;
import beans.DashboardInsight;
import beans.ProjectRiskScore_ML;
import beans.RiskPredictionResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author HP
 */
public class MLPredictionService {
    
    
    public List ProjectRisk_MLService() throws Exception {
        Map<Integer, ProjectRiskScore_ML> projectMap = new HashMap<>();

        DashboardInsight dashboardInsights = new DashboardInsight();
        try {
            ProjectDAO projectDAO = new ProjectDAO();

            //OverduePercentage
            projectMap = projectDAO.getOverduePercentageAllProject();
            projectMap = projectDAO.getTaskRejectionRateAllProject(projectMap);
            projectMap = projectDAO.getAVGStoryPointAllProject(projectMap);
            projectMap = projectDAO.getRemainingDateAllProject(projectMap);
            projectMap = projectDAO.getCompletionRateAllProject(projectMap);

            List<RiskPredictionResponse> predictions = new ArrayList<>();

            MLPredictionService mLPredictionService = new MLPredictionService();
            predictions = mLPredictionService.predictRisk(new ArrayList(projectMap.values()));
            System.out.println("Data : " + predictions.getFirst().getProjectId() + " " + predictions.getFirst().getRiskScore());
            projectDAO.updateProjectRiskScores(predictions);
            return predictions;
        } catch (Exception e) {
            System.out.println("Exception occured at ProjectRisk_MLService : " + e);
            throw e;
        }
    }

    public List<RiskPredictionResponse> predictRisk(List<ProjectRiskScore_ML> projects) throws Exception {
        long start = System.currentTimeMillis();
        Gson gson = new Gson();

        // add the data into json file
        String json = gson.toJson(projects);
        System.out.println(json);

        URL url = new URL("http://localhost:5000/predict-batch");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            os.write(json.getBytes("UTF-8"));
        }
        
        StringBuffer response;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {

            response = new StringBuffer();

            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
        }
        
        System.out.println(response.toString());

        String jsonResponse = response.toString();

        Type listType = new TypeToken<List<RiskPredictionResponse>>() {
        }.getType();

        List<RiskPredictionResponse> predictions = gson.fromJson(jsonResponse, listType);
         System.out.println("Before" + predictions.getFirst().getProjectId() + " " + predictions.getFirst().getRiskScore());
        return predictions;
    }

}
