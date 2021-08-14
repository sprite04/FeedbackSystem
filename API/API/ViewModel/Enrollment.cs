using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using API.Models;

namespace API.ViewModel
{
    public class Enrollment
    {
        public Class Class { get; set; }
        public Trainee Trainee { get; set; }
        public String TraineeID { get; set; }
        public int ClassID { get; set; }
        public Enrollment() { }
        public Enrollment(int ClassID, String TraineeID, Class Class, Trainee Trainee)
        {
            this.ClassID = ClassID;
            this.TraineeID = TraineeID;
            this.Class = Class;
            this.Trainee = Trainee;
        }
    }
}