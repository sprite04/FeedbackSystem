using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace API.ViewModel
{
    public class Statistic
    {
        public int QuestionID { get; set; }
        public List<int> Amount { get; set; }
    }
}