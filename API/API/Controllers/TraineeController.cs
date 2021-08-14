using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class TraineeController : ApiController
    {
        [HttpGet]
        public List<Trainee> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Trainee> trainees = context.Trainees.Where(x => x.IsActive == true).ToList();
            foreach (var x in trainees)
            {
                x.Answers.Clear();
                x.Trainee_Comment.Clear();
                x.Classes.Clear();
                x.Trainee_Assignment.Clear();
            }
            return trainees;
        }

        [HttpGet]
        public Trainee GetTraineeByUsername(String username)
        {
            SystemFeedback context = new SystemFeedback();
            Trainee trainee = context.Trainees.FirstOrDefault(x => x.IsActive == true && x.UserName == username);
            if (trainee != null)
            {
                trainee.Answers.Clear();
                trainee.Trainee_Comment.Clear();
                trainee.Classes.Clear();
                trainee.Trainee_Assignment.Clear();
            }
            return trainee;
        }

        public Trainee GetTrainee(String username, String password)
        {
            SystemFeedback context = new SystemFeedback();
            Trainee trainee = context.Trainees.FirstOrDefault(x => x.IsActive== true && x.UserName==username && x.Password==password);
            if (trainee != null)
            {
                trainee.Answers.Clear();
                trainee.Trainee_Comment.Clear();
                trainee.Classes.Clear();
                trainee.Trainee_Assignment.Clear();
            }
            return trainee;
        }
    }
}
