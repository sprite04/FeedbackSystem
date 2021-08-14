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
    public class AnswerController : ApiController
    {
        [HttpGet]
        public List<Answer> GetAll(String idTrainee)
        {
            SystemFeedback context = new SystemFeedback();
            List<Answer> answers = context.Answers.Where(x => x.TraineeID==idTrainee && x.Class.IsDeleted==false && x.Module.IsDeleted==false && x.Question.IsDeleted==false && x.Trainee.IsActive==true).ToList();
            foreach (var x in answers)
            {
                x.Module.Answers.Clear();
                x.Module.Trainee_Comment.Clear();
                x.Module.Assignments.Clear();
                x.Class.Trainee_Comment.Clear();
                x.Class.Answers.Clear();
                x.Class.Trainees.Clear();
                x.Class.Assignments.Clear();
                x.Question.Answers.Clear();
                x.Question.Feedbacks.Clear();
                x.Trainee.Answers.Clear();
                x.Trainee.Trainee_Comment.Clear();
                x.Trainee.Classes.Clear();
                x.Trainee.Trainee_Assignment.Clear();

            }
            return answers;
        }

        //Đang test lại
        public List<Answer> GetAnswersByClassModuleTopic(int idTopic, int idModule, int idClass)
        {
            SystemFeedback context = new SystemFeedback();
            List<Answer> answers = context.Answers.Where(x => x.ClassID==idClass && x.ModuleID==idModule && x.Question.TopicID==idTopic&& x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Question.IsDeleted == false ).ToList();
            foreach (var x in answers)
            {
                x.Module.Answers.Clear();
                x.Module.Trainee_Comment.Clear();
                x.Module.Assignments.Clear();
                x.Class.Trainee_Comment.Clear();
                x.Class.Answers.Clear();
                x.Class.Trainees.Clear();
                x.Class.Assignments.Clear();
                x.Question.Answers.Clear();
                x.Question.Feedbacks.Clear();
                x.Trainee.Answers.Clear();
                x.Trainee.Trainee_Comment.Clear();
                x.Trainee.Classes.Clear();
                x.Trainee.Trainee_Assignment.Clear();

            }
            return answers;
        }


        [HttpGet]
        public List<Answer> GetAnswersByClassModule(int idClass, int idModule)
        {
            SystemFeedback context = new SystemFeedback();
            List<Answer> answers = context.Answers.Where(x => x.ClassID == idClass && x.ModuleID == idModule && x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Question.IsDeleted == false && x.Trainee.IsActive == true).ToList();
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
            return answers;
        }

        [HttpGet]
        public List<Answer> GetAnswerByClassModuleTrainee(int idClass, int idModule, String idTrainee)
        {
            SystemFeedback context = new SystemFeedback();
            List<Answer> answers = context.Answers.Where(x => x.ClassID == idClass && x.ModuleID == idModule&&x.TraineeID==idTrainee && x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Question.IsDeleted == false && x.Trainee.IsActive == true).ToList();
            foreach (var x in answers)
            {
                x.Module.Answers.Clear();
                x.Module.Trainee_Comment.Clear();
                x.Module.Assignments.Clear();
                x.Class.Trainee_Comment.Clear();
                x.Class.Answers.Clear();
                x.Class.Trainees.Clear();
                x.Class.Assignments.Clear();
                x.Question.Answers.Clear();
                x.Question.Feedbacks.Clear();
                x.Trainee.Answers.Clear();
                x.Trainee.Trainee_Comment.Clear();
                x.Trainee.Classes.Clear();
                x.Trainee.Trainee_Assignment.Clear();

            }
            return answers;
        }

        

        [HttpGet]
        public bool CheckAnswerUsed(int clss, int module, String traineeId)
        {
            bool used = true;
            SystemFeedback context = new SystemFeedback();
            Trainee trainee= context.Trainees.FirstOrDefault(x => x.UserName == traineeId);
            List<Class> classes = trainee.Classes.Where(x => x.IsDeleted == false).ToList();
            List<Assignment> assignments = new List<Assignment>();
            List<Assignment> assignList = new List<Assignment>();
            DateTime date = DateTime.Now;

            foreach (var cl in classes)
            {
                List<Assignment> assignmentList = context.Assignments.Where(x => x.ClassID == cl.ClassID && x.Module.IsDeleted == false).ToList();
                foreach (var x in assignmentList)
                {
                    DateTime dateStart = x.Module.FeedbackStartTime ?? DateTime.Now;
                    DateTime dateEnd = x.Module.FeedbackEndTime ?? DateTime.Now;
                    if (x != null && date.CompareTo(dateStart) >= 0 && date.CompareTo(dateEnd) <= 0)
                    {
                        assignments.Add(x);
                    }
                }



                foreach (var x in trainee.Trainee_Assignment)
                {
                    foreach (var y in assignments)
                    {
                        if (y.RegistrationCode == x.RegistrationCode)
                        {
                            bool exist = false;
                            foreach (var z in assignList)
                            {
                                if (z.ModuleID == y.ModuleID && z.ClassID == y.ClassID)
                                {
                                    exist = true;
                                }
                            }

                            if (exist == false)
                                assignList.Add(y);
                        }
                    }
                }
            }

            Assignment assignment = assignList.FirstOrDefault(x => x.ClassID == clss && x.ModuleID == module);
            if(assignment!=null)
            {
                Feedback feedback = assignment.Module.Feedback;
                List<Question> questions = new List<Question>();
                foreach(var x in feedback.Questions)
                {
                    if(x.IsDeleted==false)
                    {
                        questions.Add(x);
                    }    
                }
                


                Trainee_Comment traineeComment = context.Trainee_Comment.FirstOrDefault(x => x.ClassID == clss && x.ModuleID == module && x.TraineeID == traineeId && x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Trainee.IsActive == true);
                List<Answer> answers = context.Answers.Where(x => x.ClassID == clss && x.ModuleID == module && x.TraineeID == traineeId && x.Class.IsDeleted == false && x.Module.IsDeleted == false && x.Question.IsDeleted == false && x.Trainee.IsActive == true).ToList();
                if (answers.Count() == questions.Count() && traineeComment!=null)
                    used = true;
                else
                    used = false;
            }


            
            return used;
        }

        /*[HttpPost]
        public bool AddAnswers(List<Answer> answers)
        {
            SystemFeedback context = new SystemFeedback();
            String str = "";
            try
            {
                context.Answers.AddRange(answers);
                context.SaveChanges();
                return true;
            }
            catch (Exception e) { }

            return false;
        }*/


        [HttpPost]
        public bool AddAnswers(Review review)
        {
            SystemFeedback context = new SystemFeedback();
            try
            {
                foreach(var x in review.Answers)
                {
                    Answer answer = context.Answers.FirstOrDefault(y => y.ClassID == x.ClassID && y.ModuleID == x.ModuleID && y.TraineeID == x.TraineeID && y.QuestionID == x.QuestionID);
                    if (answer == null)
                        context.Answers.Add(x);
                    else
                    {
                        context.Answers.Remove(answer);
                        context.Answers.Add(x);
                    }
                }
                Trainee_Comment comment = context.Trainee_Comment.FirstOrDefault(x => x.ClassID == review.TraineeComment.ClassID && x.ModuleID == review.TraineeComment.ModuleID && x.TraineeID == review.TraineeComment.TraineeID);
                if(comment==null)
                    context.Trainee_Comment.Add(review.TraineeComment);
                else
                {
                    context.Trainee_Comment.Remove(comment);
                    context.Trainee_Comment.Add(review.TraineeComment);
                }
                context.SaveChanges();
                return true;
            }
            catch (Exception e) { }

            return false;
        }

        /* [HttpPost]
         public bool AddAnswers(Answer answer)
         {
             SystemFeedback context = new SystemFeedback();
             try
             {
                 context.Answers.Add(answer);
                 context.SaveChanges();
                 return true;
             }
             catch (Exception e) { }

             return false;
         }*/

    }
}
