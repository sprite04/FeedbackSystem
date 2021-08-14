using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace API.ViewModel
{
    public class FeedbackQuestion
    {
        public Feedback Feedback { get; set; }
        public List<int> Questions { get; set; }
    }
}