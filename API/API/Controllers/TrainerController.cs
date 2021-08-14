using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace API.Controllers
{
    public class TrainerController : ApiController
    {
        [HttpGet]
        public List<Trainer> GetAll()
        {
            SystemFeedback context = new SystemFeedback();
            List<Trainer> trainers = context.Trainers.Where(x => x.IsActive == true).ToList();
            foreach (var x in trainers)
            {
                x.Skill.Trainers.Clear();
                x.Assignments.Clear();
            }
            return trainers;
        }

        [HttpGet]
        public Trainer GetTrainer(String username, String password)
        {
            SystemFeedback context = new SystemFeedback();
            Trainer trainer = context.Trainers.FirstOrDefault(x => x.IsActive == true && x.UserName == username && x.Password == password);
            if (trainer != null)
            {
                trainer.Skill.Trainers.Clear();
                trainer.Assignments.Clear();
            }
            return trainer;
        }
    }
}
