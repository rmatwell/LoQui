package edu.odu.cs411.loqui;

public class DailyData
{
    int EmotionScore, scoreDay, scoreMonth, scoreYear;

    DailyData(int emotionScore, int newScoreDay, int newScoreMonth, int newScoreYear)
    {
        EmotionScore = emotionScore;
        scoreDay = newScoreDay;
        scoreMonth = newScoreMonth;
        scoreYear = newScoreYear;
    }

    DailyData()
    {

    }

    public int getEmotionScore()
    {
        return EmotionScore;
    }

    public void setEmotionScore(int emotionScore)
    {
        EmotionScore = emotionScore;
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
