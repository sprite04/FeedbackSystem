using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class TraineeCommentController : ApiController
    {
        [HttpGet]
        public List<Trainee_Comment> GetAll(int classId, int moduleId)
        {
            SystemFeedback context = new SystemFeedback();
            List<Trainee_Comment> traineeComments = context.Trainee_Comment.Where(x => x.ClassID==classId&&x.ModuleID==moduleId && x.Class.IsDeleted == false && x.Module.IsDeleted == false &&  x.Trainee.IsActive == true).ToList();
            foreach (var x in traineeComments)
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
                x.Trainee.Answers.Clear();
                x.Trainee.Trainee_Comment.Clear();
                x.Trainee.Classes.Clear();
                x.Trainee.Trainee_Assignment.Clear();
                x.Trainee = null;

            }
            return traineeComments;
        }

        [HttpGet]
        public bool CheckCommentUsed(int idClass, int idModule, String idTrainee)
        {
            SystemFeedback context = new SystemFeedback();
            List<Trainee_Comment> answers = context.Trainee_Comment.Where(x => x.ClassID == idClass && x.ModuleID == idModule && x.TraineeID.Trim().Equals(idTrainee.Trim()) && x.Class.IsDeleted == false && x.Module.IsDeleted == false &&  x.Trainee.IsActive == true).ToList();
            return answers.Count() > 0;
        }

        [HttpPost]
        public bool addTraineeComment(Trainee_Comment traineeComment)
        {
            SystemFeedback context = new SystemFeedback();
            try
            {
                context.Trainee_Comment.Add(traineeComment);
                context.SaveChanges();
                return true;
            }
            catch(Exception ex){}
            return false;
        }
    }
}
