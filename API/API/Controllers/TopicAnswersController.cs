using API.Models;
using API.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class TopicAnswersController : ApiController
    {
        [HttpGet]
        public List<TopicAnswers> GetTopicAnswersByClassModule(int idClass , int idModule)
        {
            SystemFeedback context = new SystemFeedback();
            List<Topic> topics = context.Topics.Where(x => x.Questions.Count() > 0).ToList();
            List<TopicAnswers> topicAnswersList = new List<TopicAnswers>();
            foreach (var topic in topics)
            {
                List<Answer> answers = context.Answers.Where(x => x.ClassID == idClass && x.ModuleID == idModule && x.Question.TopicID == topic.TopicID && x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Question.IsDeleted == false).ToList();
                foreach (var x in answers)
                {
                    x.Module.Answers.Clear();
                    x.Module.Trainee_Comment.Clear();
                    x.Module.Assignments.Clear();
                    x.Module = null;
                    x.Class.Trainee_Comment.Clear();
                    x.Class.Answers.Clear();
                    x.Class.Trainees.Clear();
                    x.Class.Assignments.Clear();
                    x.Class = null;
                    x.Question.Answers.Clear();
                    x.Question.Feedbacks.Clear();
                    x.Question = null;
                    
                    x.Trainee.Answers.Clear();
                    x.Trainee.Trainee_Comment.Clear();
                    x.Trainee.Classes.Clear();
                    x.Trainee.Trainee_Assignment.Clear();
                    x.Trainee = null;

                }
                
                if (answers.Count() > 0)
                {
                    TopicAnswers topicAnswers = new TopicAnswers();
                    foreach(var y in topic.Questions)
                    {
                        y.Answers.Clear();
                        y.Feedbacks.Clear();
                        y.Topic = null;
                    }

                    topicAnswers.Topic = topic;
                    topicAnswers.Answers = answers;
                    topicAnswersList.Add(topicAnswers);
                }
                
            }
            return topicAnswersList;
        }


        [HttpGet]
        public List<TopicStatistic> GetTopicStatisticByClassModule(int classID, int moduleID)
        {
            SystemFeedback context = new SystemFeedback();
            List<Topic> topics = context.Topics.Where(x => x.Questions.Count() > 0).ToList();
            List<TopicStatistic> topicStatistics = new List<TopicStatistic>();
            foreach (var topic in topics)
            {
                List<Answer> answers = context.Answers.Where(x => x.ClassID == classID && x.ModuleID == moduleID && x.Question.TopicID == topic.TopicID && x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Question.IsDeleted == false).ToList();
                foreach (var x in answers)
                {
                    x.Module.Answers.Clear();
                    x.Module.Trainee_Comment.Clear();
                    x.Module.Assignments.Clear();
                    x.Module = null;
                    x.Class.Trainee_Comment.Clear();
                    x.Class.Answers.Clear();
                    x.Class.Trainees.Clear();
                    x.Class.Assignments.Clear();
                    x.Class = null;
                    x.Question.Answers.Clear();
                    x.Question.Feedbacks.Clear();
                    x.Question = null;

                    x.Trainee.Answers.Clear();
                    x.Trainee.Trainee_Comment.Clear();
                    x.Trainee.Classes.Clear();
                    x.Trainee.Trainee_Assignment.Clear();
                    x.Trainee = null;

                }

                if (answers.Count() > 0)
                {
                    TopicStatistic topicStatistic = new TopicStatistic();
                    topicStatistic.Statistics = new List<Statistic>();
                    foreach (var y in topic.Questions)
                    {
                        Statistic statistic = new Statistic();
                        statistic.Amount = new List<int>();
                        statistic.QuestionID = y.QuestionID;

                        List<int> amountList = new List<int>();
                        for (int i = 0; i < 5; i++)
                        {
                            int sum = 0;
                            foreach (var a in answers)
                            {
                                if (a.Value == i && a.QuestionID==y.QuestionID)
                                    sum++;
                            }
                            amountList.Add(sum);
                        }

                        statistic.Amount.AddRange(amountList);  
                        
                        topicStatistic.Statistics.Add(statistic);
                    }
                    foreach (var y in topic.Questions)
                    {
                        y.Answers.Clear();
                        y.Feedbacks.Clear();
                        y.Topic = null;
                    }
                    topicStatistic.Topic = topic;
                    topicStatistics.Add(topicStatistic);
                }
            }
            return topicStatistics;
        }
    }
}
