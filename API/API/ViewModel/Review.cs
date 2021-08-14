using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace API.ViewModel
{
    public class Review
    {
        public List<Answer> Answers { get; set; }
        public Trainee_Comment TraineeComment { get;set; }
    }
}