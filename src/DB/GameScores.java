package DB;

import java.sql.ResultSet;

/**
 * GameScores
 * gets average scores from data base
 */
public class GameScores {
    DBConnection dataBase;

    /**
     * [Constructor] <br>
     * @param db - data base of game
     */
    public GameScores(DBConnection db){
        this.dataBase = db;
    }

    /**
     * averageScoreForAllScenarios gets average score of all scenarios
     * @return Average score
     */
    public double averageScoreForAllScenarios(){
        return averageScore("SELECT AVG(Point) FROM logs;");
    }

    /**
     * averageScoreForAllScenario gets average score of all scenarios by id
     * @param id int id of user.
     * @return Average of score
     */
    public double averageScoreForAllScenarios(int id){
        return averageScore("SELECT AVG(Point) FROM logs WHERE FirstID="+id+" OR SecondID="+id+" OR ThirdID="+id+";");
    }

    /**
     * averageScoreForScenario gets average score by scenario
     * @param scenario int hash of scenario
     * @return Average of score
     */
    public double averageScoreForScenario(int scenario){
        return averageScore("SELECT AVG(Point) FROM logs WHERE SomeDouble="+scenario+";");
    }

    /**
     * averageScoreForScenario gets average score by id and scenario
     * @param scenario int hash of scenario
     * @param id int id of user.
     * @return Average of score
     */
    public double averageScoreForScenario(int scenario,int id){
        return averageScore("SELECT AVG(Point) FROM logs WHERE SomeDouble="+scenario+" AND ( FirstID="+id+" OR SecondID="+id+" OR ThirdID="+id+");");
    }

    /**
     * averageScore send statement to DB
     * @param statement String statement.
     * @return Average score
     */
    private double averageScore(String statement){
        double avg = Double.MAX_VALUE;
        try {
            if (dataBase.connectToDB()) {

                ResultSet resultSet = dataBase.getDataFromDB(statement);

                if (resultSet.next()) {
                    avg = resultSet.getFloat(1);
                }
                if (!dataBase.disconnectFromDB())
                    System.out.println("Failed to disconnect from server...");
            } else System.out.println("Failed to connect to server...");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Crashed");
        }
        return avg;
    }
}
