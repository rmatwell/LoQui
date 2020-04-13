package edu.odu.cs411.loqui;

public class DailyData
{
    int scoreDay, scoreMonth, scoreYear;
    double score;

    DailyData(double newScore, int newScoreDay, int newScoreMonth, int newScoreYear)
    {
        score = newScore;
        scoreDay = newScoreDay;
        scoreMonth = newScoreMonth;
        scoreYear = newScoreYear;
    }

    DailyData()
    {

    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public int getScoreDay()
    {
        return scoreDay;
    }

    public void setScoreDay(int scoreDay)
    {
        this.scoreDay = scoreDay;
    }

    public int getScoreMonth()
    {
        return scoreMonth;
    }

    public void setScoreMonth(int scoreMonth)
    {
        this.scoreMonth = scoreMonth;
    }

    public int getScoreYear()
    {
        return scoreYear;
    }

    public void setScoreYear(int scoreYear)
    {
        this.scoreYear = scoreYear;
    }
}
