using API.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class QuestionController : ApiController
    {
        [HttpGet]
        public List<Question> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Question> questions = context.Questions.Where(x => x.IsDeleted == false).ToList();
            foreach (var x in questions)
            {
                x.Answers.Clear();
                x.Feedbacks.Clear();
                x.Topic.Questions.Clear();

            }
            return questions;
        }

        [HttpGet]
        public Question GetQuestionById(int id)
        {
            SystemFeedback context = new SystemFeedback();
            Question question = context.Questions.FirstOrDefault(x => x.QuestionID == id && x.IsDeleted == false);
            if (question != null)
            {
                question.Answers.Clear();
                question.Feedbacks.Clear();
                question.Topic.Questions.Clear();
            }
            return question;
        }

        [HttpGet]
        public List<Question> GetQuestionByIdTopic(int idTopic)
        {
            SystemFeedback context = new SystemFeedback();
            List<Question> questions = context.Questions.Where(x => x.TopicID==idTopic && x.IsDeleted == false).ToList();
            foreach (var x in questions)
            {
                x.Answers.Clear();
                x.Feedbacks.Clear();
                x.Topic.Questions.Clear();

            }
            return questions;
        }

        [HttpGet]
        public int CheckQuestionUsed(int idUsed)
        {
            SystemFeedback context = new SystemFeedback();
            Question question = context.Questions.FirstOrDefault(x => x.QuestionID == idUsed && x.IsDeleted == false);
            if (question != null)
            {
                if (question.Feedbacks.Count > 0)
                    return question.Feedbacks.Count;
            }
            return -1;
        }

        [HttpPost]
        public bool AddQuestion(Question q)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Question question = q;
                question.IsDeleted = false;
                context.Questions.Add(question);
                context.SaveChanges();
                return true;
            }
            catch { }
            return false;
        }

        [HttpPut]
        public bool EditQuestion(Question q)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Question question = context.Questions.FirstOrDefault(x => x.QuestionID==q.QuestionID && x.IsDeleted == false);
                if (question != null)
                {
                    question.TopicID = q.TopicID;
                    question.QuestionContent = q.QuestionContent;

                    context.Entry(question).State = EntityState.Modified;
                    context.SaveChanges();
                    return true;
                }

            }
            catch { }

            return false;
        }

        [HttpDelete]
        public bool DeleteQuestion(int id)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Question question = context.Questions.FirstOrDefault(x => x.QuestionID == id && x.IsDeleted == false);
                if (question != null)
                {
                    question.IsDeleted = true;

                    //Để cái này nếu muốn Feedback_Question tương ứng với Question xoá luôn
                    question.Feedbacks.Clear();

                    context.Entry(question).State = EntityState.Modified;
                    context.SaveChanges();
                    return true;
                }

            }
            catch { }

            return false;
        }
    }
}
