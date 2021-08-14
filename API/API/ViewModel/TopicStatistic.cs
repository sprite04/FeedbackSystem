using API.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace API.ViewModel
{
    public class TopicStatistic
    {
        public Topic Topic { get; set; }
        public List<Statistic> Statistics { get; set; }
    }
}