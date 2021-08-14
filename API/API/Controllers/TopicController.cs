using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class TopicController : ApiController
    {

        //Cần dò lại các hàm của topic liên quan đến xoá question thì thực hiện thế nào
        //Cần dò lại các hàm của answer xem ảnh hưởng thế nào nếu question bị xoá
        //Đã dò 2 điều kiện trên 1 lần nhưng cần test lại
        [HttpGet]
        public List<Topic> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Topic> topics= context.Topics.ToList();
            List<Question> questions = context.Questions.Where(x => x.IsDeleted == false).ToList();
            foreach (var topic in topics)
            {
                topic.Questions.Clear();
                foreach(var x in questions)
                {
                    if (x.TopicID == topic.TopicID)
                    {
                        topic.Questions.Add(x);
                    }
                }

                foreach(var question in topic.Questions)
                {
                    question.Answers.Clear();
                    question.Feedbacks.Clear();
                }    
            }
            return topics;
        }

        [HttpGet]
        public List<Topic> GetAll(int id)
        {
            SystemFeedback context = new SystemFeedback();
            List<Topic> topics = context.Topics.ToList();
            List<Question> questions = context.Questions.Where(x => x.IsDeleted == false).ToList();
            foreach (var topic in topics)
            {
                topic.Questions.Clear();
                
            }
            return topics;
        }



        [HttpGet]
        public List<Topic> GetTopicByIdFeedback(int idFeedback)
        {
            SystemFeedback context = new SystemFeedback();
            Feedback feedback = context.Feedbacks.FirstOrDefault(x => x.FeedbackID == idFeedback && x.IsDeleted==false);
            List<Topic> topicList = new List<Topic>();
            List<Topic> topics = new List<Topic>();
            if (feedback!=null)
            {
                foreach(var x in feedback.Questions)
                {
                    if (x.IsDeleted == false)
                    {
                        bool exist = false;
                        foreach (var y in topicList)
                        {
                            if (y.TopicID == x.TopicID)
                                exist = true;
                        }

                        if (exist == false)
                            topicList.Add(x.Topic);
                    }
                }    
            }   
            foreach (var topic in topicList)
            {
                topic.Questions.Clear();
                foreach(var x in feedback.Questions)
                {
                    if (x.TopicID == topic.TopicID)
                        topic.Questions.Add(x);
                }

                if (topic.Questions.Count() > 0)
                {
                    foreach (var question in topic.Questions)
                    {
                        question.Answers.Clear();
                        question.Feedbacks.Clear();
                    }
                    topics.Add(topic);
                }
                
            }

            foreach(var x in topics)
            {
                foreach(var y in x.Questions)
                {
                    y.Topic = null;
                }    
            }    
            return topics;
        }


    }
}
