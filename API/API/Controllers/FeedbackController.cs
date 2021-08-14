using API.Models;
using API.ViewModel;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class FeedbackController : ApiController
    {
        [HttpGet]
        public List<Feedback> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Feedback> feedbacks = context.Feedbacks.Where(x => x.IsDeleted == false).ToList();
            foreach(var x in feedbacks)
            {
                x.Modules.Clear();
                
                foreach(var y in x.Questions)
                {
                    y.Feedbacks.Clear();
                    y.Answers.Clear();
                    y.Topic.Questions.Clear();
                }    
                x.Admin.Feedbacks.Clear();
                x.Admin.Modules.Clear();
                x.TypeFeedback.Feedbacks.Clear();
            }    
            return feedbacks;
        }

        [HttpGet]
        public Feedback GetFeedbackById(int id)
        {
            SystemFeedback context = new SystemFeedback();
            Feedback feedback = context.Feedbacks.FirstOrDefault(x => x.FeedbackID == id && x.IsDeleted == false);
            if(feedback!=null)
            {
                feedback.Modules.Clear();
                foreach (var y in feedback.Questions)
                {
                    y.Feedbacks.Clear();
                    y.Answers.Clear();
                    y.Topic.Questions.Clear();
                }
                feedback.Admin.Feedbacks.Clear();
                feedback.Admin.Modules.Clear();
                feedback.TypeFeedback.Feedbacks.Clear();
            }
            return feedback;
        }

        [HttpGet]
        public int CheckFeedbackUsed(int idUsed)
        {
            SystemFeedback context = new SystemFeedback();
            Feedback feedback = context.Feedbacks.FirstOrDefault(x => x.FeedbackID == idUsed && x.IsDeleted == false);
            int count = 0;
            if (feedback != null)
            {
                if (feedback.Modules.Count > 0)
                {
                    foreach(var module in feedback.Modules)
                    {
                        if(module.FeedbackStartTime!=null && module.FeedbackEndTime!=null)
                        {
                            DateTime date = DateTime.Now;
                            if (date.CompareTo(module.FeedbackStartTime) >= 0 && date.CompareTo(module.FeedbackEndTime)<=0)
                            {
                                count++;
                            }    
                        }    
                    }    
                }    
            }
            return count;
        }


        //xem cái này có còn cần hay không
        [HttpGet]
        public int CheckFeedbackUsedByModule(int idUsedModule)
        {
            SystemFeedback context = new SystemFeedback();
            Feedback feedback = context.Feedbacks.FirstOrDefault(x => x.FeedbackID == idUsedModule && x.IsDeleted == false);
            if (feedback != null)
            {
                return feedback.Modules.Count();
            }
            return 0;
        }



        [HttpPost]
        public bool AddFeedback(FeedbackQuestion feedbackQuestion)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Feedback feedback = feedbackQuestion.Feedback;
                foreach (var x in feedbackQuestion.Questions)
                {
                    Question question = context.Questions.FirstOrDefault(y => y.QuestionID == x && y.IsDeleted == false);

                    if (question != null)
                    {
                        feedback.Questions.Add(question);
                    }
                }
                feedback.IsDeleted = false;
                context.Feedbacks.Add(feedback);
                context.SaveChanges();
                return true;
            }
            catch { }
            return false;
        }




        //Cần test kỹ hàm này
        [HttpPut]
        public bool EditFeedback(FeedbackQuestion feedbackQuestion)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Feedback feedback = context.Feedbacks.FirstOrDefault(x => x.FeedbackID == feedbackQuestion.Feedback.FeedbackID && x.IsDeleted == false);
                if (feedback != null)
                {
                    feedback.Title = feedbackQuestion.Feedback.Title;
                    feedback.TypeFeedbackID = feedbackQuestion.Feedback.TypeFeedbackID;
                    feedback.AdminID = feedbackQuestion.Feedback.AdminID;

                    //Xoá Question cũ
                    feedback.Questions.Clear();

                    foreach (var x in feedbackQuestion.Questions)
                    {
                        Question question = context.Questions.SingleOrDefault(y => y.QuestionID == x && y.IsDeleted == false);

                        if (question != null)
                        {
                            feedback.Questions.Add(question);
                        }
                    }

                    context.Entry(feedback).State = EntityState.Modified;
                    context.SaveChanges();
                    return true;
                }
            }
            catch { }

            return false;
        }

        [HttpDelete]
        public bool DeleteFeedback(int id)
        {
            try
            {
                SystemFeedback context = new SystemFeedback();
                Feedback feedback = context.Feedbacks.FirstOrDefault(x => x.FeedbackID == id && x.IsDeleted == false);
                if (feedback != null)
                {
                    feedback.IsDeleted = true;

                    //Để cái này nếu muốn Feedback_Question tương ứng với Feedback xoá luôn
                    feedback.Questions.Clear();

                    context.Entry(feedback).State = EntityState.Modified;
                    context.SaveChanges();

                    return true;
                }
            }
            catch { }

            return false;
        }
    }
}
