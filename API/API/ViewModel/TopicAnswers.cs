using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace API.ViewModel
{
    public class TopicAnswers
    {
        public Topic Topic { get; set; }
        public List<Answer> Answers { get; set; }

        
    }
}